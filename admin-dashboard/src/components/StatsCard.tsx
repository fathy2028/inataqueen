import { Card, CardContent, Typography, Box } from '@mui/material';
import type { ReactNode } from 'react';

export default function StatsCard({ title, value, icon, color }: { title: string; value: string | number; icon: ReactNode; color: string }) {
  return (
    <Card sx={{ height: '100%' }}>
      <CardContent sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
        <Box sx={{ bgcolor: color, borderRadius: 2, p: 1.5, display: 'flex', color: '#fff' }}>{icon}</Box>
        <Box>
          <Typography variant="body2" color="text.secondary">{title}</Typography>
          <Typography variant="h5" fontWeight="bold">{value}</Typography>
        </Box>
      </CardContent>
    </Card>
  );
}
