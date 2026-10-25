package com.camila.notification.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

import jakarta.mail.internet.MimeMessage;
import java.io.InputStream;

/**
 * Em dev-mode (sem SMTP real), substitui o JavaMailSender por
 * uma implementação que apenas loga — evita erros de conexão.
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "notification.dev-mode", havingValue = "true", matchIfMissing = true)
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSender() {
            @Override public MimeMessage createMimeMessage() { return new JavaMailSenderImpl().createMimeMessage(); }
            @Override public MimeMessage createMimeMessage(InputStream contentStream) { return createMimeMessage(); }
            @Override public void send(MimeMessage mimeMessage) { log.info("[DEV] MimeMessage suprimido"); }
            @Override public void send(MimeMessage... mimeMessages) {}
            @Override public void send(MimeMessagePreparator mimeMessagePreparator) {}
            @Override public void send(MimeMessagePreparator... mimeMessagePreparators) {}
            @Override public void send(SimpleMailMessage simpleMessage) {
                log.info("[DEV] SimpleMailMessage para={} assunto={}", simpleMessage.getTo(), simpleMessage.getSubject());
            }
            @Override public void send(SimpleMailMessage... simpleMessages) {}
        };
    }
}
