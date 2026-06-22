---
name: jwt-frontend
description: >
  Guía completa para manejar JSON Web Tokens (JWT) en aplicaciones frontend.
  Úsala SIEMPRE que el usuario mencione JWT, tokens, autenticación, login, sesiones,
  refresh tokens, Authorization headers, Bearer tokens, decode de tokens, expiración
  de tokens, o cualquier flujo de auth en el frontend — aunque no diga explícitamente "JWT".
  También aplica cuando el usuario trabaja con interceptores de axios/fetch, rutas
  protegidas, guards de React/Vue/Angular, o almacenamiento seguro de credenciales.
---

# JWT Frontend Skill

## Principios fundamentales

1. **Preguntar antes de asumir** — Cada proyecto tiene contexto diferente. Antes de escribir código, hacer las preguntas de la sección "Elicitación".
2. **Reutilizar código** — Siempre proponer helpers/hooks/servicios reutilizables, no código inline disperso.
3. **Seguridad primero** — Nunca `localStorage` para tokens sensibles sin advertir. Preferir `httpOnly cookies` cuando el backend lo permita.

---

## Elicitación — Preguntar SIEMPRE lo que no se sepa

Antes de escribir código, identificar qué información falta y preguntar solo lo necesario:

### Credenciales / Endpoints (preguntar si no están en el contexto)
- ¿Cuál es el endpoint de login? (ej: `POST /api/auth/login`)
- ¿Cuál es el endpoint de refresh? (ej: `POST /api/auth/refresh`)
- ¿El token viene en el body (`{ access_token }`) o en un header/cookie?
- ¿Hay refresh token? ¿Viene junto al access token o separado?

### Stack técnico (preguntar si no es obvio)
- ¿Framework? (React / Vue / Angular / Vanilla)
- ¿Cliente HTTP? (axios / fetch / ky)
- ¿Hay estado global? (Redux / Zustand / Pinia / NgRx / Context)
- ¿TypeScript o JavaScript?

### Estrategia de almacenamiento (preguntar si no se mencionó)
- ¿El backend soporta `httpOnly cookies`? → Opción más segura
- ¿Solo frontend sin control del backend? → Usar memoria + fallback

**Regla**: No inventar URLs, nombres de campos ni estructura de respuesta. Si no está en el contexto → preguntar.

---

## Arquitectura recomendada

Siempre proponer esta estructura modular y reutilizable:

```
src/
├── services/
│   └── authService.ts       ← login, logout, refresh
├── utils/
│   └── tokenManager.ts      ← get/set/clear/decode token
├── hooks/ (React) o composables/ (Vue)
│   ├── useAuth.ts
│   └── useAuthGuard.ts
├── interceptors/
│   └── httpClient.ts        ← axios/fetch con Bearer automático
└── components/
    └── ProtectedRoute.tsx   ← rutas protegidas
```

Para Angular: usar `AuthService`, `AuthGuard`, `AuthInterceptor`.

---

## Módulos reutilizables — Ver referencias

Leer el archivo de referencia según el stack del usuario:

- **React + axios** → `references/react-axios.md`
- **React + fetch** → `references/react-fetch.md`
- **Vue 3** → `references/vue3.md`
- **Angular** → `references/angular.md`
- **Vanilla JS** → `references/vanilla.md`

Cada referencia incluye:
- `tokenManager` (almacenamiento y decode)
- Cliente HTTP con interceptor (Bearer automático + refresh silencioso)
- Hook/composable/servicio de auth
- Componente de ruta protegida
- Manejo de expiración

---

## Flujo estándar JWT

```
1. Login
   POST /auth/login → { access_token, refresh_token? }
   → guardar tokens → redirigir

2. Requests autenticados
   Cada request → header: Authorization: Bearer <access_token>
   → interceptor lo agrega automáticamente

3. Token expirado (401)
   → interceptor detecta 401
   → POST /auth/refresh con refresh_token
   → obtener nuevo access_token
   → reintentar request original (1 vez)
   → si refresh falla → logout

4. Logout
   → limpiar tokens del storage
   → redirigir a login
   → opcional: POST /auth/logout al backend
```

---

## Almacenamiento — Decisión

| Opción | Seguridad | Uso recomendado |
|--------|-----------|-----------------|
| `httpOnly cookie` | ✅ Alta (no accesible desde JS) | Cuando el backend lo soporta |
| Memoria (variable JS) | ✅ Media (no persiste XSS) | SPA sin refresh de página |
| `sessionStorage` | ⚠️ Media | Sesión por pestaña |
| `localStorage` | ❌ Baja (vulnerable a XSS) | Solo si no hay alternativa, con advertencia |

**Regla por defecto**: Si el usuario no especifica → preguntar. Si el backend no soporta cookies → usar memoria con `sessionStorage` como fallback, y advertir sobre XSS.

---

## Decode del token (sin librería)

```typescript
function decodeJwt<T = Record<string, unknown>>(token: string): T | null {
  try {
    const payload = token.split('.')[1];
    const decoded = atob(payload.replace(/-/g, '+').replace(/_/g, '/'));
    return JSON.parse(decoded) as T;
  } catch {
    return null;
  }
}

function isTokenExpired(token: string): boolean {
  const payload = decodeJwt<{ exp: number }>(token);
  if (!payload?.exp) return true;
  return Date.now() >= payload.exp * 1000;
}
```

---

## Checklist antes de entregar código

- [ ] ¿Se preguntaron las credenciales/endpoints que no estaban en el contexto?
- [ ] ¿El código es modular y reutilizable (no inline disperso)?
- [ ] ¿El interceptor maneja el refresh automático?
- [ ] ¿Hay manejo de errores y logout ante refresh fallido?
- [ ] ¿Se advirtió sobre la estrategia de almacenamiento elegida?
- [ ] ¿Se usó TypeScript si el proyecto lo usa?
- [ ] ¿El decode del JWT es sin dependencias externas innecesarias?
