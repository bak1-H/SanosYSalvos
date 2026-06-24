export interface ReporteAdmin {
  id: number;
  tipoReporte: 'PERDIDO' | 'VISTO';
  tipoMascota: string;
  nombreMascota: string;
  color: string;
  tamano: 'PEQUENO' | 'MEDIANO' | 'GRANDE';
  raza: string;
  fotoMascota: string;
  descripcion: string;
  direccion: string;
  coordenadas: string;
  sexo: 'MACHO' | 'HEMBRA';
  nombreReportante: string;
}
