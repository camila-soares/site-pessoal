package com.camila.sales.dto;

import com.camila.sales.model.ItemPedido;
import com.camila.sales.model.Pedido;
import com.camila.sales.model.StatusPedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoResponse {

    private Long id;
    private Long clienteId;
    private String clienteNome;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private String observacao;
    private LocalDateTime criadoEm;
    private List<ItemResponse> itens;

    @Data
    public static class ItemResponse {
        private Long produtoId;
        private String produtoNome;
        private Integer quantidade;
        private BigDecimal precoUnitario;
        private BigDecimal subtotal;

        public static ItemResponse from(ItemPedido item) {
            ItemResponse dto = new ItemResponse();
            dto.produtoId = item.getProduto().getId();
            dto.produtoNome = item.getProduto().getNome();
            dto.quantidade = item.getQuantidade();
            dto.precoUnitario = item.getPrecoUnitario();
            dto.subtotal = item.getSubtotal();
            return dto;
        }
    }

    public static PedidoResponse from(Pedido p) {
        PedidoResponse dto = new PedidoResponse();
        dto.id = p.getId();
        dto.clienteId = p.getCliente().getId();
        dto.clienteNome = p.getCliente().getNome();
        dto.status = p.getStatus();
        dto.valorTotal = p.getValorTotal();
        dto.observacao = p.getObservacao();
        dto.criadoEm = p.getCriadoEm();
        dto.itens = p.getItens().stream().map(ItemResponse::from).toList();
        return dto;
    }
}
