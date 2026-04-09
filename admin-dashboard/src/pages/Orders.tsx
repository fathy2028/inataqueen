import { useEffect, useState } from 'react';
import { Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Select, MenuItem, Chip, Box, CircularProgress, Tabs, Tab } from '@mui/material';
import { orders as api } from '../services/api';
import type { Order } from '../types';
import toast from 'react-hot-toast';
import dayjs from 'dayjs';

const STATUSES = ['', 'PENDING', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED'];
const statusColor = (s: string) => ({ PENDING: 'warning', CONFIRMED: 'info', PREPARING: 'secondary', OUT_FOR_DELIVERY: 'primary', DELIVERED: 'success', CANCELLED: 'error' }[s] || 'default') as any;

export default function Orders() {
  const [items, setItems] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('');

  const load = (status?: string) => { setLoading(true); api.getAll(0, 100, status || undefined).then((d) => setItems(d.content)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, []);

  const updateStatus = async (id: string, status: string) => {
    try { await api.updateStatus(id, status); toast.success('Status updated'); load(filter || undefined); } catch { toast.error('Failed'); }
  };

  return (
    <>
      <Typography variant="h4" fontWeight="bold" mb={3}>Orders</Typography>
      <Tabs value={filter} onChange={(_, v) => { setFilter(v); load(v || undefined); }} sx={{ mb: 2 }}>
        <Tab label="All" value="" />{STATUSES.slice(1).map((s) => <Tab key={s} label={s.replace('_', ' ')} value={s} />)}
      </Tabs>
      {loading ? <CircularProgress /> : (
        <TableContainer component={Paper}><Table>
          <TableHead><TableRow><TableCell>Order #</TableCell><TableCell>Customer</TableCell><TableCell>Total</TableCell><TableCell>Status</TableCell><TableCell>Date</TableCell><TableCell>Update Status</TableCell></TableRow></TableHead>
          <TableBody>
            {items.map((o) => <TableRow key={o.id}>
              <TableCell sx={{ fontFamily: 'monospace' }}>{o.id.slice(0, 8)}</TableCell>
              <TableCell>{o.customerName}</TableCell>
              <TableCell>${o.finalAmount?.toFixed(2)}</TableCell>
              <TableCell><Chip label={o.status} color={statusColor(o.status)} size="small" /></TableCell>
              <TableCell>{dayjs(o.createdAt).format('MMM D, YYYY')}</TableCell>
              <TableCell>
                <Select size="small" value={o.status} onChange={(e) => updateStatus(o.id, e.target.value)} sx={{ minWidth: 150 }}>
                  {STATUSES.slice(1).map((s) => <MenuItem key={s} value={s}>{s.replace('_', ' ')}</MenuItem>)}
                </Select>
              </TableCell>
            </TableRow>)}
          </TableBody>
        </Table></TableContainer>
      )}
    </>
  );
}
