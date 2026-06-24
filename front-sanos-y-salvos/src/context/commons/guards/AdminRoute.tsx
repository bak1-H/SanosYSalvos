import { Navigate, Outlet } from 'react-router'
import { tokenManager } from '../../../services/tokenManager.ts'

export function AdminRoute() {
  if (!tokenManager.isValid()) {
    return <Navigate to="/unauthorized" replace />
  }

  const payload = tokenManager.decode()
  const role = payload?.role

  return role === 'ADMIN' ? <Outlet /> : <Navigate to="/unauthorized" replace />
}
