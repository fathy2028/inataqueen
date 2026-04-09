import { useState } from 'react';
import { Box, Card, CardContent, TextField, Button, Typography } from '@mui/material';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import toast from 'react-hot-toast';

export default function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    try { await login(email, password); navigate('/'); } catch { toast.error('Invalid credentials'); }
    finally { setLoading(false); }
  };

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center', bgcolor: '#1B5E20' }}>
      <Card sx={{ maxWidth: 400, width: '100%', mx: 2 }}>
        <CardContent sx={{ p: 4 }}>
          <Typography variant="h4" textAlign="center" fontWeight="bold" color="#1B5E20" gutterBottom>Queen Pharmacy</Typography>
          <Typography variant="body2" textAlign="center" color="text.secondary" mb={3}>Admin Dashboard</Typography>
          <form onSubmit={handleSubmit}>
            <TextField fullWidth label="Email" type="email" value={email} onChange={(e) => setEmail(e.target.value)} margin="normal" required />
            <TextField fullWidth label="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} margin="normal" required />
            <Button fullWidth type="submit" variant="contained" sx={{ mt: 2, bgcolor: '#1B5E20', py: 1.5 }} disabled={loading}>{loading ? 'Signing in...' : 'Sign In'}</Button>
          </form>
        </CardContent>
      </Card>
    </Box>
  );
}
