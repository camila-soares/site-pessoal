package com.camila.notification.service;

import com.camila.notification.dto.NotificacaoRequest;
import com.camila.notification.dto.NotificacaoResponse;
import com.camila.notification.exception.RecursoNaoEncontradoException;
import com.camila.notification.model.Notificacao;
import com.camila.notification.model.StatusNotificacao;
import com.camila.notification.model.TipoNotificacao;
import com.camila.notification.repository.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;
    private final EmailService emailService;

    @Value("${notification.dev-mode:true}")
    private boolean devMode;

    @Transactional
    public NotificacaoResponse enviar(NotificacaoRequest request) {
        Notificacao notificacao = Notificacao.builder()
                .destinatario(request.getDestinatario())
                .nomeDestinatario(request.getNomeDestinatario())
                .tipo(request.getTipo())
                .assunto(request.getAssunto())
                .mensagem(request.getMensagem())
                .referencia(request.getReferencia())
                .build();

        notificacao = notificacaoRepository.save(notificacao);

        try {
            despachar(notificacao);
            notificacao.setStatus(devMode ? StatusNotificacao.SIMULADA : StatusNotificacao.ENVIADA);
            notificacao.setEnviadaEm(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Falha ao enviar notificação id={}: {}", notificacao.getId(), e.getMessage());
            notificacao.setStatus(StatusNotificacao.FALHA);
            notificacao.setErroDetalhe(e.getMessage());
        }

        notificacao.setTentativas(notificacao.getTentativas() + 1);
        return NotificacaoResponse.from(notificacaoRepository.save(notificacao));
    }

    @Transactional(readOnly = true)
    public NotificacaoResponse buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .map(NotificacaoResponse::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificação não encontrada: " + id));
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listarPorDestinatario(String destinatario) {
        return notificacaoRepository.findByDestinatario(destinatario).stream()
                .map(NotificacaoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<NotificacaoResponse> listarPorReferencia(String referencia) {
        return notificacaoRepository.findByReferencia(referencia).stream()
                .map(NotificacaoResponse::from).toList();
    }

    @Transactional
    public NotificacaoResponse reenviar(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Notificação não encontrada: " + id));

        try {
            despachar(notificacao);
            notificacao.setStatus(devMode ? StatusNotificacao.SIMULADA : StatusNotificacao.ENVIADA);
            notificacao.setEnviadaEm(LocalDateTime.now());
            notificacao.setErroDetalhe(null);
        } catch (Exception e) {
            notificacao.setStatus(StatusNotificacao.FALHA);
            notificacao.setErroDetalhe(e.getMessage());
        }

        notificacao.setTentativas(notificacao.getTentativas() + 1);
        return NotificacaoResponse.from(notificacaoRepository.save(notificacao));
    }

    private void despachar(Notificacao notificacao) {
        if (notificacao.getTipo() == TipoNotificacao.EMAIL) {
            emailService.enviar(
                    notificacao.getDestinatario(),
                    notificacao.getAssunto(),
                    notificacao.getMensagem()
            );
        } else {
            // SMS e PUSH: loga (plugue seu provider aqui)
            log.info("[{}] Para={} Assunto={}", notificacao.getTipo(),
                    notificacao.getDestinatario(), notificacao.getAssunto());
        }
    }
}
