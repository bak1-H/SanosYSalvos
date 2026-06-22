---
name: react-best-practices
description: >
  Mejores prácticas, patrones y convenciones para desarrollar aplicaciones React modernas
  de alta calidad. Usa esta skill siempre que el usuario esté trabajando con React —
  ya sea creando componentes, manejando estado, estructurando un proyecto, optimizando
  rendimiento, o revisando/refactorizando código existente. Activa también cuando el usuario
  pregunte sobre hooks, Context, lifting state, props drilling, re-renders, code splitting,
  formularios en React, o cualquier patrón de arquitectura frontend con React.
  Combínala con la skill web-design para proyectos con foco visual.
---

# React Best Practices

Guía de mejores prácticas para React moderno (v18+). El objetivo no es solo que el código
funcione, sino que sea mantenible, predecible y performante.

---

## 1. Estructura de proyecto

### Organización recomendada (feature-based)
```
src/
├── components/         # Componentes reutilizables globales
│   ├── ui/             # Primitivas: Button, Input, Modal, etc.
│   └── layout/         # Header, Sidebar, Footer, etc.
├── features/           # Módulos por dominio
│   ├── auth/
│   │   ├── components/
│   │   ├── hooks/
│   │   └── index.ts    # Barrel export
│   └── dashboard/
├── hooks/              # Hooks globales reutilizables
├── lib/                # Utilidades, helpers, configuración
├── pages/ (o routes/)  # Componentes de ruta/página
├── store/              # Estado global (Zustand, Redux, etc.)
├── types/              # TypeScript types e interfaces
└── App.tsx
```

### Reglas de organización
- **Co-location**: mantén los archivos relacionados juntos. Tests, estilos y hooks específicos de un componente van en la misma carpeta.
- **Barrel exports** (`index.ts`): facilitan imports limpios. `import { Button } from '@/components/ui'`
- **Evita** estructuras tipo `components/`, `hooks/`, `utils/` a nivel global para todo — no escala.

---

## 2. Componentes

### Functional components siempre
```tsx
// ✅ Correcto
const UserCard = ({ name, email }: UserCardProps) => {
  return <div>...</div>;
};

// ❌ Evitar class components en código nuevo
```

### Props tipadas con TypeScript
```tsx
// ✅ Interface para props de componente
interface ButtonProps {
  label: string;
  variant?: 'primary' | 'secondary' | 'ghost';
  onClick?: () => void;
  disabled?: boolean;
  children?: React.ReactNode;
}

const Button = ({ label, variant = 'primary', onClick, disabled }: ButtonProps) => {
  // ...
};
```

### Single Responsibility
- Un componente debe hacer una sola cosa bien.
- Si un componente supera ~150-200 líneas, considera dividirlo.
- Separa lógica (hooks) de presentación (JSX).

### Nombrado
- Componentes: **PascalCase** → `UserProfile`, `LoginForm`
- Hooks: **camelCase con prefijo use** → `useAuth`, `useFetchUsers`
- Handlers: **prefijo handle** → `handleSubmit`, `handleInputChange`
- Props booleanas: **prefijo is/has/can** → `isLoading`, `hasError`, `canEdit`

---

## 3. Hooks

### Reglas de hooks (obligatorias)
- Solo en el top level — nunca dentro de condicionales o loops.
- Solo en componentes funcionales o custom hooks.

### useState
```tsx
// ✅ Inicialización lazy para computaciones costosas
const [data, setData] = useState(() => expensiveComputation());

// ✅ Actualización funcional cuando el nuevo estado depende del anterior
setCount(prev => prev + 1);

// ❌ Nunca mutar el estado directamente
state.items.push(newItem); // MAL
setItems(prev => [...prev, newItem]); // BIEN
```

### useEffect
```tsx
// ✅ Siempre incluir dependencias correctas
useEffect(() => {
  fetchUser(userId);
}, [userId]); // userId en deps

// ✅ Cleanup cuando corresponde
useEffect(() => {
  const subscription = subscribe(topic);
  return () => subscription.unsubscribe(); // cleanup
}, [topic]);

// ⚠️ Evitar useEffect para transformaciones de datos derivados
// Usa useMemo o calcula directamente en el render
```

### Custom hooks — cuándo crearlos
- Lógica reutilizable entre componentes.
- Separar side effects complejos del componente.
- Abstraer acceso a APIs, localStorage, suscripciones.

```tsx
// ✅ Custom hook bien estructurado
const useLocalStorage = <T>(key: string, initialValue: T) => {
  const [storedValue, setStoredValue] = useState<T>(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue;
    } catch {
      return initialValue;
    }
  });

  const setValue = (value: T) => {
    setStoredValue(value);
    window.localStorage.setItem(key, JSON.stringify(value));
  };

  return [storedValue, setValue] as const;
};
```

---

## 4. Manejo de estado

### Regla de oro: estado lo más local posible
1. **useState local** — para UI state de un componente.
2. **Lifting state** — cuando dos componentes hermanos necesitan compartirlo.
3. **Context** — para estado que muchos componentes leen pero rara vez cambia (tema, usuario autenticado, idioma).
4. **Estado global (Zustand/Redux)** — para estado complejo con muchas actualizaciones o accesible desde cualquier parte.

