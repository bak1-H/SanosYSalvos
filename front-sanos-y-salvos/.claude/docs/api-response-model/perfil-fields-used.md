# Campos usados — Vista de Perfil

## GET /usuarios/:id → ApiUsuario

Respuesta completa del endpoint real:

```json
{
  "error": null,
  "message": "Usuario encontrado exitosamente",
  "status": 200,
  "user": { ... }
}
```

| Campo        | Tipo              | Usado | Para qué                                         |
|--------------|-------------------|-------|--------------------------------------------------|
| id           | string (UUID)     | ✅    | Clave interna, futura call a reportes            |
| name         | string \| null    | ✅    | Nombre de PERSONA                                |
| lastName     | string \| null    | ✅    | Apellido de PERSONA                              |
| clinicaName  | string \| null    | ✅    | Nombre de institución tipo clínica               |
| refugioName  | string \| null    | ✅    | Nombre de institución tipo refugio               |
| address      | string \| null    | ✅    | Dirección (clínica)                              |
| adress       | string \| null    | ✅    | Dirección (refugio) — typo del API, se mantiene  |
| phone        | number            | ✅    | Teléfono (se convierte a string en la UI)        |
| role         | 'PERSONA' \| 'INSTITUCION' | ✅ | Badge de rol                              |
| userType     | 'persona' \| 'clinica' \| 'refugio' | ✅ | Lógica de nombre y dirección     |

**Nota:** `email` NO viene en este endpoint. Se obtiene del token de Supabase
(`user_metadata.email` en `SupabaseLoginResponse`).

**Nota:** `fechaRegistro` no está disponible en este endpoint — no se muestra en la vista.

---

## Reporte (GET /reportes?idUsuario=:id)

Usado en la sección "Mis reportes" del perfil. Solo se consume un subconjunto.

| Campo         | Tipo                                  | Usado | Para qué                  |
|---------------|---------------------------------------|-------|---------------------------|
| id            | number                                | ✅    | key de React              |
| nombreMascota | string                                | ✅    | Título del card           |
| tipoMascota   | string                                | ✅    | Subtítulo del card        |
| tipoReporte   | 'PERDIDO' \| 'AVISTADO'              | ✅    | Badge con color           |
| estado        | 'ACTIVO' \| 'INACTIVO' \| 'RESUELTO' | ✅    | Badge de estado           |
| fotoMascota   | string (URL)                          | ✅    | Thumbnail (opcional)      |
| direccion     | string                                | ✅    | Texto secundario del card |

Campos **no usados**: idUsuario, raza, sexo, tamano, color, descripcion, coordenadas.
