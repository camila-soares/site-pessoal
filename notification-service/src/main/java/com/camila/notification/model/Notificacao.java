package com.camila.notification.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinatario;

    @Column(name = "nome_destinatario")
    private String nomeDestinatario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TipoNotificacao tipo = TipoNotificacao.EMAIL;

    @Column(nullable = false)
    private String assunto;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensagem;

    /** Identificador da entidade de origem (ex: PAGAMENTO-42). */
    private String referencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusNotificacao status = StatusNotificacao.PENDENTE;

    @Column(name = "erro_detalhe", columnDefinition = "TEXT")
    private String erroDetalhe;

    @Column(name = "tentativas")
    @Builder.Default
    private Integer tentativas = 0;

    @Column(name = "enviada_em")
    private LocalDateTime enviadaEm;

    @Column(name = "criada_em", updatable = false)
    private LocalDateTime criadaEm;

    @PrePersist
    protected void onCreate() {
        criadaEm = LocalDateTime.now();
    }
}
