package com.camila.notification.dto;

import com.camila.notification.model.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NotificacaoRequest {

    @NotBlank(message = "Destinatário é obrigatório")
    private String destinatario;

    private String nomeDestinatario;

    @NotNull(message = "Tipo é obrigatório")
    private TipoNotificacao tipo;

    @NotBlank(message = "Assunto é obrigatório")
    private String assunto;

    @NotBlank(message = "Mensagem é obrigatória")
    private String mensagem;

    private String referencia;
}
