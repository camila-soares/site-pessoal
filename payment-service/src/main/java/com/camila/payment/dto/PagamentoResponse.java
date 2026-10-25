package com.camila.payment.dto;

import com.camila.payment.model.FormaPagamento;
import com.camila.payment.model.Pagamento;
import com.camila.payment.model.StatusPagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagamentoResponse {
    private Long id;
    private Long pedidoId;
    private String clienteEmail;
    private String clienteNome;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private StatusPagamento status;
    private String gatewayId;
    private String codigoAutorizacao;
    private String motivoRecusa;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public static PagamentoResponse from(Pagamento p) {
        PagamentoResponse dto = new PagamentoResponse();
        dto.id = p.getId();
        dto.pedidoId = p.getPedidoId();
        dto.clienteEmail = p.getClienteEmail();
        dto.clienteNome = p.getClienteNome();
        dto.valor = p.getValor();
        dto.formaPagamento = p.getFormaPagamento();
        dto.status = p.getStatus();
        dto.gatewayId = p.getGatewayId();
        dto.codigoAutorizacao = p.getCodigoAutorizacao();
        dto.motivoRecusa = p.getMotivoRecusa();
        dto.criadoEm = p.getCriadoEm();
        dto.atualizadoEm = p.getAtualizadoEm();
        return dto;
    }
}
