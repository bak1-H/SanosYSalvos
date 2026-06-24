import { useState } from 'react';
import { useAdminReportesController } from '../controller/useAdminReportesController.ts';

export function AdminReportesView() {
  const { reportes, loading, error, deletingId, handleDelete } = useAdminReportesController();
  const [confirmandoId, setConfirmandoId] = useState<number | null>(null);

  function onEliminarClick(id: number) {
    if (confirmandoId === id) {
      setConfirmandoId(null);
      handleDelete(id);
    } else {
      setConfirmandoId(id);
    }
  }

  return (
    <div className="min-h-screen bg-[#f5f7ff] px-4 py-8 sm:px-6 lg:px-10">
      <div className="mx-auto max-w-6xl">
        <h1 className="font-manrope text-2xl font-extrabold text-[#191c1e] mb-1">
          Administración de reportes
        </h1>
        <p className="font-inter text-sm text-[#505f76] mb-6">
          Lista completa de reportes registrados en la plataforma.
        </p>

        {error && (
          <div className="mb-4 rounded-lg border border-red-200 bg-red-50 px-4 py-3 font-inter text-sm text-red-700">
            {error}
          </div>
        )}

        {loading ? (
          <div className="flex flex-col items-center justify-center py-16">
            <div className="w-10 h-10 rounded-full border-4 border-[#0f52ba] border-t-transparent animate-spin mb-4" />
            <p className="font-inter text-sm text-[#505f76]">Cargando reportes...</p>
          </div>
        ) : reportes.length === 0 ? (
          <div className="rounded-xl border border-slate-200 bg-white px-6 py-12 text-center">
            <p className="font-inter text-sm text-[#505f76]">No hay reportes registrados.</p>
          </div>
        ) : (
          <div className="overflow-x-auto rounded-xl border border-slate-200 bg-white shadow-sm">
            <table className="w-full min-w-[720px] text-left">
              <thead>
                <tr className="border-b border-slate-200 bg-[#f0f4ff]">
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Foto
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Mascota
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Tipo
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Raza
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Reportante
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Dirección
                  </th>
                  <th className="px-4 py-3 font-manrope text-xs font-semibold uppercase tracking-wide text-[#505f76]">
                    Acción
                  </th>
                </tr>
              </thead>
              <tbody>
                {reportes.map((reporte) => (
                  <tr key={reporte.id} className="border-b border-slate-100 last:border-0">
                    <td className="px-4 py-3">
                      {reporte.fotoMascota ? (
                        <img
                          src={reporte.fotoMascota}
                          alt={reporte.nombreMascota}
                          className="h-12 w-12 rounded-lg object-cover"
                        />
                      ) : (
                        <div className="h-12 w-12 rounded-lg bg-[#f0f4ff]" />
                      )}
                    </td>
                    <td className="px-4 py-3 font-inter text-sm font-semibold text-[#191c1e]">
                      {reporte.nombreMascota}
                    </td>
                    <td className="px-4 py-3 font-inter text-sm text-[#505f76]">{reporte.tipoReporte}</td>
                    <td className="px-4 py-3 font-inter text-sm text-[#505f76]">{reporte.raza}</td>
                    <td className="px-4 py-3 font-inter text-sm text-[#505f76]">{reporte.nombreReportante}</td>
                    <td className="px-4 py-3 font-inter text-sm text-[#505f76]">{reporte.direccion}</td>
                    <td className="px-4 py-3">
                      {confirmandoId === reporte.id ? (
                        <div className="flex flex-col gap-2 sm:flex-row">
                          <button
                            type="button"
                            onClick={() => onEliminarClick(reporte.id)}
                            disabled={deletingId === reporte.id}
                            className="rounded-md bg-red-600 px-3 py-1.5 font-inter text-xs font-semibold text-white transition-colors hover:bg-red-700 disabled:opacity-60"
                          >
                            {deletingId === reporte.id ? 'Eliminando...' : '¿Confirmar?'}
                          </button>
                          <button
                            type="button"
                            onClick={() => setConfirmandoId(null)}
                            disabled={deletingId === reporte.id}
                            className="rounded-md border border-slate-200 px-3 py-1.5 font-inter text-xs font-semibold text-[#505f76] transition-colors hover:bg-slate-50 disabled:opacity-60"
                          >
                            Cancelar
                          </button>
                        </div>
                      ) : (
                        <button
                          type="button"
                          onClick={() => onEliminarClick(reporte.id)}
                          className="rounded-md border border-red-200 px-3 py-1.5 font-inter text-xs font-semibold text-red-600 transition-colors hover:bg-red-50"
                        >
                          Eliminar
                        </button>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
}
