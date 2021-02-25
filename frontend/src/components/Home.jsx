import React, { Suspense } from 'react';
import clsx from 'clsx';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Container from '@material-ui/core/Container';
import { withStyles } from "@material-ui/core/styles";

import { styles } from './styles';
import Loader from './Loader';


export default function Home() {

  const classes = withStyles(styles);
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  return (
  <Container maxWidth="xl" className={classes.container}>
  <Grid container spacing={3}>
      <Suspense fallback={<Loader />}>
       <Grid item xs={12}>
          <Paper>
            <h4 className={classes.padded}>HOME PAGE COMING SOON....</h4>
          </Paper>
        </Grid>
      {/* Chart */}
      <Grid item xs={12} md={8} lg={9}>
        <Paper className={fixedHeightPaper}>

        </Paper>
      </Grid>
      {/* Recent Deposits */}
      <Grid item xs={12} md={4} lg={3}>
        <Paper className={fixedHeightPaper}>

        </Paper>
      </Grid>
      {/* Recent Orders */}
      <Grid item xs={12}>
        <Paper className={classes.paper}>

        </Paper>
      </Grid>
      </Suspense>
    </Grid>
    </Container>
  );
}
