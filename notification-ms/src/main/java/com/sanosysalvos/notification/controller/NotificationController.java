package com.sanosysalvos.notification.controller;

import com.sanosysalvos.notification.dto.CoincidenciaEventDTO;
import com.sanosysalvos.notification.model.Notificacion;
import com.sanosysalvos.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Recepción y consulta de notificaciones de coincidencias")
public class NotificationController {

    private final NotificationService service;

    @PostMapping("/match")
    @Operation(summary = "Procesar un evento de coincidencia",
            description = "Crea la notificación según el tipo de evento (Factory Method) y la entrega "
                    + "por todos los canales disponibles (Strategy). El campo 'tipo_evento' puede ser "
                    + "COINCIDENCIA_ENCONTRADA o MASCOTA_ENCONTRADA; si se omite, se usa el valor por defecto.")
    public String enviar(@RequestBody CoincidenciaEventDTO evento) {
        service.procesarNotificacion(evento);
        return "Evento recibido correctamente";
    }

    @GetMapping
    @Operation(summary = "Listar todas las notificaciones")
    public List<Notificacion> obtenerTodas() {
        return service.obtenerTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una notificación por su ID")
    public Notificacion obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    @Operation(summary = "Listar las notificaciones de un usuario",
            description = "Permite recuperar las notificaciones acumuladas mientras el usuario estaba "
                    + "desconectado del WebSocket.")
    public List<Notificacion> obtenerPorUsuario(@PathVariable String idUsuario) {
        return service.obtenerPorUsuario(idUsuario);
    }
}
