import { useState } from 'react';
import { Typography, TextField, Button, Paper, Select, MenuItem, FormControl, InputLabel, Box } from '@mui/material';
import { notifications as api } from '../services/api';
import toast from 'react-hot-toast';

export default function Notifications() {
  const [title, setTitle] = useState('');
  const [body, setBody] = useState('');
  const [type, setType] = useState('GENERAL');
  const [loading, setLoading] = useState(false);

  const send = async () => {
    if (!title || !body) { toast.error('Title and body required'); return; }
    setLoading(true);
    try { await api.send({ title, body, type }); toast.success('Notification sent!'); setTitle(''); setBody(''); } catch { toast.error('Failed to send'); }
    finally { setLoading(false); }
  };

  return (
    <>
      <Typography variant="h4" fontWeight="bold" mb={3}>Send Notification</Typography>
      <Paper sx={{ p: 3, maxWidth: 600 }}>
        <Box display="flex" flexDirection="column" gap={2}>
          <TextField label="Title" value={title} onChange={(e) => setTitle(e.target.value)} required />
          <TextField label="Message" value={body} onChange={(e) => setBody(e.target.value)} multiline rows={4} required />
          <FormControl><InputLabel>Type</InputLabel>
            <Select value={type} onChange={(e) => setType(e.target.value)} label="Type">
              <MenuItem value="GENERAL">General</MenuItem><MenuItem value="OFFER">Offer</MenuItem><MenuItem value="ORDER">Order</MenuItem>
            </Select>
          </FormControl>
          <Button variant="contained" onClick={send} disabled={loading} sx={{ bgcolor: '#1B5E20', alignSelf: 'flex-start' }}>{loading ? 'Sending...' : 'Send to All Customers'}</Button>
        </Box>
      </Paper>
    </>
  );
}
