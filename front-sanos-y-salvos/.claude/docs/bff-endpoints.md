# BFF — Endpoints requeridos por el frontend

> Todos los endpoints están bajo un prefijo común, ej. `/bff/v1/`.
> El BFF es responsable de hablar con Supabase Auth y con el backend de negocio,
> y de ensamblar/filtrar la respuesta para lo que la vista necesita exactamente.

---

## Resumen

| # | Método | Ruta                     | Vista que lo consume              |
|---|--------|--------------------------|-----------------------------------|
| 1 | POST   | `/auth/login`            | Login (pendiente de construir)    |
| 2 | POST   | `/auth/registro`         | RegisterView                      |
| 3 | GET    | `/usuarios/me`           | PerfilView                        |
| 4 | GET    | `/reportes/cercanos`     | DashboardView (mapa)              |
| 5 | GET    | `/reportes/mis-reportes` | PerfilView (sección Mis reportes) |
| 6 | GET    | `/reportes/:id`          | ReporteDetailView                 |
| 7 | POST   | `/reportes`              | CrearReporteView                  |
| 8 | GET    | `/notificaciones`        | NotificacionesView                |

**Total: 8 endpoints**

---

## 1. POST `/auth/login`

**Vista:** Login (pendiente de construir en el frontend).

**Qué hace el BFF:** llama a Supabase Auth → devuelve el token y los datos mínimos del usuario.

**Request body:**
```json
{
  "email": "string",
  "password": "string"
}
```

**Response esperada por el frontend:**
```json
{
  "access_token": "string",
  "user": {
    "id": "string (UUID)",
    "email": "string",
    "role": "PERSONA | INSTITUCION",
    "userType": "persona | clinica | refugio"
  }
}
```

**Nota:** El `email` se extrae de `user_metadata.email` de la respuesta de Supabase.
El BFF no debe exponer el `refresh_token` directamente — manejarlo via cookie `httpOnly`.

---

## 2. POST `/auth/registro`

**Vista:** `RegisterView` — formularios de persona, clínica y refugio.

**Qué hace el BFF:**
1. Crea la cuenta en Supabase Auth con email + password.
2. Crea el perfil en la base de datos con los datos del formulario.

**Request body (varía por `userType`):**

```json
// userType: "persona"
{
  "userType": "persona",
  "email": "string",
  "password": "string",
  "phone": "string",
  "name": "string",
  "lastName": "string"
}

// userType: "clinica"
{
  "userType": "clinica",
  "email": "string",
  "password": "string",
  "phone": "string",
  "clinicaName": "string",
  "address": "string"
}

// userType: "refugio"
{
  "userType": "refugio",
  "email": "string",
  "password": "string",
  "phone": "string",
  "refugioName": "string",
  "adress": "string"
}
```

**Response esperada:**
```json
{
  "message": "Registro exitoso",
  "user": {
    "id": "string (UUID)",
    "email": "string",
    "role": "PERSONA | INSTITUCION",
    "userType": "persona | clinica | refugio"
  }
}
```

---

## 3. GET `/usuarios/me`

**Vista:** `PerfilView` — cabecera con nombre, rol, y datos de contacto.

**Qué hace el BFF:** lee el `id` del usuario desde el JWT → consulta el perfil en la DB.

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Response esperada por el frontend:**
```json
{
  "id": "string (UUID)",
  "nombre": "string",
  "email": "string",
  "telefono": "string",
  "rol": "PERSONA | INSTITUCION",
  "userType": "persona | clinica | refugio",
  "direccion": "string | null"
}
```

**Qué ensambla el BFF:**
- `nombre` → BFF construye el string: `name + lastName` para persona, `clinicaName` o `refugioName` para institución.
- `email` → desde Supabase Auth (`user_metadata.email`).
- `telefono` → convierte el `phone` (number) a string.
- `direccion` → unifica `address` (clínica) y `adress` (refugio, typo del backend) en un solo campo.

---

## 4. GET `/reportes/cercanos`

**Vista:** `DashboardView` — mapa con pins de reportes cercanos al usuario.

**Query params:**
```
?lat=-33.4489&lng=-70.6693&radio=1
```
- `radio` en kilómetros (default: 1).

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Response esperada por el frontend:**
```json
[
  {
    "id": "number",
    "nombreMascota": "string",
    "tipoMascota": "string",
    "raza": "string",
    "sexo": "MACHO | HEMBRA",
    "tamano": "PEQUENO | MEDIANO | GRANDE",
    "color": "string",
    "descripcion": "string",
    "fotoMascota": "string (URL)",
    "direccion": "string",
    "coordenadas": "string ('-lat, -lng')",
    "estado": "ACTIVO | INACTIVO | RESUELTO",
    "tipoReporte": "PERDIDO | AVISTADO"
  }
]
```

**Nota:** El filtro geoespacial puede hacerlo el BFF o el backend. Si el backend no lo soporta,
el BFF recibe todos los reportes activos y aplica el filtro de Haversine.
Solo retornar reportes con `estado: ACTIVO`.

