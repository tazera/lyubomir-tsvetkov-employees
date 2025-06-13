import React from 'react';
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from '@mui/material';

function RankingTable({ teams }) {
  // Sort teams by total duration in descending order to get ranking
  const sortedTeams = [...(teams || [])].sort((a, b) => b.totalDuration - a.totalDuration);

  return (
    <Paper elevation={3} sx={{ p: 3, mb: 4 }}>
      <Typography variant="h5" gutterBottom>
        Ranked Employee Pairs By Longest Collaboration Time
      </Typography>

      <TableContainer>
        <Table>
          <TableHead>
            <TableRow sx={{ backgroundColor: '#f5f5f5' }}>
              <TableCell sx={{ fontWeight: 'bold' }}>Place</TableCell>
              <TableCell sx={{ fontWeight: 'bold' }}>Employee ID #1</TableCell>
              <TableCell sx={{ fontWeight: 'bold' }}>Employee ID #2</TableCell>
              <TableCell sx={{ fontWeight: 'bold' }}>Days worked</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {sortedTeams.length > 0 ? (
              sortedTeams.map((team, index) => (
                <TableRow
                  key={index}
                  sx={index === 0 ? { backgroundColor: '#f0f7ff' } : {}} // Highlight the top pair
                >
                  <TableCell>{index + 1}</TableCell>
                  <TableCell>{team.firstEmployeeId}</TableCell>
                  <TableCell>{team.secondEmployeeId}</TableCell>
                  <TableCell>{team.totalDuration}</TableCell>
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={4} align="center">No data available</TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </TableContainer>
    </Paper>
  );
}

export default RankingTable;
