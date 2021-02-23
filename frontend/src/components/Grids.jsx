import React from 'react';
import Container from '@material-ui/core/Container';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import { makeStyles } from '@material-ui/core/styles';
import CustomForm from './Forms';
import Copyright from './Copyright';

const useStyles = makeStyles((theme) => ({
  paper: {
    padding: theme.spacing(2),
    textAlign: 'center',
    color: theme.palette.text.primary,
  },
}));

export default function CenteredGrid() {
  const classes = useStyles();

  return (
  <React.Fragment>
    <Container component="div" maxWidth="md">
      <CssBaseline />
      <div className={classes.paper}>
        <Grid container
         direction="row"
         justify="center"
         alignItems="center">
         <Grid item xs={12}>
          <Typography component="h1" variant="h5">
            Visual MongoDB Pro(totype)!
          </Typography>
         </Grid>
         <Grid item xs={12}>
          <CustomForm />
         </Grid>
        </Grid>
      </div>
      <Box mt={8}>
        <Copyright />
      </Box>
    </Container>
   </React.Fragment>
  );

}
