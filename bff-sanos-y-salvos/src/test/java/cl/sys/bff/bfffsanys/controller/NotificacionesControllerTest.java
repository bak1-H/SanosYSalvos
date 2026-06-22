package cl.sys.bff.bfffsanys.controller;

import cl.sys.bff.bfffsanys.model.NotificacionResponseDTO;
import cl.sys.bff.bfffsanys.service.NotificacionesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NotificacionesControllerTest {

    @Mock
    private NotificacionesService notificacionesService;

    private AutoCloseable mocks;
    private NotificacionesController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new NotificacionesController(notificacionesService);
    }

    @Test
    void obtenerPorId_retorna200ConElCuerpoDelServicio() {
        NotificacionResponseDTO expected = new NotificacionResponseDTO();
        expected.setNombreMascota("Firulais");
        when(notificacionesService.obtenerPorId(5L)).thenReturn(expected);

        ResponseEntity<NotificacionResponseDTO> response = controller.obtenerPorId(5L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Firulais", response.getBody().getNombreMascota());
    }

    @Test
    void obtenerPorUsuario_retorna200ConLaListaDelServicio() {
        NotificacionResponseDTO item = new NotificacionResponseDTO();
        item.setNombreMascota("Michi");
        when(notificacionesService.obtenerPorUsuario("user-1")).thenReturn(List.of(item));

        ResponseEntity<List<NotificacionResponseDTO>> response = controller.obtenerPorUsuario("user-1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
