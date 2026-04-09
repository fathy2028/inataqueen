import { Drawer, List, ListItemButton, ListItemIcon, ListItemText, Toolbar, Typography, Box } from '@mui/material';
import { Dashboard, Inventory, Category, LocalOffer, Campaign, ShoppingCart, People, Notifications, BarChart } from '@mui/icons-material';
import { useNavigate, useLocation } from 'react-router-dom';

const DRAWER_WIDTH = 260;
const items = [
  { text: 'Dashboard', icon: <Dashboard />, path: '/' },
  { text: 'Products', icon: <Inventory />, path: '/products' },
  { text: 'Categories', icon: <Category />, path: '/categories' },
  { text: 'Coupons', icon: <LocalOffer />, path: '/coupons' },
  { text: 'Offers', icon: <Campaign />, path: '/offers' },
  { text: 'Orders', icon: <ShoppingCart />, path: '/orders' },
  { text: 'Users', icon: <People />, path: '/users' },
  { text: 'Notifications', icon: <Notifications />, path: '/notifications' },
  { text: 'Yearly Stats', icon: <BarChart />, path: '/yearly-stats' },
];

export default function Sidebar() {
  const navigate = useNavigate();
  const location = useLocation();
  return (
    <Drawer variant="permanent" sx={{ width: DRAWER_WIDTH, '& .MuiDrawer-paper': { width: DRAWER_WIDTH, bgcolor: '#1B5E20', color: '#fff' } }}>
      <Toolbar>
        <Box sx={{ textAlign: 'center', width: '100%', py: 2 }}>
          <Typography variant="h6" fontWeight="bold">Queen Pharmacy</Typography>
          <Typography variant="caption" sx={{ opacity: 0.7 }}>Admin Panel</Typography>
        </Box>
      </Toolbar>
      <List>
        {items.map((item) => (
          <ListItemButton key={item.path} selected={location.pathname === item.path} onClick={() => navigate(item.path)}
            sx={{ '&.Mui-selected': { bgcolor: 'rgba(255,255,255,0.15)' }, '&:hover': { bgcolor: 'rgba(255,255,255,0.1)' }, color: '#fff' }}>
            <ListItemIcon sx={{ color: '#fff', minWidth: 40 }}>{item.icon}</ListItemIcon>
            <ListItemText primary={item.text} />
          </ListItemButton>
        ))}
      </List>
    </Drawer>
  );
}
