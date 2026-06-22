---
name: web-design
description: >
  Principios y mejores prácticas de diseño gráfico y visual para páginas web y aplicaciones.
  Usa esta skill siempre que el usuario quiera crear, mejorar o evaluar el diseño visual de
  una interfaz web — incluyendo layouts, paletas de color, tipografía, jerarquía visual,
  espaciado, componentes UI, diseño responsivo, accesibilidad visual, y estética general.
  Activa también cuando el usuario diga "quiero que se vea bonito/profesional/moderno",
  pida inspiración de diseño, quiera rediseñar algo, o mencione que algo "no se ve bien".
  Combínala con la skill react-best-practices o frontend-design cuando corresponda.
---

# Web Design — Diseño Visual para la Web

Esta skill cubre los principios de diseño gráfico aplicados a interfaces web: cómo lograr
que un sitio o app se vea profesional, coherente y agradable. No se trata solo de "hacerlo
bonito" — el buen diseño comunica, guía y genera confianza.

---

## 1. Fundamentos visuales

### Jerarquía visual
- Usa **tamaño, peso y contraste** para guiar la mirada del usuario.
- Regla general: un solo elemento dominante por sección (hero, CTA principal, título).
- Nunca compitas entre dos elementos del mismo peso visual en la misma área.

### Espaciado (whitespace)
- El espacio en blanco no es "espacio desperdiciado" — es respiración y enfoque.
- Usa una escala de espaciado consistente: `4, 8, 12, 16, 24, 32, 48, 64, 96px` (base 4 o 8).
- Agrupa elementos relacionados con menos espacio; separa secciones con más.

### Alineación y grids
- Usa un sistema de grid (12 columnas es estándar). En CSS: `display: grid` o frameworks como Tailwind.
- Alinea siempre a una guía invisible — evita alineaciones arbitrarias.
- Menos columnas en mobile (1-2), más en desktop (3-4+).

---

## 2. Color

### Paletas efectivas
- **60-30-10**: 60% color base/neutro, 30% color secundario, 10% acento.
- No uses más de 3-4 colores en la paleta principal.
- Para interfaces profesionales: fondo neutro (blanco, gris claro o dark), texto oscuro, 1-2 colores de marca.

