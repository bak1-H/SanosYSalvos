# Campos usados — Vista de Notificaciones

## GET /notificaciones?idUsuario=:id → ApiNotificacion[]

| Campo                | Tipo                              | Usado | Para qué                                      |
|----------------------|-----------------------------------|-------|-----------------------------------------------|
| id_notificacion      | number                            | ✅    | key de React                                  |
| estado_notificacion  | NotificationStatus                | ✅    | Estilo del card + badge + orden de lista       |
| descripcion          | string                            | ✅    | Texto principal del card                      |
| id_coincidencia      | number                            | ✅    | Botón "Ver coincidencia #N"                   |
| fecha_creacion       | string (ISO 8601)                 | ✅    | Fecha relativa (Hoy / Ayer / Hace N días)     |
| mensajeError         | string \| null                    | ✅    | Bloque de error bajo la descripción           |

Campo **no usado**: `id_usuario` (ya se conoce desde el contexto de auth).

---

## NotificationStatus (inferido — no documentado en el API)

| Valor     | Significado                              | Visual                          |
|-----------|------------------------------------------|---------------------------------|
| PENDIENTE | No leída / requiere atención del usuario | Borde izquierdo azul + dot azul |
| LEIDA     | Ya fue vista por el usuario              | Card atenuado + dot gris        |
| ERROR     | Falló el procesamiento de la coincidencia | Borde izquierdo rojo + mensaje de error |

---

## Ordenamiento en la vista

Las notificaciones se ordenan: `PENDIENTE → ERROR → LEIDA`.
El badge del BottomNavbar muestra el conteo de notificaciones con estado `PENDIENTE`.
