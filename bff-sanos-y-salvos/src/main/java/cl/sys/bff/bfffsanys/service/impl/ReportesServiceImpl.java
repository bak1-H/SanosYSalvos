package cl.sys.bff.bfffsanys.service.impl;

import cl.sys.bff.bfffsanys.client.RegistroClient;
import cl.sys.bff.bfffsanys.client.ReportesClient;
import cl.sys.bff.bfffsanys.model.ReporteItemDTO;
import cl.sys.bff.bfffsanys.model.ReporteRequestDTO;
import cl.sys.bff.bfffsanys.model.ReporteResponseDTO;
import cl.sys.bff.bfffsanys.model.ReportesNearbyRequestDTO;
import cl.sys.bff.bfffsanys.service.ReportesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportesServiceImpl implements ReportesService {

    private final ReportesClient reportesClient;
    private final RegistroClient registroClient;

    @Override
    public ReporteResponseDTO crear(ReporteRequestDTO dto) {
        return reportesClient.crear(dto);
    }

    @Override
    public List<ReporteItemDTO> obtenerCercanos(ReportesNearbyRequestDTO dto) {
        List<ReporteItemDTO> reportes = reportesClient.obtenerCercanos(dto);
        enriquecerConNombreReportante(reportes);
        return reportes;
    }

    @Override
    public ReporteItemDTO obtenerPorId(Long id) {
        ReporteItemDTO reporte = reportesClient.obtenerPorId(id);
        enriquecerConNombreReportante(List.of(reporte));
        return reporte;
    }

    @Override
    public List<ReporteItemDTO> listarTodos() {
        List<ReporteItemDTO> reportes = reportesClient.listarTodos();
        enriquecerConNombreReportante(reportes);
        return reportes;
    }

    @Override
    public void eliminar(Long id) {
        reportesClient.eliminar(id);
    }

    private void enriquecerConNombreReportante(List<ReporteItemDTO> reportes) {
        Map<String, String> nombresPorUsuario = new HashMap<>();
        for (ReporteItemDTO reporte : reportes) {
            String idUsuario = reporte.getIdUsuario();
            if (idUsuario == null || nombresPorUsuario.containsKey(idUsuario)) {
                continue;
            }
            nombresPorUsuario.put(idUsuario, resolverNombre(idUsuario));
        }
        reportes.forEach(reporte -> reporte.setNombreReportante(nombresPorUsuario.get(reporte.getIdUsuario())));
    }

    private String resolverNombre(String idUsuario) {
        try {
            return registroClient.getNombre(idUsuario).getNombre();
        } catch (Exception e) {
            log.warn("No se pudo resolver el nombre del usuario {}: {}", idUsuario, e.getMessage());
            return "Usuario desconocido";
        }
    }
}
