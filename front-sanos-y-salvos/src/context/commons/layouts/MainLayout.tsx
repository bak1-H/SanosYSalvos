import { Outlet } from 'react-router';
import { Navbar } from '../components/navbar/Navbar.tsx';

export function MainLayout() {
  return (
    <>
      <Navbar />
      <Outlet />
    </>
  );
}
