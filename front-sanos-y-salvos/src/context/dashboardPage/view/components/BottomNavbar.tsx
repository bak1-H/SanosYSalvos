interface BottomNavbarProps {
  notificaciones: number;
  activeTab: 'mapa' | 'notificaciones' | 'perfil';
  onTabChange: (tab: 'mapa' | 'notificaciones' | 'perfil') => void;
  isAdmin?: boolean;
  onAdminClick?: () => void;
}

export function BottomNavbar({ notificaciones, activeTab, onTabChange, isAdmin, onAdminClick }: BottomNavbarProps) {
  return (
    <nav className="fixed bottom-0 left-0 right-0 z-50 h-16 bg-white border-t border-slate-100 shadow-[0_-2px_12px_rgba(0,0,0,0.07)] flex items-center">
      <div className="w-full flex justify-around items-center px-2">

        <button
          onClick={() => onTabChange('perfil')}
          className={`flex flex-col items-center gap-1 px-4 py-1.5 rounded-xl transition-colors ${
            activeTab === 'perfil' ? 'text-[#0f52ba]' : 'text-slate-400'
          }`}
        >
          <svg viewBox="0 0 24 24" className="w-6 h-6 fill-current">
            <path d="M12 12q-1.65 0-2.825-1.175Q8 9.65 8 8q0-1.65 1.175-2.825Q10.35 4 12 4q1.65 0 2.825 1.175Q16 6.35 16 8q0 1.65-1.175 2.825Q13.65 12 12 12zm-7 8v-1q0-1.25.638-2.288Q6.275 15.675 7.4 15.1q1.3-.65 2.638-0.975Q11.375 13.8 12 13.8t1.963.325Q15.3 14.45 16.6 15.1q1.125.575 1.763 1.612Q19 17.75 19 19v1H5z" />
          </svg>
          <span className="text-[10px] font-medium font-manrope">Perfil</span>
        </button>

        <button
          onClick={() => onTabChange('mapa')}
          className={`flex flex-col items-center gap-1 px-6 py-2 rounded-2xl transition-all ${
            activeTab === 'mapa'
              ? 'bg-[#0f52ba] text-white shadow-lg shadow-blue-200 scale-105'
              : 'text-slate-400'
          }`}
        >
          <svg viewBox="0 0 24 24" className="w-6 h-6 fill-current">
            <path d="M12 12q.825 0 1.413-.588Q14 10.825 14 10t-.587-1.413Q12.825 8 12 8q-.825 0-1.412.587Q10 9.175 10 10q0 .825.588 1.412Q11.175 12 12 12zm0 7.35q-3.65-3.15-5.325-5.838Q5 10.825 5 9q0-2.9 1.862-4.65Q8.725 2.6 12 2.6q3.275 0 5.138 1.75Q19 6.1 19 9q0 1.825-1.675 4.512Q15.65 16.2 12 19.35z" />
          </svg>
          <span className="text-[10px] font-medium font-manrope">Reportes</span>
        </button>

        <button
          onClick={() => onTabChange('notificaciones')}
          className={`relative flex flex-col items-center gap-1 px-4 py-1.5 rounded-xl transition-colors ${
            activeTab === 'notificaciones' ? 'text-[#0f52ba]' : 'text-slate-400'
          }`}
        >
          <div className="relative">
            <svg viewBox="0 0 24 24" className="w-6 h-6 fill-current">
              <path d="M5 19v-2h2v-7q0-2.075 1.25-3.688Q9.5 4.7 11.5 4.175V3.5q0-.625.438-1.062Q12.375 2 13 2t1.063.438Q14.5 2.875 14.5 3.5v.675q2 .525 3.25 2.137Q19 7.925 19 10v7h2v2H5zm7 3q-.825 0-1.413-.587Q10 20.825 10 20h4q0 .825-.588 1.413Q12.825 22 12 22z" />
            </svg>
            {notificaciones > 0 && (
              <span className="absolute -top-1.5 -right-1.5 min-w-[16px] h-4 bg-red-500 text-white text-[9px] font-bold rounded-full flex items-center justify-center px-1 leading-none">
                {notificaciones > 9 ? '9+' : notificaciones}
              </span>
            )}
          </div>
          <span className="text-[10px] font-medium font-manrope">Alertas</span>
        </button>

        {isAdmin && (
          <button
            onClick={onAdminClick}
            className="flex flex-col items-center gap-1 px-4 py-1.5 rounded-xl text-slate-400 transition-colors"
          >
            <svg viewBox="0 0 24 24" className="w-6 h-6 fill-current">
              <path d="M12 1 3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 4c1.86 0 3.41 1.28 3.86 3H8.14C8.59 6.28 10.14 5 12 5zm0 13c-2.67 0-5-1.43-6.28-3.55.62-.88 3.28-1.95 6.28-1.95s5.66 1.07 6.28 1.95C17 17.57 14.67 19 12 19zm0-6c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2z" />
            </svg>
            <span className="text-[10px] font-medium font-manrope">Admin</span>
          </button>
        )}

      </div>
    </nav>
  );
}