---

## 5. GET `/reportes/mis-reportes`

**Vista:** `PerfilView` — sección "Mis reportes" (cards resumidas).

**Qué hace el BFF:** lee el `id` del usuario desde el JWT → filtra reportes por `idUsuario`.

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Response esperada por el frontend (subset reducido):**
```json
[
  {
    "id": "number",
    "nombreMascota": "string",
    "tipoMascota": "string",
    "tipoReporte": "PERDIDO | AVISTADO",
    "estado": "ACTIVO | INACTIVO | RESUELTO",
    "fotoMascota": "string (URL) | null",
    "direccion": "string"
  }
]
```

**Nota:** El BFF proyecta solo estos 7 campos. No devolver `coordenadas`, `raza`, `descripcion`, etc.
El usuario puede tener reportes en cualquier estado (no filtrar por `ACTIVO` aquí).

---

## 6. GET `/reportes/:id`

**Vista:** `ReporteDetailView` — detalle completo de un reporte con mapa.

**Path param:** `id` — número entero.

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Response esperada por el frontend:**
```json
{
  "id": "number",
  "nombreMascota": "string",
  "tipoMascota": "string",
  "raza": "string",
  "sexo": "MACHO | HEMBRA",
  "tamano": "PEQUENO | MEDIANO | GRANDE",
  "color": "string",
  "descripcion": "string",
  "fotoMascota": "string (URL)",
  "direccion": "string",
  "coordenadas": "string ('-lat, -lng')",
  "estado": "ACTIVO | INACTIVO | RESUELTO",
  "tipoReporte": "PERDIDO | AVISTADO"
}
```

---

## 7. POST `/reportes`

**Vista:** `CrearReporteView` — formulario de creación de reporte.

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Request body:**
```json
{
  "tipoReporte": "PERDIDO | AVISTADO",
  "tipoMascota": "PERRO | GATO | OTRO",
  "nombreMascota": "string",
  "raza": "string",
  "color": "string",
  "tamano": "PEQUENO | MEDIANO | GRANDE",
  "sexo": "MACHO | HEMBRA",
  "descripcion": "string",
  "direccion": "string",
  "fotoMascota": "string (URL) | ''"
}
```

**Qué ensambla el BFF antes de enviar al backend:**
- `idUsuario` → extraído del JWT, no lo envía el frontend.
- `estado` → siempre `"ACTIVO"` en creación.
- `coordenadas` → el BFF **geocodifica** la `direccion` con un servicio externo
  (ej. Nominatim/OpenStreetMap o Google Maps Geocoding API) y agrega el campo
  en formato `"-lat, -lng"` antes de persistir.

**Response esperada:**
```json
{
  "message": "Reporte creado exitosamente",
  "id": "number"
}
```

---

## 8. GET `/notificaciones`

**Vista:** `NotificacionesView` — lista de alertas de coincidencias.

**Qué hace el BFF:** lee el `id` del usuario desde el JWT → retorna sus notificaciones.

**Headers requeridos:** `Authorization: Bearer <access_token>`

**Response esperada por el frontend:**
```json
[
  {
    "id_notificacion": "number",
    "estado_notificacion": "PENDIENTE | LEIDA | ERROR",
    "descripcion": "string",
    "id_coincidencia": "number",
    "fecha_creacion": "string (ISO 8601)",
    "mensajeError": "string | null"
  }
]
```

**Nota:** El `id_coincidencia` se usa en el frontend para navegar al detalle del reporte
coincidente (`/reportes/:id_coincidencia`). Mientras no exista una vista de coincidencia
dedicada, el BFF debe asegurarse de que `id_coincidencia` sea navegable como reporte.

---

## Consideraciones generales del BFF

### Auth
- Todos los endpoints (excepto `/auth/login` y `/auth/register`) requieren JWT de Supabase.
- El BFF valida el token con Supabase antes de procesar cada request.
- El `id` del usuario siempre se extrae del token — nunca se recibe como parámetro del frontend.

### Geocodificación (POST /reportes)
- El formulario de creación solo captura `direccion` en texto libre.
- El BFF es responsable de convertirla a `coordenadas` antes de persistir.
- Si la geocodificación falla, retornar `400` con mensaje claro.

### Campos que el BFF normaliza
| Campo raw (backend)     | Campo normalizado (BFF → frontend) |
|-------------------------|------------------------------------|
| `name + lastName`       | `nombre` (string unificado)        |
| `clinicaName / refugioName` | `nombre`                       |
| `address / adress`      | `direccion` (un solo campo)        |
| `phone` (number)        | `telefono` (string)                |
| `id_notificacion`       | `id` (consistencia de naming)      |

### Estado de reportes
- El backend no debe gestionar `estado` desde el frontend (documentado en el API).
- En creación: el BFF siempre envía `estado: ACTIVO`.
- Cambios de estado (RESUELTO, INACTIVO) se gestionan desde el backend o admin.
