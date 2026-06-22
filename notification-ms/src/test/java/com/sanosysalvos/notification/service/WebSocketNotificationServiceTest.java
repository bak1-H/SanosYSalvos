package com.sanosysalvos.notification.service;

import com.sanosysalvos.notification.model.Notificacion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Pruebas del envío por WebSocket. Se simula {@link SimpMessagingTemplate} para
 * verificar el destino y el contenido del mensaje sin abrir un canal real.
 */
@ExtendWith(MockitoExtension.class)
class WebSocketNotificationServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private WebSocketNotificationService webSocketService;

    private Notificacion notificacion() {
        Notificacion n = new Notificacion();
        n.setId_usuario_reporte_perdida("5");
        n.setId_coincidencia(1L);
        n.setNombre_mascota("Luna");
        n.setDescripcion("Coincidencia para Luna");
        n.setDireccion("Calle 123");
        n.setEmail_usuario("dueno@example.com");
        return n;
    }

    @Test
    @DisplayName("notificarUsuario envía al topic del usuario con el contenido esperado")
    void notificarUsuarioEnviaAlDestinoCorrecto() {
        webSocketService.notificarUsuario("5", notificacion());

        ArgumentCaptor<String> destino = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> payload = ArgumentCaptor.forClass(Object.class);
        verify(messagingTemplate, times(1)).convertAndSend(destino.capture(), payload.capture());

        assertThat(destino.getValue()).isEqualTo("/topic/notifications/5");

        @SuppressWarnings("unchecked")
        Map<String, Object> mensaje = (Map<String, Object>) payload.getValue();
        assertThat(mensaje)
                .containsEntry("nombre_mascota", "Luna")
                .containsEntry("id_usuario_reporte_perdida", "5")
                .containsKey("titulo");
    }

    @Test
    @DisplayName("notificarUsuario captura la excepción del template y no la propaga")
    void notificarUsuarioCapturaExcepciones() {
        doThrow(new RuntimeException("fallo de transporte"))
                .when(messagingTemplate).convertAndSend(any(String.class), any(Object.class));

        assertThatCode(() -> webSocketService.notificarUsuario("5", notificacion()))
                .doesNotThrowAnyException();

        verify(messagingTemplate, times(1)).convertAndSend(eq("/topic/notifications/5"), any(Object.class));
    }
}
