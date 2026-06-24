import { useEffect, useState } from 'react';
import { deleteReporte, fetchAllReportes } from '../model/adminReportesApi.ts';
import type { ReporteAdmin } from '../model/reporteAdminTypes.ts';

export function useAdminReportesController() {
  const [reportes, setReportes] = useState<ReporteAdmin[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deletingId, setDeletingId] = useState<number | null>(null);

  useEffect(() => {
    let activo = true;

    fetchAllReportes()
      .then((data) => {
        if (activo) setReportes(data);
      })
      .catch(() => {
        if (activo) setError('No se pudieron cargar los reportes.');
      })
      .finally(() => {
        if (activo) setLoading(false);
      });

    return () => {
      activo = false;
    };
  }, []);

  async function handleDelete(id: number) {
    setDeletingId(id);
    setError(null);
    try {
      await deleteReporte(id);
      setReportes((prev) => prev.filter((reporte) => reporte.id !== id));
    } catch {
      setError('No se pudo eliminar el reporte. Intenta nuevamente.');
    } finally {
      setDeletingId(null);
    }
  }

  return { reportes, loading, error, deletingId, handleDelete };
}
