* Informacion de los perfiles que se pueden enviar por el registro

## DEBES PARCEAR LAS CORDENADAS PREOPIAS A NUMERO PARA LLAMAR A LA API, SOY OLVIDADIZO, SI MAÑANA NO FUNCIONA, RECUERDAME ESTO


POST OBJECTS

● PERSONA                                                                                                                                           
{                                                                                                                                                 
"email": "string",
"password": "string",                                                                                                                           
"phone": "long",
"userType": "persona",                                                                                                                          
"name": "string",
"lastName": "string"
}

CLINICA
{
"email": "string",
"password": "string",
"phone": "long",
"userType": "clinica",
"clinicaName": "string",
"address": "string"
}

REFUGIO
{
"email": "string",
"password": "string",
"phone": "long",
"userType": "refugio",
"refugioName": "string",
"adress": "string"
}

que


* Estructura de envio de reporte

REPORTE POST OBJECT

{
idUsuario: number;
tipoReporte: 'PERDIDO' | 'AVISTADO';
tipoMascota: PERRO | GATO | OTRO
nombreMascota: string;
color: string;
tamano: 'PEQUENO' | 'MEDIANO' | 'GRANDE';
raza: string;
descripcion: string;
direccion: string;. t
coordenadas: string // formato "-lat, -long"
estado: string;
sexo: 'MACHO' | 'HEMBRA';
fotoMascota: string;
}
