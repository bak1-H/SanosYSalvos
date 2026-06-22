import { useState } from 'react'
import { useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate } from 'react-router'
import { loginSchema, type LoginForm } from '../model/loginSchema.ts'
import { loginUser } from '../model/loginApi.ts'
import { tokenManager } from '../../../services/tokenManager.ts'

export const useLoginController = () => {
  const navigate = useNavigate()
  const [serverError, setServerError] = useState<string | null>(null)

  const form = useForm<LoginForm>({
    resolver: zodResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  })

  const onSubmit = form.handleSubmit(async (data) => {
    setServerError(null)
    try {
      const res = await loginUser(data)
      tokenManager.set(res.accessToken)
      navigate('/dashboard')
    } catch {
      setServerError('Correo o contraseña incorrectos. Intenta de nuevo.')
    }
  })

  return {
    form,
    onSubmit,
    serverError,
    isSubmitting: form.formState.isSubmitting,
    goToRegister: () => navigate('/rol-selector'),
  }
}
