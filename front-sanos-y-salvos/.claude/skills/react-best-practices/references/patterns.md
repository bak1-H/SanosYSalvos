# Patrones avanzados y anti-patrones en React

## Anti-patrones comunes

### 1. Mutar estado directamente
```tsx
// ❌
state.user.name = 'nuevo nombre';
state.items.push(newItem);

// ✅
setState(prev => ({ ...prev, user: { ...prev.user, name: 'nuevo nombre' } }));
setItems(prev => [...prev, newItem]);
```

### 2. useEffect para estado derivado
```tsx
// ❌ Innecesario — useEffect para sincronizar estado con props
const [fullName, setFullName] = useState('');
useEffect(() => {
  setFullName(`${firstName} ${lastName}`);
}, [firstName, lastName]);

// ✅ Calcula directamente
const fullName = `${firstName} ${lastName}`;
// O con useMemo si el cálculo es costoso
```

### 3. Componentes gigantes
```tsx
// ❌ 500 líneas de JSX en un solo componente

// ✅ Divide en sub-componentes:
// UserProfilePage → UserHeader + UserStats + UserPosts + UserSettings
```

### 4. Fetch en useEffect sin abstracción
```tsx
// ❌ Fetch manual en useEffect — sin cache, sin loading state consistente
useEffect(() => {
  fetch('/api/users').then(r => r.json()).then(setUsers);
}, []);

// ✅ Usa React Query / SWR
const { data: users } = useQuery({ queryKey: ['users'], queryFn: fetchUsers });
```

### 5. Anonymous components
```tsx
// ❌ Dificulta debugging en React DevTools
export default () => <div>...</div>;

// ✅
const UserList = () => <div>...</div>;
export default UserList;
```

---

## Patrones avanzados

### Provider pattern
```tsx
// Combina Context + custom hook para API limpia
const AuthContext = createContext<AuthContextType | null>(null);

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  // lógica de auth...
  return <AuthContext.Provider value={{ user, login, logout }}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth must be used within AuthProvider');
  return ctx;
};
```

### HOC (Higher-Order Components) — úsalos con moderación
```tsx
// Todavía útiles para cross-cutting concerns como auth
const withAuth = <P extends object>(Component: React.ComponentType<P>) => {
  return (props: P) => {
    const { user } = useAuth();
    if (!user) return <Navigate to="/login" />;
    return <Component {...props} />;
  };
};
```

### Controlled vs Uncontrolled components
```tsx
// Controlled — React controla el valor
const [value, setValue] = useState('');
<input value={value} onChange={e => setValue(e.target.value)} />

// Uncontrolled — DOM controla el valor (usa refs)
const inputRef = useRef<HTMLInputElement>(null);
<input ref={inputRef} defaultValue="inicial" />
// Accede con: inputRef.current?.value
```

### Suspense para data fetching (React 18+)
```tsx
// Con React Query y Suspense mode
const { data } = useSuspenseQuery({ queryKey: ['user'], queryFn: fetchUser });

// Envuelve en Suspense:
<Suspense fallback={<Skeleton />}>
  <UserProfile />
</Suspense>
```

---

## Stack recomendado 2024-2025

| Categoría          | Recomendación principal       | Alternativa          |
|--------------------|-------------------------------|----------------------|
| Framework          | Next.js 14+ / Vite + React    | Remix                |
| Estilos            | Tailwind CSS                  | CSS Modules          |
| Componentes UI     | shadcn/ui + Radix UI          | MUI, Mantine         |
| Estado global      | Zustand                       | Jotai, Redux Toolkit |
| Fetching           | TanStack Query                | SWR                  |
| Formularios        | react-hook-form + Zod         | Formik               |
| Routing (SPA)      | TanStack Router               | React Router v6      |
| Testing            | Vitest + Testing Library      | Jest                 |
| Animaciones        | Framer Motion                 | react-spring         |
