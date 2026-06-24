import axios from 'axios'
import { tokenManager } from './tokenManager.ts'

const BASE_URL = import.meta.env.VITE_API_URL ?? '/bff/v1'

export const httpClient = axios.create({
  baseURL: BASE_URL,
  headers: { 'Content-Type': 'application/json' },
})

httpClient.interceptors.request.use((config) => {
  const token = tokenManager.get()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

httpClient.interceptors.response.use(
  (res) => res,
  (error) => {
    const isLoginRequest = error.config?.url?.includes('/api/login')
    if (error.response?.status === 401 && !isLoginRequest) {
      tokenManager.clear()
      window.location.href = '/'
    }
    return Promise.reject(error)
  }
)
