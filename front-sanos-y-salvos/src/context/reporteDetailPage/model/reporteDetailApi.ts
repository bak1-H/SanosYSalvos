import { httpClient } from '../../../services/httpClient.ts'
import type { Reporte } from '../../dashboardPage/model/reporteTypes.ts'

export async function fetchReporteById(id: string): Promise<Reporte> {
  const { data } = await httpClient.get<Reporte>(`/reportes/${id}`)
  return data
}
