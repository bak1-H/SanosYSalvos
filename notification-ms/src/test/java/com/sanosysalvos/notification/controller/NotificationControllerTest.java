package com.sanosysalvos.notification.controller;

import com.sanosysalvos.notification.dto.CoincidenciaEventDTO;
import com.sanosysalvos.notification.model.Notificacion;
import com.sanosysalvos.notification.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Pruebas del controlador REST. Se simula el {@link NotificationService} con
 * Mockito para verificar que cada endpoint delega correctamente y devuelve lo esperado.
 */
@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService service;

    @InjectMocks
    private NotificationController controller;

    @Test
    @DisplayName("POST /match procesa el evento y confirma la recepción")
    void enviarProcesaEventoYConfirma() {
        CoincidenciaEventDTO evento = new CoincidenciaEventDTO();
        evento.setId_coincidencia(1L);

        String respuesta = controller.enviar(evento);

        verify(service, times(1)).procesarNotificacion(evento);
        assertThat(respuesta).isEqualTo("Evento recibido correctamente");
    }

    @Test
    @DisplayName("GET / devuelve todas las notificaciones del servicio")
    void obtenerTodasDelegaEnElServicio() {
        Notificacion n = new Notificacion();
        when(service.obtenerTodas()).thenReturn(List.of(n));

        List<Notificacion> resultado = controller.obtenerTodas();

        verify(service, times(1)).obtenerTodas();
        assertThat(resultado).containsExactly(n);
    }

    @Test
    @DisplayName("GET /{id} devuelve la notificación correspondiente")
    void obtenerPorIdDelegaEnElServicio() {
        Notificacion n = new Notificacion();
        n.setId_notificacion(7L);
        when(service.obtenerPorId(7L)).thenReturn(n);

        Notificacion resultado = controller.obtenerPorId(7L);

        verify(service, times(1)).obtenerPorId(7L);
        assertThat(resultado.getId_notificacion()).isEqualTo(7L);
    }
}
