package com.camila.payment.dto;

import com.camila.payment.model.StatusPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WebhookRequest {

    @NotBlank(message = "gatewayId é obrigatório")
    private String gatewayId;

    @NotNull(message = "status é obrigatório")
    private StatusPagamento status;

    private String codigoAutorizacao;
    private String motivoRecusa;
}
