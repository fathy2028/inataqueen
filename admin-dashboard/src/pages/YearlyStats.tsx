import { useEffect, useState } from 'react';
import { Typography, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Select, MenuItem, Box, CircularProgress } from '@mui/material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { statistics } from '../services/api';
import type { YearlyStats as YS } from '../types';

export default function YearlyStats() {
  const [year, setYear] = useState(new Date().getFullYear());
  const [data, setData] = useState<YS | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => { setLoading(true); statistics.getYearly(year).then(setData).finally(() => setLoading(false)); }, [year]);

  const years = Array.from({ length: 5 }, (_, i) => new Date().getFullYear() - i);

  return (
    <>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4" fontWeight="bold">Yearly Statistics</Typography>
        <Select value={year} onChange={(e) => setYear(Number(e.target.value))} size="small">{years.map((y) => <MenuItem key={y} value={y}>{y}</MenuItem>)}</Select>
      </Box>
      {loading ? <CircularProgress /> : data && (
        <>
          <Paper sx={{ p: 3, mb: 3 }}>
            <ResponsiveContainer width="100%" height={400}>
              <BarChart data={data.monthlyEarnings}><CartesianGrid strokeDasharray="3 3" /><XAxis dataKey="monthName" /><YAxis /><Tooltip formatter={(v: number) => `$${v.toFixed(2)}`} /><Bar dataKey="totalEarnings" fill="#1B5E20" /></BarChart>
            </ResponsiveContainer>
          </Paper>
          <Paper sx={{ p: 3 }}>
            <TableContainer><Table>
              <TableHead><TableRow><TableCell>Month</TableCell><TableCell align="right">Earnings</TableCell></TableRow></TableHead>
              <TableBody>
                {data.monthlyEarnings.map((m) => <TableRow key={m.month}><TableCell>{m.monthName}</TableCell><TableCell align="right">${m.totalEarnings?.toFixed(2) || '0.00'}</TableCell></TableRow>)}
                <TableRow><TableCell><strong>Year Total</strong></TableCell><TableCell align="right"><strong>${data.yearTotal?.toFixed(2) || '0.00'}</strong></TableCell></TableRow>
              </TableBody>
            </Table></TableContainer>
          </Paper>
        </>
      )}
    </>
  );
}
