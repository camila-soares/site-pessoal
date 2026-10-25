package com.camila.notification.repository;

import com.camila.notification.model.Notificacao;
import com.camila.notification.model.StatusNotificacao;
import com.camila.notification.model.TipoNotificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    List<Notificacao> findByDestinatario(String destinatario);
    List<Notificacao> findByReferencia(String referencia);
    List<Notificacao> findByStatus(StatusNotificacao status);
    List<Notificacao> findByTipo(TipoNotificacao tipo);
}
