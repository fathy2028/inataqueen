import { Box, AppBar, Toolbar, Typography, Button } from '@mui/material';
import { Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Sidebar from './Sidebar';

export default function Layout() {
  const { logout } = useAuth();
  return (
    <Box sx={{ display: 'flex' }}>
      <Sidebar />
      <Box sx={{ flexGrow: 1, minHeight: '100vh', bgcolor: '#f5f5f5' }}>
        <AppBar position="static" sx={{ bgcolor: '#fff', color: '#333', boxShadow: 1 }}>
          <Toolbar sx={{ justifyContent: 'flex-end' }}>
            <Typography sx={{ mr: 2 }}>Admin</Typography>
            <Button color="error" onClick={() => { logout(); window.location.href = '/login'; }}>Logout</Button>
          </Toolbar>
        </AppBar>
        <Box sx={{ p: 3 }}><Outlet /></Box>
      </Box>
    </Box>
  );
}
