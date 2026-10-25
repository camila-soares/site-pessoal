package com.camila.payment.service;

import com.camila.payment.dto.PagamentoRequest;
import com.camila.payment.dto.PagamentoResponse;
import com.camila.payment.dto.WebhookRequest;
import com.camila.payment.exception.RecursoNaoEncontradoException;
import com.camila.payment.exception.RegraDeNegocioException;
import com.camila.payment.gateway.GatewayPagamento;
import com.camila.payment.gateway.RespostaGateway;
import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;
import com.camila.payment.model.StatusPagamento;
import com.camila.payment.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final NotificacaoClient notificacaoClient;
    private final List<GatewayPagamento> gateways;

    private Map<FormaPagamento, GatewayPagamento> gatewayMap() {
        return gateways.stream()
                .collect(Collectors.toMap(GatewayPagamento::formaSuportada, Function.identity()));
    }

    @Transactional(readOnly = true)
    public List<PagamentoResponse> listarPorPedido(Long pedidoId) {
        return pagamentoRepository.findByPedidoId(pedidoId).stream()
                .map(PagamentoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PagamentoResponse buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
                .map(PagamentoResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado: " + id));
    }

    @Transactional
    public PagamentoResponse processar(PagamentoRequest request) {
        GatewayPagamento gateway = gatewayMap().get(request.getFormaPagamento());
        if (gateway == null) {
            // CARTAO_DEBITO usa o mesmo gateway de cartão
            if (request.getFormaPagamento() == FormaPagamento.CARTAO_DEBITO) {
                gateway = gatewayMap().get(FormaPagamento.CARTAO_CREDITO);
            } else {
                throw new RegraDeNegocioException("Forma de pagamento não suportada: " + request.getFormaPagamento());
            }
        }

        Pagamento pagamento = Pagamento.builder()
                .pedidoId(request.getPedidoId())
                .clienteEmail(request.getClienteEmail())
                .clienteNome(request.getClienteNome())
                .valor(request.getValor())
                .formaPagamento(request.getFormaPagamento())
                .status(StatusPagamento.PROCESSANDO)
                .build();

        pagamento = pagamentoRepository.save(pagamento);

        RespostaGateway resposta = gateway.processar(pagamento);
        aplicarResposta(pagamento, resposta);
        pagamento = pagamentoRepository.save(pagamento);

        notificarCliente(pagamento);
        return PagamentoResponse.from(pagamento);
    }

    @Transactional
    public PagamentoResponse processarWebhook(WebhookRequest webhook) {
        Pagamento pagamento = pagamentoRepository.findByGatewayId(webhook.getGatewayId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado para gatewayId: " + webhook.getGatewayId()));

        if (pagamento.getStatus() == StatusPagamento.APROVADO || pagamento.getStatus() == StatusPagamento.ESTORNADO) {
            throw new RegraDeNegocioException("Pagamento já finalizado com status: " + pagamento.getStatus());
        }

        pagamento.setStatus(webhook.getStatus());
        pagamento.setCodigoAutorizacao(webhook.getCodigoAutorizacao());
        pagamento.setMotivoRecusa(webhook.getMotivoRecusa());
        pagamento = pagamentoRepository.save(pagamento);

        notificarCliente(pagamento);
        log.info("Webhook processado — gatewayId={} novoStatus={}", webhook.getGatewayId(), webhook.getStatus());
        return PagamentoResponse.from(pagamento);
    }

    @Transactional
    public PagamentoResponse estornar(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado: " + id));

        if (pagamento.getStatus() != StatusPagamento.APROVADO) {
            throw new RegraDeNegocioException("Apenas pagamentos APROVADOS podem ser estornados");
        }

        pagamento.setStatus(StatusPagamento.ESTORNADO);
        pagamento = pagamentoRepository.save(pagamento);

        notificacaoClient.enviarNotificacaoPagamento(
                pagamento.getPedidoId(), pagamento.getClienteEmail(), pagamento.getClienteNome(),
                "estornado", "O valor será devolvido em até 10 dias úteis."
        );

        log.info("Pagamento estornado — id={} pedidoId={}", id, pagamento.getPedidoId());
        return PagamentoResponse.from(pagamento);
    }

    private void aplicarResposta(Pagamento pagamento, RespostaGateway resposta) {
        pagamento.setStatus(resposta.status());
        pagamento.setGatewayId(resposta.gatewayId());
        pagamento.setCodigoAutorizacao(resposta.codigoAutorizacao());
        pagamento.setMotivoRecusa(resposta.motivoRecusa());
    }

    private void notificarCliente(Pagamento pagamento) {
        String statusLabel = switch (pagamento.getStatus()) {
            case APROVADO  -> "aprovado";
            case RECUSADO  -> "recusado";
            case PENDENTE  -> "gerado e aguarda confirmação";
            case ESTORNADO -> "estornado";
            default        -> pagamento.getStatus().name().toLowerCase();
        };
        String detalhes = pagamento.getMotivoRecusa() != null
                ? "Motivo: " + pagamento.getMotivoRecusa() : null;

        notificacaoClient.enviarNotificacaoPagamento(
                pagamento.getPedidoId(), pagamento.getClienteEmail(),
                pagamento.getClienteNome(), statusLabel, detalhes
        );
    }
}
