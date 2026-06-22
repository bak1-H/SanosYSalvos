package cl.sys.bff.bfffsanys.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ReporteItemDTO {
    private Long id;
    private String tipoReporte;
    private String tipoMascota;
    private String nombreMascota;
    private String color;
    private String tamano;
    private String raza;
    private String fotoMascota;
    private String descripcion;
    private String direccion;
    private String coordenadas;
    private String sexo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String idUsuario;

    private String nombreReportante;
}
