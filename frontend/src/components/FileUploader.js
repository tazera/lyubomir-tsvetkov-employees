import React, { useState } from 'react';
import { useDropzone } from 'react-dropzone';
import {
  Box,
  Paper,
  Typography,
  Button,
  CircularProgress
} from '@mui/material';
import axios from 'axios';

function FileUploader({ onDataReceived }) {
  const [isUploading, setIsUploading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');
  const [selectedFile, setSelectedFile] = useState(null);

  const { getRootProps, getInputProps, open } = useDropzone({
    accept: {
      'text/csv': ['.csv']
    },
    multiple: false,
    noClick: true,
    noKeyboard: true,
    onDrop: acceptedFiles => {
      if (acceptedFiles.length > 0) {
        const file = acceptedFiles[0];
        setSelectedFile(file);
        uploadFile(file);
      }
    }
  });

  const uploadFile = async (file) => {
    setIsUploading(true);
    setErrorMessage('');
    console.log('Uploading file:', file.name);

    const formData = new FormData();
    formData.append('file', file);

    try {
      console.log('Sending request to /api/test/teams');
      const response = await axios.post('/api/test/teams', formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });

      console.log('Response received:', response);
      if (response.data) {
        onDataReceived(response.data);
      }
    } catch (error) {
      console.error('Error uploading file:', error);
      let message = 'Error uploading file. ';

      if (error.response) {
        message += `Server returned status ${error.response.status}: ${error.response.statusText}`;
        console.error('Error response data:', error.response.data);
      } else if (error.request) {
        message += 'No response received from server.';
      } else {
        message += error.message;
      }

      setErrorMessage(message);
    } finally {
      setIsUploading(false);
    }
  };

  const handleButtonClick = (e) => {
    e.stopPropagation();
    open();
  };

  return (
    <Paper elevation={3} sx={{ p: 3, mb: 4 }}>
      <Typography variant="h5" gutterBottom>
        Upload CSV File
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        Upload a CSV file to see common projects between employee pairs
      </Typography>

      <Box
        {...getRootProps()}
        sx={{
          border: '2px dashed #cccccc',
          borderRadius: 2,
          p: 3,
          textAlign: 'center',
          cursor: 'pointer',
          bgcolor: '#f8f8f8',
          '&:hover': {
            bgcolor: '#f0f0f0'
          }
        }}
      >
        <input {...getInputProps()} />
        {isUploading ? (
          <CircularProgress size={24} />
        ) : (
          <>
            <Typography>
              {selectedFile ? `Selected: ${selectedFile.name}` : 'Drag and drop a CSV file here, or click the button below to select a file'}
            </Typography>
            <Button
              variant="contained"
              sx={{ mt: 2 }}
              onClick={handleButtonClick}
            >
              Select File
            </Button>
          </>
        )}
      </Box>

      {errorMessage && (
        <Typography color="error" sx={{ mt: 2 }}>
          {errorMessage}
        </Typography>
      )}
    </Paper>
  );
}

export default FileUploader;
