import { httpClient } from '../../../services/httpClient.ts';
import type { ReporteAdmin } from './reporteAdminTypes.ts';

export async function fetchAllReportes(): Promise<ReporteAdmin[]> {
  const { data } = await httpClient.get<ReporteAdmin[]>('/reportes');
  return data;
}

export async function deleteReporte(id: number): Promise<void> {
  await httpClient.delete(`/reportes/${id}`);
}
