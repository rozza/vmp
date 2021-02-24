import React from 'react';
import ReactDOM from 'react-dom';
import { ThemeProvider } from '@material-ui/core/styles';
import theme from './components/theme';
import CssBaseline from '@material-ui/core/CssBaseline';
import Container from '@material-ui/core/Container';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import Copyright from './components/Copyright';


ReactDOM.render(
  <ThemeProvider theme={theme}>
    {/* CssBaseline kickstart an elegant, consistent, and simple baseline to build upon. */}
    <CssBaseline />
    <Container maxWidth="md">
      <Box my={4}>
        <Typography variant="h2" component="h1" gutterBottom>
          Visual MongoDB Pro(totype)
        </Typography>
        <Copyright />
      </Box>
    </Container>
  </ThemeProvider>,
  document.querySelector('#root'),
);
