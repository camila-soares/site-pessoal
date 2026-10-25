package com.camila.sales.service;

import com.camila.sales.dto.PedidoRequest;
import com.camila.sales.dto.PedidoResponse;
import com.camila.sales.exception.RecursoNaoEncontradoException;
import com.camila.sales.exception.RegraDeNegocioException;
import com.camila.sales.model.*;
import com.camila.sales.repository.ClienteRepository;
import com.camila.sales.repository.PedidoRepository;
import com.camila.sales.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarTodos() {
        return pedidoRepository.findAll().stream()
                .map(PedidoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscarPorId(Long id) {
        return pedidoRepository.findByIdComDetalhes(id)
                .map(PedidoResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(PedidoResponse::from)
                .toList();
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Cliente cliente = clienteRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + request.getClienteId()));

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .observacao(request.getObservacao())
                .build();

        for (var itemReq : request.getItens()) {
            Produto produto = produtoRepository.findById(itemReq.getProdutoId())
                    .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado: " + itemReq.getProdutoId()));

            if (!produto.getAtivo()) {
                throw new RegraDeNegocioException("Produto inativo: " + produto.getNome());
            }
            if (produto.getEstoque() < itemReq.getQuantidade()) {
                throw new RegraDeNegocioException(
                        "Estoque insuficiente para '%s'. Disponível: %d".formatted(produto.getNome(), produto.getEstoque())
                );
            }

            ItemPedido item = ItemPedido.builder()
                    .pedido(pedido)
                    .produto(produto)
                    .quantidade(itemReq.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .build();

            pedido.getItens().add(item);
            produto.setEstoque(produto.getEstoque() - itemReq.getQuantidade());
            produtoRepository.save(produto);
        }

        pedido.calcularTotal();
        Pedido salvo = pedidoRepository.save(pedido);
        log.info("Pedido criado: {} para cliente: {}", salvo.getId(), cliente.getNome());
        return PedidoResponse.from(salvo);
    }

    @Transactional
    public PedidoResponse atualizarStatus(Long id, StatusPedido novoStatus) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));

        validarTransicaoStatus(pedido.getStatus(), novoStatus);
        pedido.setStatus(novoStatus);

        if (novoStatus == StatusPedido.CANCELADO) {
            devolverEstoque(pedido);
        }

        log.info("Pedido {} status atualizado para: {}", id, novoStatus);
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo) {
        if (atual == StatusPedido.ENTREGUE || atual == StatusPedido.CANCELADO) {
            throw new RegraDeNegocioException("Pedido com status '%s' não pode ser alterado".formatted(atual));
        }
        if (novo == StatusPedido.PENDENTE) {
            throw new RegraDeNegocioException("Não é possível retornar ao status PENDENTE");
        }
    }

    private void devolverEstoque(Pedido pedido) {
        pedido.getItens().forEach(item -> {
            Produto produto = item.getProduto();
            produto.setEstoque(produto.getEstoque() + item.getQuantidade());
            produtoRepository.save(produto);
        });
    }
}
