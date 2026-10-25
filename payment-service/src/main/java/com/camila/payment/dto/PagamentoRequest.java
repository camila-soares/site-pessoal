package com.camila.payment.dto;

import com.camila.payment.model.FormaPagamento;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PagamentoRequest {

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @Email @NotBlank(message = "E-mail do cliente é obrigatório")
    private String clienteEmail;

    @NotBlank(message = "Nome do cliente é obrigatório")
    private String clienteNome;

    @NotNull @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    @NotNull(message = "Forma de pagamento é obrigatória")
    private FormaPagamento formaPagamento;
}
