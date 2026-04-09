import { useEffect, useState } from 'react';
import { Typography, Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Dialog, DialogTitle, DialogContent, DialogActions, TextField, Box, CircularProgress } from '@mui/material';
import { Delete, Edit, Add } from '@mui/icons-material';
import { categories as api } from '../services/api';
import type { Category } from '../types';
import toast from 'react-hot-toast';

export default function Categories() {
  const [items, setItems] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [editing, setEditing] = useState<Category | null>(null);
  const [form, setForm] = useState({ name: '', description: '', imageUrl: '' });

  const load = () => { setLoading(true); api.getAll().then(setItems).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, []);

  const openForm = (c?: Category) => {
    if (c) { setEditing(c); setForm({ name: c.name, description: c.description || '', imageUrl: c.imageUrl || '' }); }
    else { setEditing(null); setForm({ name: '', description: '', imageUrl: '' }); }
    setOpen(true);
  };

  const save = async () => {
    try {
      if (editing) await api.update(editing.id, form); else await api.create(form);
      toast.success(editing ? 'Updated' : 'Created'); setOpen(false); load();
    } catch { toast.error('Failed'); }
  };

  const remove = async (id: string) => { if (confirm('Delete this category? All related products will also be deleted.')) { await api.delete(id); toast.success('Deleted'); load(); } };

  if (loading) return <CircularProgress />;
  return (
    <>
      <Box display="flex" justifyContent="space-between" mb={3}>
        <Typography variant="h4" fontWeight="bold">Categories</Typography>
        <Button variant="contained" startIcon={<Add />} sx={{ bgcolor: '#1B5E20' }} onClick={() => openForm()}>Add Category</Button>
      </Box>
      <TableContainer component={Paper}><Table>
        <TableHead><TableRow><TableCell>Name</TableCell><TableCell>Description</TableCell><TableCell>Actions</TableCell></TableRow></TableHead>
        <TableBody>
          {items.map((c) => <TableRow key={c.id}><TableCell>{c.name}</TableCell><TableCell>{c.description || '-'}</TableCell>
            <TableCell><IconButton onClick={() => openForm(c)}><Edit /></IconButton><IconButton color="error" onClick={() => remove(c.id)}><Delete /></IconButton></TableCell></TableRow>)}
        </TableBody>
      </Table></TableContainer>
      <Dialog open={open} onClose={() => setOpen(false)} maxWidth="sm" fullWidth>
        <DialogTitle>{editing ? 'Edit Category' : 'Add Category'}</DialogTitle>
        <DialogContent sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: '8px !important' }}>
          <TextField label="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} required />
          <TextField label="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} multiline rows={2} />
          <TextField label="Image URL" value={form.imageUrl} onChange={(e) => setForm({ ...form, imageUrl: e.target.value })} />
        </DialogContent>
        <DialogActions><Button onClick={() => setOpen(false)}>Cancel</Button><Button variant="contained" onClick={save} sx={{ bgcolor: '#1B5E20' }}>Save</Button></DialogActions>
      </Dialog>
    </>
  );
}
