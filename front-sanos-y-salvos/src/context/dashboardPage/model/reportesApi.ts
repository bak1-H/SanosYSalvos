import { httpClient } from '../../../services/httpClient.ts'
import type { Reporte } from './reporteTypes.ts'

export async function fetchReportesCercanos(latitud: number, longitud: number): Promise<Reporte[]> {
  const { data } = await httpClient.post<Reporte[]>('/reportes', { latitud, longitud })
  return data
}
