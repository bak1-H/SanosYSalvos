package cl.sanosysalvos.reporte.service;

import cl.sanosysalvos.reporte.dto.ReporteRequestDTO;
import cl.sanosysalvos.reporte.dto.ReporteResponseDTO;
import cl.sanosysalvos.reporte.model.ReporteModel;
import cl.sanosysalvos.reporte.repository.ReporteRepository;
import cl.sanosysalvos.reporte.messaging.ReportePublisher;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final double RADIO_CERCANIA_KM = 1.0;

    private final ReporteRepository reporteRepository;
    private final ReportePublisher reportePublisher;

    public ReporteServiceImpl(
        ReporteRepository reporteRepository,
        ReportePublisher reportePublisher) {
        this.reporteRepository = reporteRepository;
        this.reportePublisher = reportePublisher;
    }

    @Override
    @Transactional
    public ReporteResponseDTO guardarReporte(ReporteRequestDTO dto) {
        ReporteModel reporte = mapToEntity(dto);

        ReporteModel guardado = reporteRepository.save(reporte);

        ReporteResponseDTO responseDTO = mapToResponseDTO(guardado);

        reportePublisher.publicarNuevoReporte(responseDTO);

        return responseDTO;
    }

    @Override
    public List<ReporteResponseDTO> obtenerTodos() {
        return reporteRepository.findAll()
            .stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ReporteResponseDTO> obtenerCercanos(Double latitud, Double longitud) {
        return reporteRepository.findAll()
            .stream()
            .filter(reporte -> estaDentroDelRadio(reporte.getCoordenadas(), latitud, longitud))
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    private boolean estaDentroDelRadio(String coordenadas, Double latitud, Double longitud) {
        Double distanciaKm = calcularDistanciaKm(coordenadas, latitud, longitud);
        return distanciaKm != null && distanciaKm <= RADIO_CERCANIA_KM;
    }

    private Double calcularDistanciaKm(String coordenadas, Double latitudOrigen, Double longitudOrigen) {
        if (coordenadas == null || coordenadas.isBlank() || latitudOrigen == null || longitudOrigen == null) {
            return null;
        }
        try {
            String[] partes = coordenadas.split(",");
            double lat1 = Double.parseDouble(partes[0].trim());
            double lon1 = Double.parseDouble(partes[1].trim());

            final int radioTierraKm = 6371;
            double dLat = Math.toRadians(latitudOrigen - lat1);
            double dLon = Math.toRadians(longitudOrigen - lon1);
            double sinDLat = Math.sin(dLat / 2);
            double sinDLon = Math.sin(dLon / 2);
            double a = sinDLat * sinDLat +
                    Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(latitudOrigen)) *
                            sinDLon * sinDLon;
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return radioTierraKm * c;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ReporteResponseDTO obtenerPorId(Long id) {
        ReporteModel reporte = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Reporte no encontrado con ID: " + id));
        return mapToResponseDTO(reporte);
    }

    @Override
    @Transactional
    public ReporteResponseDTO actualizarReporte(Long id, ReporteRequestDTO dto) {
        ReporteModel reporteExistente = reporteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede actualizar. Reporte no encontrado con ID: " + id));

        aplicarCamposEditables(reporteExistente, dto);

        ReporteModel reporteActualizado = reporteRepository.save(reporteExistente);
        return mapToResponseDTO(reporteActualizado);
    }

    @Override
    @Transactional
    public void eliminarReporte(Long id) {
        if (!reporteRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. Reporte no encontrado con ID: " + id);
        }
        reporteRepository.deleteById(id);
    }

    private ReporteModel mapToEntity(ReporteRequestDTO dto) {
        ReporteModel reporte = new ReporteModel();
        aplicarCamposEditables(reporte, dto);
        return reporte;
    }

    private void aplicarCamposEditables(ReporteModel reporte, ReporteRequestDTO dto) {
        reporte.setIdUsuario(dto.getIdUsuario());
        reporte.setTipoReporte(dto.getTipoReporte());
        reporte.setTipoMascota(dto.getTipoMascota());
        reporte.setNombreMascota(dto.getNombreMascota());
        reporte.setColor(dto.getColor());
        reporte.setTamano(dto.getTamano());
        reporte.setRaza(dto.getRaza());
        reporte.setFotoMascota(dto.getFotoMascota());
        reporte.setDescripcion(dto.getDescripcion());
        reporte.setDireccion(dto.getDireccion());
        reporte.setCoordenadas(dto.getCoordenadas());
        reporte.setSexo(dto.getSexo());
    }

    private ReporteResponseDTO mapToResponseDTO(ReporteModel model) {
        return ReporteResponseDTO.builder()
            .id(model.getIdReporte())
            .idUsuario(model.getIdUsuario())
            .tipoReporte(model.getTipoReporte())
            .tipoMascota(model.getTipoMascota())
            .nombreMascota(model.getNombreMascota())
            .color(model.getColor())
            .tamano(model.getTamano())
            .raza(model.getRaza())
            .fotoMascota(model.getFotoMascota())
            .descripcion(model.getDescripcion())
            .direccion(model.getDireccion())
            .coordenadas(model.getCoordenadas())
            .sexo(model.getSexo())
            .estado(model.getEstado())
            .build();
    }
}