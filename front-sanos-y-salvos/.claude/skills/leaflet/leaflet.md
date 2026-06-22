---
name: react-leaflet-maps
description: Integrar mapas interactivos open source con React y TypeScript usando React Leaflet v5. Usá esta skill cuando el usuario quiera mostrar un mapa con marcadores/pines desde una lista de coordenadas, clustering de puntos, popups, iconos custom, o cualquier visualización de ubicaciones en un mapa. Triggers: "mapa", "leaflet", "markers", "pines", "coordenadas en mapa", "ubicaciones en mapa", "clustering de markers".
---

Esta skill cubre la integración de mapas interactivos open source con **React Leaflet v5** y **TypeScript**, incluyendo markers desde listas de coordenadas, clustering, iconos custom, popups y manejo de SSR en Next.js.

## Stack

- `leaflet` ^1.9.x — librería base
- `react-leaflet` ^5.0.x — componentes React para Leaflet
- `react-leaflet-cluster` — clustering de markers (compatible con react-leaflet v5)
- `@types/leaflet` — tipos TypeScript

## Instalación

```bash
npm install leaflet react-leaflet react-leaflet-cluster
npm install -D @types/leaflet
```

## CRÍTICO: CSS imports obligatorios

Sin estos imports el mapa no se verá y los clusters no tendrán estilos:

```ts
// En tu componente o en main.tsx / globals.css
import 'leaflet/dist/leaflet.css'
import 'react-leaflet-cluster/dist/assets/MarkerCluster.css'
import 'react-leaflet-cluster/dist/assets/MarkerCluster.Default.css'
```

## CRÍTICO: Fix de iconos por defecto (Vite/Webpack rompe los íconos de Leaflet)

Siempre incluir este fix antes de usar markers, o los íconos no aparecerán:

```ts
import L from 'leaflet'

// Fix para bundlers (Vite, Webpack, etc.)
delete (L.Icon.Default as any).prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})
```

## CRÍTICO: TypeScript — importar siempre desde el entry point

```ts
// ❌ MAL — sin tipos
import { MapContainer } from 'react-leaflet/MapContainer'

// ✅ BIEN — con tipos
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import { LatLngExpression } from 'leaflet'
```

## Tipos base

```ts
// Coordenada simple
type Coordinate = {
  lat: number
  lng: number
  label?: string       // para el popup
  [key: string]: any   // datos extra del negocio
}

// O usar directamente LatLngExpression de leaflet
import { LatLngExpression } from 'leaflet'
```

## Componente base: Mapa con markers desde lista

```tsx
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import { LatLngExpression } from 'leaflet'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

// Fix de íconos
delete (L.Icon.Default as any).prototype._getIconUrl
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
})

type Location = {
  id: string | number
  lat: number
  lng: number
  name: string
  description?: string
}

type MapProps = {
  locations: Location[]
  center?: LatLngExpression
  zoom?: number
}

export function MapComponent({ locations, center = [-33.45, -70.65], zoom = 12 }: MapProps) {
  return (
    <MapContainer
      center={center}
      zoom={zoom}
      style={{ height: '500px', width: '100%' }}
      scrollWheelZoom={true}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      {locations.map((loc) => (
        <Marker key={loc.id} position={[loc.lat, loc.lng]}>
          <Popup>
            <strong>{loc.name}</strong>
            {loc.description && <p>{loc.description}</p>}
          </Popup>
        </Marker>
      ))}
    </MapContainer>
  )
}
```

## Clustering de markers

Usar cuando hay muchos puntos que se solapan. `chunkedLoading` mejora performance con listas grandes.

```tsx
import MarkerClusterGroup from 'react-leaflet-cluster'
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet'
import 'leaflet/dist/leaflet.css'
import 'react-leaflet-cluster/dist/assets/MarkerCluster.css'
import 'react-leaflet-cluster/dist/assets/MarkerCluster.Default.css'

export function ClusteredMap({ locations }: { locations: Location[] }) {
  return (
    <MapContainer center={[-33.45, -70.65]} zoom={10} style={{ height: '500px', width: '100%' }}>
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <MarkerClusterGroup chunkedLoading>
        {locations.map((loc) => (
          <Marker key={loc.id} position={[loc.lat, loc.lng]}>
            <Popup>{loc.name}</Popup>
          </Marker>
        ))}
      </MarkerClusterGroup>
    </MapContainer>
  )
}
```

## Ícono custom