### Contraste y accesibilidad
- Ratio mínimo de contraste texto/fondo: **4.5:1** (WCAG AA) para texto normal, **3:1** para texto grande.
- Herramienta rápida: [coolors.co/contrast-checker](https://coolors.co/contrast-checker)
- Nunca pongas texto gris claro sobre fondo blanco — parece diseño moderno pero falla en accesibilidad.

### Psicología del color (aplicada a web)
| Color       | Asociación común         | Uso típico                        |
|-------------|--------------------------|-----------------------------------|
| Azul        | Confianza, tecnología    | SaaS, finanzas, corporativo       |
| Verde       | Éxito, salud, dinero     | CTAs de confirmación, salud       |
| Rojo/naranja| Urgencia, energía        | Alertas, descuentos, CTAs         |
| Morado      | Creatividad, premium     | Startups, belleza, lujo           |
| Negro/gris  | Elegancia, seriedad      | Moda, portafolios, tecnología     |

---

## 3. Tipografía

### Escala tipográfica
Usa una escala modular (ratio 1.25 o 1.333):
```
xs:   12px
sm:   14px
base: 16px
lg:   18px
xl:   20px
2xl:  24px
3xl:  30px
4xl:  36px
5xl:  48px
6xl:  60px
```

### Pairing de fuentes
- Máximo 2 familias tipográficas: una para títulos, una para cuerpo.
- Pairings confiables:
  - **Inter + Inter** (solo pesos distintos — nunca falla)
  - **Playfair Display + Inter** (editorial, elegante)
  - **Space Grotesk + DM Sans** (tech, moderno)
  - **Fraunces + Libre Baskerville** (cálido, humano)

### Legibilidad
- Ancho de línea ideal: **60-75 caracteres** (usa `max-width: 65ch` en párrafos).
- Line-height en cuerpo: **1.5–1.7**. En títulos: **1.1–1.3**.
- Evita texto completamente en mayúsculas para párrafos — solo para labels y badges.

---

## 4. Componentes y patrones visuales

### Botones
- CTA primario: color sólido de acento, buen contraste, `border-radius` consistente con el resto del diseño.
- CTA secundario: outline o ghost — nunca del mismo peso que el primario.
- Estados obligatorios: `:hover`, `:active`, `:disabled`, `:focus-visible`.

### Cards
- Usa sombra sutil o borde ligero — no ambos a la vez salvo que sea intencional.
- Padding interno consistente (16-24px típico).
- Si las cards son clickeables, asegúrate de que el cursor y el hover lo indiquen.

### Imágenes
- Aspect ratios consistentes dentro de una misma sección.
- Usa `object-fit: cover` para evitar distorsión.
- Siempre incluye `alt` descriptivo.

### Iconografía
- Elige una sola librería (Lucide, Heroicons, Phosphor) — no mezcles estilos.
- Tamaño consistente: 16, 20 o 24px para UI inline. Iconos decorativos: 32-48px.

---

## 5. Layout y composición

### Secciones de landing page (orden recomendado)
1. **Hero** — propuesta de valor clara, CTA visible above the fold.
2. **Social proof** — logos de clientes o métricas clave.
3. **Features/Beneficios** — grid de 3 o descripción con imagen alternada.
4. **Cómo funciona** — pasos o diagrama simple.
5. **Testimonios** — cards con foto, nombre y cita.
6. **Pricing** — tabla clara con destacado en el plan recomendado.
7. **FAQ** — acordeón o lista.
8. **CTA final** — repetición del llamado a la acción.
9. **Footer** — links, redes, copyright.

### Above the fold
- El usuario decide en ~3 segundos si se queda. El hero debe comunicar: **qué es**, **para quién es** y **qué hacer**.
- CTA above the fold siempre.

---

## 6. Diseño responsivo

### Breakpoints estándar (mobile-first)
```css
/* Mobile: base (< 640px) */
/* Tablet: sm — 640px+ */
/* Desktop: md — 768px+, lg — 1024px+, xl — 1280px+ */
```

### Reglas prácticas
- Empieza diseñando en mobile, luego escala a desktop.
- En mobile: una columna, tipografía más grande (mejor legibilidad), botones full-width o grandes.
- Navegación en mobile: hamburger o bottom nav bar.
- No reduzcas el padding interno de cards a 0 en mobile — mínimo 12-16px.

---

## 7. Micro-interacciones y detalles que marcan la diferencia

- **Transiciones suaves**: `transition: all 0.2s ease` en hovers. Nada de 0s ni más de 0.4s para UI.
- **Loading states**: skeleton screens > spinners para contenido largo.
- **Empty states**: ilustración o icono + texto explicativo + acción sugerida.
- **Feedback inmediato**: cambio visual al hacer click, submit, etc.
- **Focus rings**: personaliza `:focus-visible` — accesibilidad no significa feo.

---

## 8. Checklist de calidad visual

Antes de dar por terminado un diseño, verifica:

- [ ] ¿Hay jerarquía clara? ¿Sé qué leer primero?
- [ ] ¿El contraste de texto cumple WCAG AA?
- [ ] ¿El espaciado es consistente (escala de 4/8px)?
- [ ] ¿Máximo 2 familias tipográficas?
- [ ] ¿La paleta tiene máximo 3-4 colores principales?
- [ ] ¿Todos los botones tienen estados hover/focus/disabled?
- [ ] ¿Se ve bien en mobile (375px) y desktop (1280px)?
- [ ] ¿Las imágenes tienen `alt` y aspect ratios consistentes?
- [ ] ¿El CTA más importante está above the fold?

---

## Referencias adicionales

- Ver `references/design-inspiration.md` para recursos y fuentes de inspiración.
- Para implementación en código, combinar con skill `react-best-practices` o `frontend-design`.
