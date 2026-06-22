package cl.sys.bff.bfffsanys.controller;

import cl.sys.bff.bfffsanys.model.ReporteItemDTO;
import cl.sys.bff.bfffsanys.model.ReporteRequestDTO;
import cl.sys.bff.bfffsanys.model.ReporteResponseDTO;
import cl.sys.bff.bfffsanys.model.ReportesNearbyRequestDTO;
import cl.sys.bff.bfffsanys.service.ReportesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ReportesControllerTest {

    @Mock
    private ReportesService reportesService;

    private AutoCloseable mocks;
    private ReportesController controller;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        controller = new ReportesController(reportesService);
    }

    @Test
    void crear_retorna201ConElCuerpoDelServicio() {
        ReporteRequestDTO request = new ReporteRequestDTO();
        ReporteResponseDTO expected = new ReporteResponseDTO();
        expected.setId(1L);
        when(reportesService.crear(request)).thenReturn(expected);

        ResponseEntity<ReporteResponseDTO> response = controller.crear(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }

    @Test
    void obtenerCercanos_retorna200ConLaListaDelServicio() {
        ReportesNearbyRequestDTO request = new ReportesNearbyRequestDTO();
        ReporteItemDTO item = new ReporteItemDTO();
        when(reportesService.obtenerCercanos(request)).thenReturn(List.of(item));

        ResponseEntity<List<ReporteItemDTO>> response = controller.obtenerCercanos(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void obtenerPorId_retorna200ConElCuerpoDelServicio() {
        ReporteItemDTO expected = new ReporteItemDTO();
        when(reportesService.obtenerPorId(7L)).thenReturn(expected);

        ResponseEntity<ReporteItemDTO> response = controller.obtenerPorId(7L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
    }
}
