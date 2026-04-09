import { useEffect, useState } from 'react';
import { Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Chip, Box, CircularProgress } from '@mui/material';
import { Delete, Edit, Add } from '@mui/icons-material';
import { coupons as api } from '../services/api';
import type { Coupon } from '../types';
import toast from 'react-hot-toast';

export default function Coupons() {
  const [items, setItems] = useState<Coupon[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState<Coupon | null>(null);
  const [form, setForm] = useState({ code: '', description: '', discountPercentage: '', maxDiscount: '', minOrderAmount: '', expiryDate: '', usageLimit: '' });

  const load = () => { setLoading(true); api.getAll().then(setItems).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, []);

  const openForm = (c?: Coupon) => {
    if (c) { setEditing(c); setForm({ code: c.code, description: c.description || '', discountPercentage: String(c.discountPercentage), maxDiscount: String(c.maxDiscount || ''), minOrderAmount: String(c.minOrderAmount || ''), expiryDate: c.expiryDate?.slice(0, 16) || '', usageLimit: String(c.usageLimit) }); }
    else { setEditing(null); setForm({ code: '', description: '', discountPercentage: '', maxDiscount: '', minOrderAmount: '', expiryDate: '', usageLimit: '100' }); }
    setOpen(true);
  };

  const save = async () => {
    const data = { code: form.code, description: form.description, discountPercentage: Number(form.discountPercentage), maxDiscount: form.maxDiscount ? Number(form.maxDiscount) : undefined, minOrderAmount: form.minOrderAmount ? Number(form.minOrderAmount) : undefined, expiryDate: form.expiryDate || undefined, usageLimit: Number(form.usageLimit) };
    try { if (editing) await api.update(editing.id, data); else await api.create(data); toast.success('Saved'); setOpen(false); load(); } catch { toast.error('Failed'); }
  };

  const remove = async (id: string) => { if (confirm('Delete?')) { await api.delete(id); toast.success('Deleted'); load(); } };

  if (loading) return <CircularProgress />;
  return (
    <>
      <Box display="flex" justifyContent="space-between" mb={3}>
        <Typography variant="h4" fontWeight="bold">Coupons</Typography>
        <Button variant="contained" startIcon={<Add />} sx={{ bgcolor: '#1B5E20' }} onClick={() => openForm()}>Add Coupon</Button>
      </Box>
      <TableContainer component={Paper}><Table>
        <TableHead><TableRow><TableCell>Code</TableCell><TableCell>Discount</TableCell><TableCell>Max</TableCell><TableCell>Min Order</TableCell><TableCell>Usage</TableCell><TableCell>Status</TableCell><TableCell>Actions</TableCell></TableRow></TableHead>
        <TableBody>
          {items.map((c) => <TableRow key={c.id}><TableCell><strong>{c.code}</strong></TableCell><TableCell>{c.discountPercentage}%</TableCell><TableCell>${c.maxDiscount || '-'}</TableCell><TableCell>${c.minOrderAmount || '-'}</TableCell>
            <TableCell>{c.usedCount}/{c.usageLimit}</TableCell><TableCell><Chip label={c.isActive ? 'Active' : 'Inactive'} color={c.isActive ? 'success' : 'default'} size="small" /></TableCell>
            <TableCell><IconButton onClick={() => openForm(c)}><Edit /></IconButton><IconButton color="error" onClick={() => remove(c.id)}><Delete /></IconButton></TableCell></TableRow>)}
        </TableBody>
      </Table></TableContainer>
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editing ? 'Edit Coupon' : 'Add Coupon'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '8px !important' }}>
          <TextField label="Code" value={form.code} onChange={(e) => setForm({ ...form, code: e.target.value })} required />
          <TextField label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
          <TextField label="Discount %" type="number" value={form.discountPercentage} onChange={(e) => setForm({ ...form, discountPercentage: e.target.value })} required />
          <TextField label="Max Discount ($)" type="number" value={form.maxDiscount} onChange={(e) => setForm({ ...form, maxDiscount: e.target.value })} />
          <TextField label="Min Order ($)" type="number" value={form.minOrderAmount} onChange={(e) => setForm({ ...form, minOrderAmount: e.target.value })} />
          <TextField label="Expiry Date" type="datetime-local" value={form.expiryDate} onChange={(e) => setForm({ ...form, expiryDate: e.target.value })} slotProps={{ inputLabel: { shrink: true } }} />
          <TextField label="Usage Limit" type="number" value={form.usageLimit} onChange={(e) => setForm({ ...form, usageLimit: e.target.value })} />
        </DialogContent>
        <DialogActions><Button onClick={() => setOpen(false)}>Cancel</Button><Button variant="contained" onClick={save} sx={{ bgcolor: '#1B5E20' }}>Save</Button></DialogActions>
      </Dialog>
    </>
  );
}
