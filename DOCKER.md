# Cómo levantar Sanos y Salvos con Docker Compose

Este repo es un monorepo de microservicios. El `docker-compose.yml` de la raíz levanta toda la infraestructura y todos los servicios de una sola vez.

## Requisitos previos

- Docker y Docker Compose instalados (`docker compose version` debe funcionar).
- Un archivo `.env` en la raíz del repo (junto a `docker-compose.yml`) con estas variables:

  ```env
  POSTGRES_DB=...
  POSTGRES_USER=...
  POSTGRES_PASSWORD=...
  ```

  Sin este archivo, `postgres-registro` y los servicios que dependen de la base de datos no van a levantar.

## Servicios que define el compose

| Servicio | Rol | Puerto expuesto |
|---|---|---|
| `eureka-server` | Service discovery (Netflix Eureka) | 8761 |
| `rabbitmq` | Cola de mensajería (panel de admin) | 15672 |
| `postgres-registro` | Base de datos Postgres compartida | interno (5432) |
| `api-registro` | Registro/login de usuarios | interno (8080) |
| `api-reporte` | Microservicio de reportes | 8086 |
| `ms-coincidencias` | Microservicio de coincidencias | 8082 |
| `notification-ms` | Microservicio de notificaciones | 8083 |
| `api-gateway` | Punto de entrada único (valida JWT, enruta) | 8081 |
| `bff-sanys` | Backend for Frontend | 8090 |

El orden de arranque (quién espera a quién) ya está resuelto con `depends_on` + `healthcheck` en el propio `docker-compose.yml` — no hay que levantarlos a mano en ningún orden particular.

## Levantar todo

Desde la raíz del repo (donde está `docker-compose.yml`):

```bash
docker compose up -d --build
```

- `--build` fuerza a reconstruir las imágenes a partir del código fuente (necesario la primera vez o después de cambiar código de algún microservicio).
- `-d` lo deja corriendo en segundo plano.

Para las siguientes veces, si no cambiaste código, alcanza con:

```bash
docker compose up -d
```

## Ver qué está pasando

```bash
# Estado de todos los contenedores
docker compose ps

# Logs de todo
docker compose logs -f

# Logs de un servicio puntual
docker compose logs -f api-registro
```

## Reconstruir un solo servicio

Si solo cambiaste código de un microservicio (por ejemplo `api-registro`), no hace falta rehacer todo:

```bash
docker compose up -d --build api-registro
```

## Apagar

```bash
# Para los contenedores, conserva los datos de Postgres
docker compose down

# Para los contenedores y BORRA los volúmenes (datos de Postgres, claves JWT)
docker compose down -v
```

## Troubleshooting rápido

- **`postgres-registro` nunca queda "healthy"**: revisá que el `.env` tenga `POSTGRES_DB`, `POSTGRES_USER` y `POSTGRES_PASSWORD` seteados.
- **Un microservicio reinicia en loop**: `docker compose logs -f <nombre-del-servicio>` — casi siempre es que arrancó antes de que su dependencia (Postgres, RabbitMQ, Eureka) estuviera lista, o un error de conexión a la base.
- **Cambié código y no se refleja**: te olvidaste el `--build`. Docker Compose no reconstruye la imagen solo porque cambió el código fuente.
