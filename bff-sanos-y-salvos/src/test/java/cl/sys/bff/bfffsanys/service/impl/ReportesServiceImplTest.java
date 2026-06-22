package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.RegistroClient;
import cl.sys.bff.bfffsanys.client.ReportesClient;
import cl.sys.bff.bfffsanys.model.NombreUsuarioResponseDTO;
import cl.sys.bff.bfffsanys.model.ReporteItemDTO;
import cl.sys.bff.bfffsanys.model.ReporteRequestDTO;
import cl.sys.bff.bfffsanys.model.ReporteResponseDTO;
import cl.sys.bff.bfffsanys.model.ReportesNearbyRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReportesServiceImplTest {

    @Mock
    private ReportesClient reportesClient;

    @Mock
    private RegistroClient registroClient;

    private AutoCloseable mocks;
    private ReportesServiceImpl reportesService;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        reportesService = new ReportesServiceImpl(reportesClient, registroClient);
    }

    @Test
    void obtenerCercanos_resuelveNombreUnaSolaVezPorUsuarioRepetido() {
        ReporteItemDTO uno = itemDe("user-1");
        ReporteItemDTO dos = itemDe("user-1");
        ReporteItemDTO tres = itemDe("user-2");
        when(reportesClient.obtenerCercanos(any(ReportesNearbyRequestDTO.class)))
                .thenReturn(List.of(uno, dos, tres));
        when(registroClient.getNombre("user-1")).thenReturn(nombre("Daniel Beltran"));
        when(registroClient.getNombre("user-2")).thenReturn(nombre("Clinica Sur"));

        List<ReporteItemDTO> resultado = reportesService.obtenerCercanos(new ReportesNearbyRequestDTO());

        assertEquals("Daniel Beltran", resultado.get(0).getNombreReportante());
        assertEquals("Daniel Beltran", resultado.get(1).getNombreReportante());
        assertEquals("Clinica Sur", resultado.get(2).getNombreReportante());
        verify(registroClient, times(1)).getNombre("user-1");
        verify(registroClient, times(1)).getNombre("user-2");
    }

    @Test
    void obtenerCercanos_siFallaResolucionUsaFallback() {
        ReporteItemDTO uno = itemDe("user-1");
        when(reportesClient.obtenerCercanos(any(ReportesNearbyRequestDTO.class))).thenReturn(List.of(uno));
        when(registroClient.getNombre("user-1")).thenThrow(new RuntimeException("timeout"));

        List<ReporteItemDTO> resultado = reportesService.obtenerCercanos(new ReportesNearbyRequestDTO());

        assertEquals("Usuario desconocido", resultado.get(0).getNombreReportante());
    }

    @Test
    void obtenerCercanos_sinIdUsuarioNoConsultaRegistro() {
        ReporteItemDTO sinUsuario = itemDe(null);
        when(reportesClient.obtenerCercanos(any(ReportesNearbyRequestDTO.class))).thenReturn(List.of(sinUsuario));

        List<ReporteItemDTO> resultado = reportesService.obtenerCercanos(new ReportesNearbyRequestDTO());

        assertEquals(null, resultado.get(0).getNombreReportante());
        verify(registroClient, never()).getNombre(any());
    }

    @Test
    void obtenerPorId_enriqueceElReporteUnico() {
        ReporteItemDTO reporte = itemDe("user-3");
        when(reportesClient.obtenerPorId(7L)).thenReturn(reporte);
        when(registroClient.getNombre("user-3")).thenReturn(nombre("Refugio Patitas"));

        ReporteItemDTO resultado = reportesService.obtenerPorId(7L);

        assertEquals("Refugio Patitas", resultado.getNombreReportante());
    }

    @Test
    void crear_delegaEnElCliente() {
        ReporteRequestDTO request = new ReporteRequestDTO();
        ReporteResponseDTO expected = new ReporteResponseDTO();
        expected.setId(1L);
        when(reportesClient.crear(request)).thenReturn(expected);

        ReporteResponseDTO response = reportesService.crear(request);

        assertEquals(expected, response);
    }

    private ReporteItemDTO itemDe(String idUsuario) {
        ReporteItemDTO item = new ReporteItemDTO();
        item.setIdUsuario(idUsuario);
        return item;
    }

    private NombreUsuarioResponseDTO nombre(String nombre) {
        NombreUsuarioResponseDTO dto = new NombreUsuarioResponseDTO();
        dto.setNombre(nombre);
        return dto;
    }
}
