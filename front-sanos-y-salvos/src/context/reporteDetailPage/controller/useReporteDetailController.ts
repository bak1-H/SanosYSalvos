import { useState, useEffect } from 'react'
import { useParams } from 'react-router'
import type { Reporte } from '../../dashboardPage/model/reporteTypes.ts'
import { fetchReporteById } from '../model/reporteDetailApi.ts'

export function useReporteDetailController() {
  const { id } = useParams<{ id: string }>()
  const [reporte, setReporte] = useState<Reporte | null>(null)
  const [cargando, setCargando] = useState(true)
  const [noEncontrado, setNoEncontrado] = useState(false)

  useEffect(() => {
    if (!id) return
    fetchReporteById(id)
      .then(setReporte)
      .catch(() => setNoEncontrado(true))
      .finally(() => setCargando(false))
  }, [id])

  return { reporte, cargando, noEncontrado }
}
