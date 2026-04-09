import { Routes, Route, Navigate } from 'react-router-dom';
import { CssBaseline } from '@mui/material';
import { useAuth } from './context/AuthContext';
import Layout from './components/Layout';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import YearlyStats from './pages/YearlyStats';
import Products from './pages/Products';
import Categories from './pages/Categories';
import Coupons from './pages/Coupons';
import Offers from './pages/Offers';
import Orders from './pages/Orders';
import Users from './pages/Users';
import Notifications from './pages/Notifications';

export default function App() {
  const { isAuthenticated } = useAuth();
  return (
    <>
      <CssBaseline />
      <Routes>
        <Route path="/login" element={isAuthenticated ? <Navigate to="/" /> : <Login />} />
        <Route element={isAuthenticated ? <Layout /> : <Navigate to="/login" />}>
          <Route path="/" element={<Dashboard />} />
          <Route path="/products" element={<Products />} />
          <Route path="/categories" element={<Categories />} />
          <Route path="/coupons" element={<Coupons />} />
          <Route path="/offers" element={<Offers />} />
          <Route path="/orders" element={<Orders />} />
          <Route path="/users" element={<Users />} />
          <Route path="/notifications" element={<Notifications />} />
          <Route path="/yearly-stats" element={<YearlyStats />} />
        </Route>
      </Routes>
    </>
  );
}
