* ESTOS MODELOS SON REFERENCIAS PARA TRABAJAR. EN FUNCION DE LOS DATOS QUE REQUIERA LA VISTA, SO CONSUMIRAN LOS OBJETOS Y SE DEFINIRA LO QUE SE UTILIZARA EN EN BFF
* LOS VALORES DE LOS OBJETOS CON ENUM, DEBEN RESPETARSE


* Modelo de las notificaciones
* Usar para la creacion de notificaciones

## RECEPCION NOTIFICACION

{
id_notificacion: number;
estado_notificacion: NotificationStatus;
descripcion: string;
id_coincidencia: number;
id_usuario: number;
fecha_creacion: Date;
mensajeError: string | null;
}



* Modelo de respuesta de reportes:
* EL ESTADO NO DEBE MANEJARSE DESTE EL FRONTEND
* Usar para las vistas de reportes

## RECEPCION REPORTE

{
id: number;
idUsuario: number;
nombreMascota: string;
tipoMascota: string; // Podrías usar 'Perro' | 'Gato' si son fijos
raza: string;
sexo: 'MACHO' | 'HEMBRA';
tamano: 'PEQUENO' | 'MEDIANO' | 'GRANDE';
color: string;
descripcion: string;
fotoMascota: string; // URL
direccion: string;
coordenadas: string; // Formato "-lat, -long"
estado: 'ACTIVO' | 'INACTIVO' | 'RESUELTO';
tipoReporte: 'PERDIDO' | 'AVISTADO';

}

## SUPABASE LOGIN RESPONSE

/**
* @description Estructura exacta del payload de respuesta del endpoint /auth/v1/token de Supabase.
* Basado en la respuesta real del proyecto 'sanos-y-salvos-regAuth'.
  */
  type SupabaseLoginResponse = {
  access_token: string;
  token_type: "bearer";
  expires_in: number;
  expires_at: number;
  refresh_token: string;
  user: {
  id: string; // UUID
  aud: "authenticated";
  role: "authenticated";
  email: string;
  email_confirmed_at: string; // ISO 8601
  phone: string;
  confirmed_at: string;
  last_sign_in_at: string;
  app_metadata: {
  provider: string;
  providers: string[];
  };
  user_metadata: {
  email: string;
  email_verified: boolean;
  phone: string;
  phone_verified: boolean;
  role: "INSTITUCION" | string; // Campo personalizado
  user_type: "refugio" | string; // Campo personalizado
  sub: string;
  };
  identities: Array<{
  identity_id: string;
  id: string;
  user_id: string;
  identity_data: {
  email: string;
  email_verified: boolean;
  phone: string;
  phone_verified: boolean;
  role: string;
  user_type: string;
  sub: string;
  };
  provider: string;
  last_sign_in_at: string;
  created_at: string;
  updated_at: string;
  email: string;
  }>;
  created_at: string;
  updated_at: string;
  is_anonymous: boolean;
  };
  weak_password: null | string;
  };

 ## Respuesta de busqueda de usuario por id

{
"error": null,
"message": "Usuario encontrado exitosamente",
"status": 200,
"user": {
"address": null,
"adress": null,
"clinicaName": null,
"id": "dd6efccc-eaa3-4c18-a50e-17e900b23ac9",
"lastName": "Lopez",
"name": "Maria",
"phone": 922222222,
"refugioName": null,
"role": "PERSONA",
"userType": "persona"
}
}
