package com.camila.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${notification.from-email}")
    private String fromEmail;

    @Value("${notification.dev-mode:true}")
    private boolean devMode;

    public void enviar(String destinatario, String assunto, String mensagem) {
        if (devMode) {
            log.info("""
                    ══════════════════════════════════════════
                    [DEV] E-mail simulado (não enviado)
                    Para:     {}
                    Assunto:  {}
                    Mensagem:
                    {}
                    ══════════════════════════════════════════""",
                    destinatario, assunto, mensagem);
            return;
        }

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(fromEmail);
        mail.setTo(destinatario);
        mail.setSubject(assunto);
        mail.setText(mensagem);
        mailSender.send(mail);
        log.info("E-mail enviado para: {}", destinatario);
    }
}
