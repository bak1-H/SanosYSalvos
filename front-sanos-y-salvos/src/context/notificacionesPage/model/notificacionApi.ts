import { httpClient } from '../../../services/httpClient.ts'
import type { NotificacionMatchDTO } from './notificacionTypes.ts'

export async function fetchNotificacionesPendientes(uuid: string): Promise<NotificacionMatchDTO[]> {
  try {
    const { data } = await httpClient.get<NotificacionMatchDTO[]>(`/api/notifications/usuario/${uuid}`)
    return Array.isArray(data) ? data : []
  } catch {
    return []
  }
}
