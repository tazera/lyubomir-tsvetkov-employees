import { useState } from 'react';
import { Container, Box, Typography, CssBaseline, ThemeProvider, createTheme } from '@mui/material';
import FileUploader from './components/FileUploader';
import RankingTable from './components/RankingTable';
import './App.css';

const theme = createTheme({
  palette: {
    primary: {
      main: '#3f51b5',
    },
    secondary: {
      main: '#f50057',
    },
  },
});

function App() {
  const [teamData, setTeamData] = useState([]);

  const handleDataReceived = (data) => {
    setTeamData(data);
  };

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <Container maxWidth="lg">
        <Box sx={{ my: 4 }}>
          <Typography variant="h4" component="h1" gutterBottom align="center">
            Employee Project Pairs
          </Typography>

          <FileUploader onDataReceived={handleDataReceived} />

          {/* Single table layout */}
          <RankingTable teams={teamData} />
        </Box>
      </Container>
    </ThemeProvider>
  );
}

export default App;
