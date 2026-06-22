package com.sanosysalvos.notification.strategy;

import com.sanosysalvos.notification.model.Notificacion;
import com.sanosysalvos.notification.service.WebSocketNotificationService;
import com.sanosysalvos.notification.service.strategy.CanalNotificacion;
import com.sanosysalvos.notification.service.strategy.EmailCanalStrategy;
import com.sanosysalvos.notification.service.strategy.PushCanalStrategy;
import com.sanosysalvos.notification.service.strategy.WebSocketCanalStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Pruebas de cada estrategia de canal (patrón Strategy): verifican el canal que
 * representan y que la entrega se ejecuta sin lanzar excepciones.
 */
@ExtendWith(MockitoExtension.class)
class CanalStrategiesTest {

    private Notificacion notificacion() {
        Notificacion n = new Notificacion();
        n.setId_usuario_reporte_perdida("5");
        n.setEmail_usuario("dueno@example.com");
        n.setDescripcion("Coincidencia para Luna");
        return n;
    }

    @Test
    @DisplayName("EmailCanalStrategy reporta el canal EMAIL y entrega sin error")
    void emailStrategy() {
        EmailCanalStrategy strategy = new EmailCanalStrategy();
        assertThat(strategy.getCanal()).isEqualTo(CanalNotificacion.EMAIL);
        assertThatCode(() -> strategy.enviar(notificacion())).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("PushCanalStrategy reporta el canal PUSH y entrega sin error")
    void pushStrategy() {
        PushCanalStrategy strategy = new PushCanalStrategy();
        assertThat(strategy.getCanal()).isEqualTo(CanalNotificacion.PUSH);
        assertThatCode(() -> strategy.enviar(notificacion())).doesNotThrowAnyException();
    }

    @Mock
    private WebSocketNotificationService webSocketService;

    @InjectMocks
    private WebSocketCanalStrategy webSocketStrategy;

    @Test
    @DisplayName("WebSocketCanalStrategy reporta el canal WEBSOCKET y delega en el servicio")
    void webSocketStrategy() {
        Notificacion n = notificacion();

        assertThat(webSocketStrategy.getCanal()).isEqualTo(CanalNotificacion.WEBSOCKET);

        webSocketStrategy.enviar(n);
        verify(webSocketService, times(1)).notificarUsuario("5", n);
    }
}
