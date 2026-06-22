package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.NotificacionesClient;
import cl.sys.bff.bfffsanys.model.NotificacionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class NotificacionesServiceImplTest {

    @Mock
    private NotificacionesClient notificacionesClient;

    private AutoCloseable mocks;
    private NotificacionesServiceImpl notificacionesService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        notificacionesService = new NotificacionesServiceImpl(notificacionesClient);
    }

    @Test
    void obtenerPorId_delegaEnElCliente() {
        NotificacionResponseDTO expected = new NotificacionResponseDTO();
        expected.setNombreMascota("Firulais");
        when(notificacionesClient.obtenerPorId(5L)).thenReturn(expected);

        NotificacionResponseDTO response = notificacionesService.obtenerPorId(5L);

        assertEquals("Firulais", response.getNombreMascota());
    }

    @Test
    void obtenerPorUsuario_delegaEnElCliente() {
        NotificacionResponseDTO item = new NotificacionResponseDTO();
        item.setNombreMascota("Michi");
        when(notificacionesClient.obtenerPorUsuario("user-1")).thenReturn(List.of(item));

        List<NotificacionResponseDTO> response = notificacionesService.obtenerPorUsuario("user-1");

        assertEquals(1, response.size());
        assertEquals("Michi", response.get(0).getNombreMascota());
    }
}
