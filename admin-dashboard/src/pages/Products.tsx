import { useEffect, useState } from 'react';
import { Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Select, MenuItem, Box, CircularProgress } from '@mui/material';
import { Delete, Edit, Add } from '@mui/icons-material';
import { products as api, categories as catApi } from '../services/api';
import type { Product, Category } from '../types';
import toast from 'react-hot-toast';

export default function Products() {
  const [items, setItems] = useState<Product[]>([]);
  const [cats, setCats] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState<Product | null>(null);
  const [form, setForm] = useState({ name: '', description: '', price: '', imageUrl: '', stock: '', categoryId: '' });

  const load = () => { setLoading(true); api.getAll(0, 100).then((d) => setItems(d.content)).finally(() => setLoading(false)); };
  useEffect(() => { load(); catApi.getAll().then(setCats); }, []);

  const openForm = (p?: Product) => {
    if (p) { setEditing(p); setForm({ name: p.name, description: p.description || '', price: String(p.price), imageUrl: p.imageUrl || '', stock: String(p.stock), categoryId: p.category?.id || '' }); }
    else { setEditing(null); setForm({ name: '', description: '', price: '', imageUrl: '', stock: '', categoryId: '' }); }
    setOpen(true);
  };

  const save = async () => {
    const data = { name: form.name, description: form.description, price: Number(form.price), imageUrl: form.imageUrl, stock: Number(form.stock), categoryId: form.categoryId || undefined };
    try {
      if (editing) await api.update(editing.id, data); else await api.create(data);
      toast.success(editing ? 'Product updated' : 'Product created'); setOpen(false); load();
    } catch { toast.error('Failed to save'); }
  };

  const remove = async (id: string) => { if (confirm('Delete this product?')) { await api.delete(id); toast.success('Deleted'); load(); } };

  if (loading) return <CircularProgress />;
  return (
    <>
      <Box display="flex" justifyContent="space-between" mb={3}>
        <Typography variant="h4" fontWeight="bold">Products</Typography>
        <Button variant="contained" startIcon={<Add />} sx={{ bgcolor: '#1B5E20' }} onClick={() => openForm()}>Add Product</Button>
      </Box>
      <TableContainer component={Paper}><Table>
        <TableHead><TableRow><TableCell>Name</TableCell><TableCell>Category</TableCell><TableCell>Price</TableCell><TableCell>Stock</TableCell><TableCell>Actions</TableCell></TableRow></TableHead>
        <TableBody>
          {items.map((p) => <TableRow key={p.id}><TableCell>{p.name}</TableCell><TableCell>{p.category?.name || '-'}</TableCell><TableCell>${p.price}</TableCell><TableCell>{p.stock}</TableCell>
            <TableCell><IconButton onClick={() => openForm(p)}><Edit /></IconButton><IconButton color="error" onClick={() => remove(p.id)}><Delete /></IconButton></TableCell></TableRow>)}
        </TableBody>
      </Table></TableContainer>
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editing ? 'Edit Product' : 'Add Product'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '8px !important' }}>
          <TextField label="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          <TextField label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} multiline rows={2} />
          <TextField label="Price" type="number" value={form.price} onChange={(e) => setForm({ ...form, price: e.target.value })} required />
          <TextField label="Image URL" value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
          <TextField label="Stock" type="number" value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} required />
          <Select value={form.categoryId} onChange={(e) => setForm({ ...form, categoryId: e.target.value })} displayEmpty>
            <MenuItem value="">No Category</MenuItem>{cats.map((c) => <MenuItem key={c.id} value={c.id}>{c.name}</MenuItem>)}
          </Select>
        </DialogContent>
        <DialogActions><Button onClick={() => setOpen(false)}>Cancel</Button><Button variant="contained" onClick={save} sx={{ bgcolor: '#1B5E20' }}>Save</Button></DialogActions>
      </Dialog>
    </>
  );
}
