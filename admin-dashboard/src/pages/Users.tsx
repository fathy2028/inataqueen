import { useEffect, useState } from 'react';
import { Typography, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, IconButton, Chip, CircularProgress } from '@mui/material';
import { Delete } from '@mui/icons-material';
import { users as api } from '../services/api';
import type { User } from '../types';
import toast from 'react-hot-toast';
import dayjs from 'dayjs';

export default function Users() {
  const [items, setItems] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);

  const load = () => { setLoading(true); api.getAll(0, 100).then((d) => setItems(d.content)).finally(() => setLoading(false)); };
  useEffect(() => { load(); }, []);

  const remove = async (id: string) => { if (confirm('Delete this user?')) { await api.delete(id); toast.success('Deleted'); load(); } };

  if (loading) return <CircularProgress />;
  return (
    <>
      <Typography variant="h4" fontWeight="bold" mb={3}>Users</Typography>
      <TableContainer component={Paper}><Table>
        <TableHead><TableRow><TableCell>Name</TableCell><TableCell>Email</TableCell><TableCell>Phone</TableCell><TableCell>Role</TableCell><TableCell>Joined</TableCell><TableCell>Actions</TableCell></TableRow></TableHead>
        <TableBody>
          {items.map((u) => <TableRow key={u.id}><TableCell>{u.fullName}</TableCell><TableCell>{u.email}</TableCell><TableCell>{u.phone || '-'}</TableCell>
            <TableCell><Chip label={u.role} color={u.role === 'ADMIN' ? 'primary' : 'default'} size="small" /></TableCell>
            <TableCell>{dayjs(u.createdAt).format('MMM D, YYYY')}</TableCell>
            <TableCell><IconButton color="error" onClick={() => remove(u.id)}><Delete /></IconButton></TableCell></TableRow>)}
        </TableBody>
      </Table></TableContainer>
    </>
  );
}
