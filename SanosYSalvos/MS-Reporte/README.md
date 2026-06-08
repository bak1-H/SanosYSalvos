# MS-Reportes

Microservicio de reportes para una plataforma de mascotas perdidas y encontradas. El proyecto expone una API REST para crear, consultar, actualizar y eliminar reportes, persiste la información en PostgreSQL, publica eventos en RabbitMQ y usa LocationIQ para transformar direcciones en coordenadas.

## Qué hace este proyecto

Este servicio centraliza la gestión de reportes con la siguiente responsabilidad:

- recibir reportes desde clientes web o integraciones externas,
- validar y transformar la información de entrada,
- enriquecer la dirección con coordenadas geográficas,
- persistir los datos en base de datos,
- publicar eventos para que otros servicios reaccionen de forma asíncrona.

## Stack tecnológico

- Java 21
- Spring Boot 4.0.5
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring AMQP
- PostgreSQL
- RabbitMQ
- LocationIQ
- Lombok
- Maven Wrapper
- Docker y Docker Compose
- Mockito y JUnit 5 para pruebas

## Estructura del proyecto

El código principal está en `MS-Reporte/reporte/reporte/src/main/java/cl/sanosysalvos/reporte` y se organiza así:

- `controller`: expone los endpoints REST.
- `service`:
  - `ReporteService` define el contrato de negocio.
  - `ReporteServiceImpl` contiene la lógica principal del caso de uso.
  - `GeoService` consulta LocationIQ para obtener coordenadas.
- `repository`: acceso a datos con Spring Data JPA.
- `model`: entidades JPA y enums de dominio.
- `dto`: objetos de entrada y salida de la API.
- `messaging`: publicación de eventos hacia RabbitMQ.
- `config`: configuración de `RabbitMQ` y `RestTemplate`.

## Flujo principal

1. El cliente envía un `POST /reportes` con un `ReporteRequestDTO`.
2. `ReporteController` delega la operación en `ReporteService`.
3. `ReporteServiceImpl` arma la entidad `ReporteModel` y decide si debe enriquecer la dirección.
4. Si existe dirección, `GeoService` llama a LocationIQ y obtiene latitud y longitud.
5. El reporte se persiste con `ReporteRepository`.
6. El resultado se transforma a `ReporteResponseDTO`.
7. `ReportePublisher` publica un evento en la cola `reporte.creado.queue`.
8. El controlador responde al cliente con el estado HTTP correspondiente.

## Modelo de dominio

### `ReporteModel`

Entidad principal persistida en la tabla `reportes`.

Campos principales:

- `idReporte`
- `idUsuario`
- `tipoReporte`
- `tipoMascota`
- `nombreMascota`
- `color`
- `tamano`
- `raza`
- `fotoMascota`
- `descripcion`
- `direccion`
- `coordenadas`
- `estado`
- `sexo`

### Enums

- `TipoReporte`: define el tipo de reporte de negocio.
- `TamanoMascota`: define el tamaño de la mascota.

## DTOs

### `ReporteRequestDTO`

Objeto que recibe la API cuando se crea o actualiza un reporte. Se usa para desacoplar el contrato externo de la entidad interna.

### `ReporteResponseDTO`

Objeto que devuelve la API al cliente. Incluye los campos relevantes del reporte y evita exponer la estructura completa de persistencia.

## Componentes principales

### `ReporteController`

Expone la API REST sobre la ruta `/reportes`.

Endpoints:

- `POST /reportes`
- `GET /reportes`
- `GET /reportes/{id}`
- `PUT /reportes/{id}`
- `DELETE /reportes/{id}`

### `ReporteServiceImpl`

Contiene la lógica de negocio. Se encarga de:

- convertir DTOs en entidades,
- aplicar reglas de actualización,
- consultar coordenadas cuando corresponde,
- persistir en base de datos,
- transformar entidades a DTO de respuesta,
- lanzar errores cuando el recurso no existe.

### `GeoService`

Resuelve coordenadas geográficas desde una dirección usando LocationIQ. Está aislado en un servicio aparte para que la lógica de geocodificación no contamine el flujo del dominio principal.

### `ReportePublisher`

Publica eventos en RabbitMQ. Esto permite que otros servicios consuman el alta de reportes sin acoplarse directamente a la API.

### `ReporteRepository`

Extiende `JpaRepository` y encapsula el acceso a PostgreSQL. También define consultas personalizadas para búsquedas específicas.

### `RestTemplateConfig`

Provee el bean de `RestTemplate` para mantener el cliente HTTP administrado por Spring y facilitar el testeo.

### `RabbitMQConfig`