```tsx
import L from 'leaflet'

const customIcon = new L.Icon({
  iconUrl: '/icons/marker.png',     // o URL pública
  iconSize: [32, 32],               // [ancho, alto] en px
  iconAnchor: [16, 32],             // punto del ícono que coincide con coordenada [x, y]
  popupAnchor: [0, -32],            // offset del popup relativo al iconAnchor
  shadowUrl: '/icons/shadow.png',   // opcional
  shadowSize: [41, 41],
})

// Uso:
<Marker position={[lat, lng]} icon={customIcon}>
  <Popup>...</Popup>
</Marker>
```

## Ícono custom con div (HTML/CSS puro — muy flexible)

```tsx
const divIcon = L.divIcon({
  html: `<div style="
    background: #ef4444;
    width: 24px;
    height: 24px;
    border-radius: 50% 50% 50% 0;
    transform: rotate(-45deg);
    border: 2px solid white;
  "></div>`,
  className: '',          // importante: string vacío para no aplicar estilos default de leaflet
  iconSize: [24, 24],
  iconAnchor: [12, 24],
  popupAnchor: [0, -24],
})
```

## Cluster con ícono custom

```tsx
import { MarkerCluster } from 'leaflet'

// NOTA: iconCreateFunction no soporta arrow functions en Leaflet (usa function regular)
const createClusterIcon = function (cluster: MarkerCluster) {
  return L.divIcon({
    html: `<span>${cluster.getChildCount()}</span>`,
    className: 'custom-cluster-icon',
    iconSize: L.point(40, 40, true),
  })
}

<MarkerClusterGroup iconCreateFunction={createClusterIcon} chunkedLoading>
  {/* markers */}
</MarkerClusterGroup>
```

## Manejo de SSR — Next.js

React Leaflet no es compatible con SSR porque Leaflet accede al DOM directamente. En Next.js usar dynamic import:

```tsx
// components/MapWrapper.tsx — wrapper con 'use client'
'use client'
import dynamic from 'next/dynamic'

const MapComponent = dynamic(
  () => import('./MapComponent'),
  { 
    ssr: false,
    loading: () => <div style={{ height: '500px' }} className="bg-gray-100 animate-pulse" />
  }
)

export default MapComponent
```

El componente del mapa DEBE tener `export default` (no named export) para que funcione con dynamic.

## Centrar el mapa dinámicamente

Para cambiar el centro del mapa desde fuera del componente, usar un componente hijo con el hook `useMap`:

```tsx
import { useMap } from 'react-leaflet'
import { useEffect } from 'react'
import { LatLngExpression } from 'leaflet'

function MapController({ center }: { center: LatLngExpression }) {
  const map = useMap()
  useEffect(() => {
    map.setView(center, map.getZoom())
  }, [center, map])
  return null
}

// Uso dentro de MapContainer:
<MapContainer ...>
  <TileLayer ... />
  <MapController center={selectedLocation} />
  {/* markers */}
</MapContainer>
```

## Tile Layers alternativos (todos open source, sin API key)

```ts
// OpenStreetMap (default, el más común)
url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"

// CartoDB Positron (más limpio, ideal para datos)
url="https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png"

// CartoDB Dark Matter (oscuro)
url="https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png"

// OpenTopoMap (topográfico)
url="https://{s}.tile.opentopomap.org/{z}/{x}/{y}.png"
```

## Performance con muchos markers

- Usar `MarkerClusterGroup` con `chunkedLoading` para >500 markers
- Filtrar markers al viewport actual con `useMapEvents` + bounds
- Evitar re-renders innecesarios memorizando la lista: `useMemo(() => locations, [locations])`

```tsx
import { useMapEvents } from 'react-leaflet'
import { useState } from 'react'
import { LatLngBounds } from 'leaflet'

function BoundsFilter({ locations, onFilter }: { locations: Location[], onFilter: (filtered: Location[]) => void }) {
  const map = useMapEvents({
    moveend: () => {
      const bounds: LatLngBounds = map.getBounds()
      const visible = locations.filter(loc =>
        bounds.contains([loc.lat, loc.lng])
      )
      onFilter(visible)
    }
  })
  return null
}
```

## Gotchas comunes

- **Mapa en blanco**: olvidaste importar `leaflet/dist/leaflet.css`
- **Íconos rotos**: olvidaste el fix `L.Icon.Default.mergeOptions(...)` 
- **Error en Next.js**: el componente no tiene `ssr: false` en el dynamic import
- **`useMap` fuera de MapContainer**: los hooks de react-leaflet solo funcionan dentro de `<MapContainer>`
- **Props inmutables**: `center` y `zoom` de `MapContainer` solo se toman en el primer render; para cambiarlos después usar el hook `useMap`
- **Cluster sin estilos**: olvidaste importar los CSS de `react-leaflet-cluster`
- **`iconCreateFunction` con arrow function**: Leaflet no soporta arrow functions acá, usar `function` regular
