CREATE DATABASE reporte_db;

CREATE TABLE public.profiles (
    id uuid NOT NULL,
    email text NOT NULL,
    password_hash text NOT NULL,
    updated_at timestamp with time zone DEFAULT now(),
    phone numeric,
    user_type text,
    role text,
    CONSTRAINT profiles_pkey PRIMARY KEY (id),
    CONSTRAINT profiles_email_key UNIQUE (email)
);

CREATE TABLE public.personas (
    id uuid NOT NULL,
    name text,
    last_name text,
    CONSTRAINT personas_pkey PRIMARY KEY (id),
    CONSTRAINT personas_id_fkey FOREIGN KEY (id) REFERENCES public.profiles(id)
);

CREATE TABLE public.clinica (
    id uuid NOT NULL,
    clinica_name text,
    address text,
    CONSTRAINT clinica_pkey PRIMARY KEY (id),
    CONSTRAINT clinica_id_fkey FOREIGN KEY (id) REFERENCES public.profiles(id)
);

CREATE TABLE public.refugio (
    id uuid NOT NULL,
    refugio_name text,
    adress text,
    CONSTRAINT refugio_pkey PRIMARY KEY (id),
    CONSTRAINT refugio_id_fkey FOREIGN KEY (id) REFERENCES public.profiles(id)
);
