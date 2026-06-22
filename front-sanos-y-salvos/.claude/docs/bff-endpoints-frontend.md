# BFF Endpoints — Referencia para Frontend

Base URL: `http://localhost:8090`

---

## POST /api/registro

Registra un nuevo usuario. No requiere token.

### Request

Dependiendo del tipo de usuario, algunos campos son opcionales. El campo `userType` determina el tipo.

**PERSONA**
```json
{
  "email": "string",
  "password": "string",
  "phone": 123456789,
  "userType": "persona",
  "name": "string",
  "lastName": "string"
}
```

**CLINICA**
```json
{
  "email": "string",
  "password": "string",
  "phone": 123456789,
  "userType": "clinica",
  "clinicaName": "string",
  "address": "string"
}
```

**REFUGIO**
```json
{
  "email": "string",
  "password": "string",
  "phone": 123456789,
  "userType": "refugio",
  "refugioName": "string",
  "adress": "string"
}
```

> ⚠️ `adress` en REFUGIO es así — sin doble `d`. Respetar exactamente.

### Response exitosa — 201
```json
{
  "error": null,
  "message": "string",
  "status": 201,
  "token": "string"
}
```

### Response de error — 500
```json
{
  "error": "string",
  "message": "string",
  "status": 500,
  "token": null
}
```

---

## POST /api/login

Autentica un usuario existente. No requiere token.

### Request
```json
{
  "email": "string",
  "password": "string"
}
```

### Response exitosa — 200
```json
{
  "status": 200,
  "accessToken": "string",
  "expiresIn": 3600,
  "refreshToken": "string",
  "user": {
    "email": "string",
    "role": "string",
    "phone": "string",
    "userType": "string"
  }
}
```

> El `accessToken` es un JWT Bearer. Guardarlo para enviarlo en requests protegidos como `Authorization: Bearer <token>`.

### Response de error — 400
```json
{
  "code": 400,
  "error_code": "invalid_credentials",
  "msg": "Invalid login credentials"
}
```

---

## Manejo de errores general

| Status | Causa |
|--------|-------|
| 201 | Registro exitoso |
| 200 | Login exitoso |
| 400 | Credenciales inválidas (login) |
| 500 | Error interno del microservicio (registro) |
| 503 | BFF no puede conectarse al microservicio destino |
