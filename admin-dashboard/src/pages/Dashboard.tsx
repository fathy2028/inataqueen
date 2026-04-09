import { useEffect, useState } from 'react';
import { Grid, Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Chip, CircularProgress, Box } from '@mui/material';
import { People, Inventory, Campaign, LocalOffer, AttachMoney } from '@mui/icons-material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import StatsCard from '../components/StatsCard';
import { statistics } from '../services/api';
import type { DashboardStats } from '../types';

export default function Dashboard() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => { statistics.getDashboard().then(setStats).finally(() => setLoading(false)); }, []);

  if (loading) return <Box display="flex" justifyContent="center" p={4}><CircularProgress /></Box>;
  if (!stats) return <Typography>Failed to load</Typography>;

  return (
    <>
      <Typography variant="h4" fontWeight="bold" mb={3}>Dashboard</Typography>
      <Grid container spacing={3} mb={3}>
        <Grid size={{ xs: 12, sm: 6, md: 3 }}><StatsCard title="Total Customers" value={stats.totalCustomers} icon={<People />} color="#1976d2" /></Grid>
        <Grid size={{ xs: 12, sm: 6, md: 3 }}><StatsCard title="Total Products" value={stats.totalProducts} icon={<Inventory />} color="#2e7d32" /></Grid>
        <Grid size={{ xs: 12, sm: 6, md: 3 }}><StatsCard title="Total Offers" value={stats.totalOffers} icon={<Campaign />} color="#ed6c02" /></Grid>
        <Grid size={{ xs: 12, sm: 6, md: 3 }}><StatsCard title="Total Coupons" value={stats.totalCoupons} icon={<LocalOffer />} color="#9c27b0" /></Grid>
      </Grid>
      <Grid container spacing={3} mb={3}>
        <Grid size={{ xs: 12, sm: 6 }}><StatsCard title="Today's Earnings" value={`$${stats.todayEarnings?.toFixed(2) || '0.00'}`} icon={<AttachMoney />} color="#2e7d32" /></Grid>
        <Grid size={{ xs: 12, sm: 6 }}><StatsCard title="This Month's Earnings" value={`$${stats.monthEarnings?.toFixed(2) || '0.00'}`} icon={<AttachMoney />} color="#1976d2" /></Grid>
      </Grid>

      {stats.topSellingProducts?.length > 0 && (
        <Paper sx={{ p: 3, mb: 3 }}>
          <Typography variant="h6" fontWeight="bold" mb={2}>Top Selling Products</Typography>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={stats.topSellingProducts}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="productName" /><YAxis /><Tooltip /><Bar dataKey="totalSold" fill="#1B5E20" /></BarChart>
          </ResponsiveContainer>
        </Paper>
      )}

      <Paper sx={{ p: 3, mb: 3 }}>
        <Typography variant="h6" fontWeight="bold" mb={2}>Low Stock Products (&le;2)</Typography>
        <TableContainer><Table size="small">
          <TableHead><TableRow><TableCell>Product</TableCell><TableCell>Stock</TableCell><TableCell>Price</TableCell></TableRow></TableHead>
          <TableBody>
            {stats.lowStockProducts?.map((p) => (
              <TableRow key={p.id}><TableCell>{p.name}</TableCell><TableCell><Chip label={p.stock} color={p.stock === 0 ? 'error' : 'warning'} size="small" /></TableCell><TableCell>${p.price}</TableCell></TableRow>
            ))}
            {(!stats.lowStockProducts || stats.lowStockProducts.length === 0) && <TableRow><TableCell colSpan={3} align="center">All products are well stocked</TableCell></TableRow>}
          </TableBody>
        </Table></TableContainer>
      </Paper>
    </>
  );
}
