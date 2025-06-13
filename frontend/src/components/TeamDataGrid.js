import React from 'react';
import {
  Paper,
  Typography,
  Box
} from '@mui/material';
import {
  DataGrid
} from '@mui/x-data-grid';

function TeamDataGrid({ teams }) {
  // Define columns for the data grid - removing Project ID as mentioned by the user
  const columns = [
    { field: 'firstEmployeeId', headerName: 'Employee ID #1', flex: 1 },
    { field: 'secondEmployeeId', headerName: 'Employee ID #2', flex: 1 },
    { field: 'daysWorked', headerName: 'Days worked', flex: 1 },
  ];

  // Convert the API data structure to the format expected by DataGrid
  const prepareRows = (teams) => {
    if (!teams || teams.length === 0) {
      return [];
    }

    // Map directly from the Team model structure to our display format
    return teams.map((team, index) => ({
      id: index,
      firstEmployeeId: team.firstEmployeeId,
      secondEmployeeId: team.secondEmployeeId,
      daysWorked: team.totalDuration
    }));
  };

  const rows = prepareRows(teams);

  return (
    <Paper elevation={3} sx={{ p: 3 }}>
      <Typography variant="h5" gutterBottom>
        Employee Pairs with Longest Collaboration Time
      </Typography>

      {rows.length > 0 ? (
        <Box sx={{ height: 400, width: '100%' }}>
          <DataGrid
            rows={rows}
            columns={columns}
            pageSize={5}
            rowsPerPageOptions={[5, 10, 20]}
            disableSelectionOnClick
            density="comfortable"
            sx={{
              '& .MuiDataGrid-cell:focus': {
                outline: 'none',
              }
            }}
          />
        </Box>
      ) : (
        <Typography color="text.secondary">
          No employee pairs found. Upload a CSV file to see results.
        </Typography>
      )}
    </Paper>
  );
}

export default TeamDataGrid;
