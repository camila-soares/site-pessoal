package com.camila.notification.dto;

import com.camila.notification.model.Notificacao;
import com.camila.notification.model.StatusNotificacao;
import com.camila.notification.model.TipoNotificacao;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacaoResponse {
    private Long id;
    private String destinatario;
    private String nomeDestinatario;
    private TipoNotificacao tipo;
    private String assunto;
    private String mensagem;
    private String referencia;
    private StatusNotificacao status;
    private Integer tentativas;
    private LocalDateTime enviadaEm;
    private LocalDateTime criadaEm;

    public static NotificacaoResponse from(Notificacao n) {
        NotificacaoResponse dto = new NotificacaoResponse();
        dto.id = n.getId();
        dto.destinatario = n.getDestinatario();
        dto.nomeDestinatario = n.getNomeDestinatario();
        dto.tipo = n.getTipo();
        dto.assunto = n.getAssunto();
        dto.mensagem = n.getMensagem();
        dto.referencia = n.getReferencia();
        dto.status = n.getStatus();
        dto.tentativas = n.getTentativas();
        dto.enviadaEm = n.getEnviadaEm();
        dto.criadaEm = n.getCriadaEm();
        return dto;
    }
}
