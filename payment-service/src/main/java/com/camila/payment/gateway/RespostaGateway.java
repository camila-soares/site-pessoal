package com.camila.payment.gateway;

import com.camila.payment.model.StatusPagamento;
import lombok.Builder;

@Builder
public record RespostaGateway(
        StatusPagamento status,
        String gatewayId,
        String codigoAutorizacao,
        String motivoRecusa
) {
    public static RespostaGateway aprovado(String gatewayId, String codigoAutorizacao) {
        return RespostaGateway.builder()
                .status(StatusPagamento.APROVADO)
                .gatewayId(gatewayId)
                .codigoAutorizacao(codigoAutorizacao)
                .build();
    }

    public static RespostaGateway pendente(String gatewayId) {
        return RespostaGateway.builder()
                .status(StatusPagamento.PENDENTE)
                .gatewayId(gatewayId)
                .build();
    }

    public static RespostaGateway recusado(String gatewayId, String motivo) {
        return RespostaGateway.builder()
                .status(StatusPagamento.RECUSADO)
                .gatewayId(gatewayId)
                .motivoRecusa(motivo)
                .build();
    }
}
