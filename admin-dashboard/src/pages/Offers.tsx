import { useEffect, useState } from 'react';
import { Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Chip, Box, CircularProgress, Select, MenuItem } from '@mui/material';
import { Delete, Edit, Add } from '@mui/icons-material';
import { offers as api, products as prodApi } from '../services/api';
import type { Offer, Product } from '../types';
import toast from 'react-hot-toast';

export default function Offers() {
  const [items, setItems] = useState<Offer[]>([]);
  const [allProducts, setAllProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState<Offer | null>(null);
  const [form, setForm] = useState({ title: '', description: '', bannerImageUrl: '', discountPercentage: '', originalTotal: '', discountedTotal: '', startDate: '', endDate: '' });
  const [offerProducts, setOfferProducts] = useState<{ productId: string; quantity: number }[]>([]);

  const load = () => { setLoading(true); api.getAll().then(setItems).finally(() => setLoading(false)); };
  useEffect(() => { load(); prodApi.getAll(0, 200).then((d) => setAllProducts(d.content)); }, []);

  const openForm = (o?: Offer) => {
    if (o) {
      setEditing(o);
      setForm({ title: o.title, description: o.description || '', bannerImageUrl: o.bannerImageUrl || '', discountPercentage: String(o.discountPercentage), originalTotal: String(o.originalTotal), discountedTotal: String(o.discountedTotal), startDate: o.startDate?.slice(0, 16) || '', endDate: o.endDate?.slice(0, 16) || '' });
      setOfferProducts(o.products.map((p) => ({ productId: p.productId, quantity: p.quantity })));
    } else {
      setEditing(null); setForm({ title: '', description: '', bannerImageUrl: '', discountPercentage: '', originalTotal: '', discountedTotal: '', startDate: '', endDate: '' }); setOfferProducts([]);
    }
    setOpen(true);
  };

  const save = async () => {
    const data = { ...form, discountPercentage: Number(form.discountPercentage), originalTotal: Number(form.originalTotal), discountedTotal: Number(form.discountedTotal), products: offerProducts };
    try { if (editing) await api.update(editing.id, data); else await api.create(data); toast.success('Saved'); setOpen(false); load(); } catch { toast.error('Failed'); }
  };

  const remove = async (id: string) => { if (confirm('Delete?')) { await api.delete(id); toast.success('Deleted'); load(); } };

  const addProduct = () => setOfferProducts([...offerProducts, { productId: '', quantity: 1 }]);
  const removeProduct = (idx: number) => setOfferProducts(offerProducts.filter((_, i) => i !== idx));

  if (loading) return <CircularProgress />;
  return (
    <>
      <Box display="flex" justifyContent="space-between" mb={3}>
        <Typography variant="h4" fontWeight="bold">Offers</Typography>
        <Button variant="contained" startIcon={<Add />} sx={{ bgcolor: '#1B5E20' }} onClick={() => openForm()}>Add Offer</Button>
      </Box>
      <TableContainer component={Paper}><Table>
        <TableHead><TableRow><TableCell>Title</TableCell><TableCell>Discount</TableCell><TableCell>Total</TableCell><TableCell>Period</TableCell><TableCell>Status</TableCell><TableCell>Actions</TableCell></TableRow></TableHead>
        <TableBody>
          {items.map((o) => <TableRow key={o.id}><TableCell>{o.title}</TableCell><TableCell>{o.discountPercentage}%</TableCell><TableCell>${o.originalTotal} → ${o.discountedTotal}</TableCell>
            <TableCell style={{ fontSize: 12 }}>{o.startDate?.slice(0, 10)} - {o.endDate?.slice(0, 10)}</TableCell>
            <TableCell><Chip label={o.isActive ? 'Active' : 'Inactive'} color={o.isActive ? 'success' : 'default'} size="small" /></TableCell>
            <TableCell><IconButton onClick={() => openForm(o)}><Edit /></IconButton><IconButton color="error" onClick={() => remove(o.id)}><Delete /></IconButton></TableCell></TableRow>)}
        </TableBody>
      </Table></TableContainer>
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="md" fullWidth>
        <DialogTitle>{editing ? 'Edit Offer' : 'Add Offer'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '8px !important' }}>
          <TextField label="Title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
          <TextField label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} multiline rows={2} />
          <TextField label="Banner Image URL" value={form.bannerImageUrl} onChange={(e) => setForm({ ...form, bannerImageUrl: e.target.value })} />
          <Box display="flex" gap={2}>
            <TextField label="Discount %" type="number" value={form.discountPercentage} onChange={(e) => setForm({ ...form, discountPercentage: e.target.value })} fullWidth />
            <TextField label="Original Total" type="number" value={form.originalTotal} onChange={(e) => setForm({ ...form, originalTotal: e.target.value })} fullWidth />
            <TextField label="Discounted Total" type="number" value={form.discountedTotal} onChange={(e) => setForm({ ...form, discountedTotal: e.target.value })} fullWidth />
          </Box>
          <Box display="flex" gap={2}>
            <TextField label="Start Date" type="datetime-local" value={form.startDate} onChange={(e) => setForm({ ...form, startDate: e.target.value })} fullWidth slotProps={{ inputLabel: { shrink: true } }} />
            <TextField label="End Date" type="datetime-local" value={form.endDate} onChange={(e) => setForm({ ...form, endDate: e.target.value })} fullWidth slotProps={{ inputLabel: { shrink: true } }} />
          </Box>
          <Typography variant="subtitle1" fontWeight="bold">Products</Typography>
          {offerProducts.map((op, idx) => (
            <Box key={idx} display="flex" gap={2} alignItems="center">
              <Select value={op.productId} onChange={(e) => { const n = [...offerProducts]; n[idx].productId = e.target.value; setOfferProducts(n); }} sx={{ flex: 2 }} displayEmpty>
                <MenuItem value="">Select product</MenuItem>{allProducts.map((p) => <MenuItem key={p.id} value={p.id}>{p.name} - ${p.price}</MenuItem>)}
              </Select>
              <TextField label="Qty" type="number" value={op.quantity} onChange={(e) => { const n = [...offerProducts]; n[idx].quantity = Number(e.target.value); setOfferProducts(n); }} sx={{ width: 100 }} />
              <Button color="error" onClick={() => removeProduct(idx)}>Remove</Button>
            </Box>
          ))}
          <Button onClick={addProduct} variant="outlined">+ Add Product</Button>
        </DialogContent>
        <DialogActions><Button onClick={() => setOpen(false)}>Cancel</Button><Button variant="contained" onClick={save} sx={{ bgcolor: '#1B5E20' }}>Save</Button></DialogActions>
      </Dialog>
    </>
  );
}