Configura el convertidor JSON para serializar y deserializar mensajes de forma simple.

## Integraciones externas

### PostgreSQL

Se usa como base de datos relacional principal.

Configuración típica:

- host: `postgres`
- puerto: `5432`
- base de datos: `coincidencias`
- usuario: `postgres`
- contraseña: `postgres`

### RabbitMQ

Se usa para mensajería asíncrona y desacople entre servicios.

- cola principal: `reporte.creado.queue`
- puerto AMQP: `5672`
- consola de administración: `15672`

### LocationIQ

Se usa para convertir una dirección en coordenadas. La API key se define en `application.properties` o como variable de entorno en Docker.

## Endpoints de ejemplo

### Crear reporte

`POST /reportes`

Ejemplo de body:

```json
{
  "idUsuario": 123,
  "tipoReporte": "PERDIDO",
  "tipoMascota": "Perro",
  "nombreMascota": "Firulais",
  "color": "marron",
  "tamano": "MEDIANO",
  "raza": "Labrador",
  "fotoMascota": "https://ejemplo.com/foto.jpg",
  "descripcion": "Se perdió en el parque",
  "direccion": "Av. Siempre Viva 123",
  "sexo": "Macho"
}
```

### Listar reportes

`GET /reportes`

### Obtener por id

`GET /reportes/{id}`

### Actualizar reporte

`PUT /reportes/{id}`

### Eliminar reporte

`DELETE /reportes/{id}`

## Configuración local

La configuración principal está en `MS-Reporte/reporte/reporte/src/main/resources/application.properties`.

Propiedades relevantes:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`
- `spring.jpa.hibernate.ddl-auto`
- `spring.rabbitmq.host`
- `spring.rabbitmq.port`
- `locationiq.api-key`

### Ejecutar localmente con Maven

```powershell
cd "MS-Reporte/reporte/reporte"
.\mvnw.cmd spring-boot:run
```

### Ejecutar tests

```powershell
cd "MS-Reporte/reporte/reporte"
.\mvnw.cmd test
```

## Docker

El proyecto incluye `dockerfile` y `docker-compose.yml` para levantar el servicio junto con PostgreSQL y RabbitMQ.

### Levantar el entorno completo

```powershell
cd "MS-Reporte/reporte/reporte"
docker compose up --build
```

### Servicios incluidos en `docker-compose.yml`

- `postgres`: base de datos PostgreSQL.
- `rabbitmq`: broker de mensajería con consola de administración.
- `reporte-api`: aplicación Spring Boot.

### Nota importante

El archivo de ejemplo de Docker contiene una clave de LocationIQ de prueba. Para un entorno real conviene mover ese valor a variables de entorno o a un archivo `.env` y no dejar credenciales sensibles embebidas.

## Pruebas

El proyecto incluye pruebas unitarias y de integración ligera sobre los componentes principales:

- `ReporteControllerTest`
- `ReporteServiceImplTest`
- `ReportePublisherTest`
- `RabbitMQConfigTest`
- `ReporteModelAndDtoTest`
- `ReporteApplicationTests`
- `GeoServiceTest`

### Cobertura

Se agregó JaCoCo para medir cobertura. Con el set actual de pruebas, el objetivo mínimo de 80% queda cumplido.

### Qué validan los tests

- comportamiento del controlador REST,
- reglas de negocio del servicio,
- publicación hacia RabbitMQ,
- serialización/configuración,
- mapeo de modelos y DTOs,
- arranque básico de la aplicación,
- geocodificación controlada por mock.

## Decisiones de arquitectura

### Arquitectura en capas

El proyecto usa la división Controller → Service → Repository para separar responsabilidades y mantener el flujo simple de leer, probar y modificar.

### Patrón DTO

Se usan `ReporteRequestDTO` y `ReporteResponseDTO` para evitar exponer directamente la entidad JPA al exterior y para controlar el contrato de la API.

### `ServiceImpl`

`ReporteServiceImpl` concentra la lógica de negocio, las reglas de actualización y la integración con coordenadas y eventos. Eso evita que el controlador quede con demasiada responsabilidad.

### Mensajería con RabbitMQ

RabbitMQ desacopla la creación del reporte de los consumidores posteriores. El servicio principal no necesita conocer qué otros sistemas reaccionan al evento.

### Geocodificación con LocationIQ

LocationIQ se usa porque convierte texto libre de dirección en coordenadas sin implementar lógica propia de geolocalización.

### Docker

Docker y Docker Compose facilitan reproducibilidad, levantan PostgreSQL y RabbitMQ de forma consistente y reducen diferencias entre desarrollo local y despliegue.