### Context: úsalo con cuidado
```tsx
// ✅ Context para datos que cambian poco
const ThemeContext = createContext<Theme>('light');

// ❌ No uses Context para estado que cambia frecuentemente
// → causa re-renders en todos los consumidores
```

### Zustand (recomendado para estado global)
```tsx
const useUserStore = create<UserStore>((set) => ({
  user: null,
  isLoading: false,
  setUser: (user) => set({ user }),
  logout: () => set({ user: null }),
}));
```

---

## 5. Rendimiento

### Cuándo usar memo, useMemo y useCallback
- **React.memo**: cuando un componente se re-renderiza seguido con las mismas props.
- **useMemo**: para cómputos costosos que dependen de ciertas dependencias.
- **useCallback**: para funciones que se pasan como props a componentes memorizados.

```tsx
// ✅ useMemo para filtrado/transformación costosa
const filteredItems = useMemo(
  () => items.filter(item => item.active && item.name.includes(search)),
  [items, search]
);

// ✅ useCallback para handler pasado a componente memoizado
const handleDelete = useCallback((id: string) => {
  setItems(prev => prev.filter(item => item.id !== id));
}, []); // sin deps si no usa estado externo
```

⚠️ **No optimizes prematuramente.** Medir primero con React DevTools Profiler.

### Code splitting y lazy loading
```tsx
// ✅ Lazy load de páginas/rutas
const Dashboard = lazy(() => import('./pages/Dashboard'));

// En el router:
<Suspense fallback={<PageLoader />}>
  <Dashboard />
</Suspense>
```

### Listas largas
- Usa **virtualización** para listas de +100 elementos: `react-window` o `TanStack Virtual`.
- Siempre incluye `key` estable y única (nunca el índice del array si la lista puede reordenarse).

---

## 6. Formularios

### Usa react-hook-form para formularios complejos
```tsx
const { register, handleSubmit, formState: { errors } } = useForm<FormData>({
  resolver: zodResolver(schema), // validación con Zod
});

const onSubmit = (data: FormData) => { /* ... */ };

return (
  <form onSubmit={handleSubmit(onSubmit)}>
    <input {...register('email')} />
    {errors.email && <span>{errors.email.message}</span>}
  </form>
);
```

### Validación con Zod
```tsx
const schema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(8, 'Mínimo 8 caracteres'),
});
type FormData = z.infer<typeof schema>;
```

---

## 7. Fetching de datos

### TanStack Query (React Query) — estándar actual
```tsx
// ✅ Query para fetch
const { data, isLoading, error } = useQuery({
  queryKey: ['users', userId],
  queryFn: () => fetchUser(userId),
  staleTime: 5 * 60 * 1000, // 5 minutos
});

// ✅ Mutation para POST/PUT/DELETE
const mutation = useMutation({
  mutationFn: createUser,
  onSuccess: () => queryClient.invalidateQueries({ queryKey: ['users'] }),
});
```

---

## 8. Patrones importantes

### Composition over inheritance
```tsx
// ✅ Patrón de composición con children
const Card = ({ children, className }: CardProps) => (
  <div className={cn('rounded-lg border p-4', className)}>{children}</div>
);

const Card.Header = ({ children }: { children: React.ReactNode }) => (
  <div className="font-semibold mb-2">{children}</div>
);
```

### Render props y compound components
```tsx
// ✅ Compound components para UI complejas
<Select>
  <Select.Trigger>Seleccionar</Select.Trigger>
  <Select.Content>
    <Select.Item value="a">Opción A</Select.Item>
    <Select.Item value="b">Opción B</Select.Item>
  </Select.Content>
</Select>
```

### Error boundaries
```tsx
// Siempre envuelve rutas o secciones críticas con error boundary
<ErrorBoundary fallback={<ErrorPage />}>
  <Dashboard />
</ErrorBoundary>
```

---

## 9. TypeScript en React

### Tipos útiles de React
```tsx
React.ReactNode        // Cualquier cosa que React puede renderizar
React.ReactElement     // JSX específicamente
React.FC<Props>        // Evitar — no agrega valor y quita flexibilidad
React.MouseEvent<HTMLButtonElement>
React.ChangeEvent<HTMLInputElement>
React.ComponentProps<typeof Button>  // Inferir props de un componente existente
```

### Discriminated unions para estados
```tsx
type AsyncState<T> =
  | { status: 'idle' }
  | { status: 'loading' }
  | { status: 'success'; data: T }
  | { status: 'error'; error: string };
```

---

## 10. Checklist de calidad React

Antes de dar por terminado un componente o feature:

- [ ] ¿Props tienen tipos TypeScript completos?
- [ ] ¿Se evita prop drilling de más de 2 niveles?
- [ ] ¿Los useEffect tienen cleanup si corresponde?
- [ ] ¿Las keys en listas son únicas y estables?
- [ ] ¿Hay manejo de estados loading/error/empty?
- [ ] ¿Los componentes son pequeños y tienen responsabilidad única?
- [ ] ¿Se usa React.memo/useMemo/useCallback solo donde es necesario?
- [ ] ¿El código es legible sin comentarios innecesarios?
- [ ] ¿Las rutas tienen lazy loading?

---

## Referencias adicionales

- Ver `references/patterns.md` para patrones avanzados y anti-patrones comunes.
- Para estilos y diseño visual, ver skill `web-design`.
