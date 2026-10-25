package com.camila.payment.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(name = "cliente_email", nullable = false)
    private String clienteEmail;

    @Column(name = "cliente_nome", nullable = false)
    private String clienteNome;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private FormaPagamento formaPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private StatusPagamento status = StatusPagamento.PENDENTE;

    /** ID gerado pelo gateway externo (PIX txid, boleto número, etc.) */
    @Column(name = "gateway_id")
    private String gatewayId;

    /** Código de autorização retornado pelo gateway */
    @Column(name = "codigo_autorizacao")
    private String codigoAutorizacao;

    /** Mensagem de erro do gateway em caso de recusa */
    @Column(name = "motivo_recusa")
    private String motivoRecusa;

    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @PrePersist
    protected void onCreate() {
        criadoEm = LocalDateTime.now();
        atualizadoEm = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
