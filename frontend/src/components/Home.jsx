import React, { Suspense } from 'react';
import clsx from 'clsx';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import Container from '@material-ui/core/Container';
import { withStyles } from "@material-ui/core/styles";

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';

import Avatar from '@material-ui/core/Avatar';

import { styles } from './styles';
import Loader from './Loader';


export default function Home() {

  const classes = withStyles(styles);
  const fixedHeightPaper = clsx(classes.paper, classes.fixedHeight);

  return (
  <>
  <Container maxWidth="xl" className={classes.container}>

  <Grid container
    direction="row"
    justify="center"
    alignItems="top"
    spacing={3}
  >
    <Grid item xs={6}>
       <Paper className={classes.paper}>
          <Grid container spacing={4}>
            <Grid item />
            <Grid item />
            <Grid item >
              <Typography variant="h3" component="h2">Tech Stack</Typography>
              <Typography>100% Hackfest compliant</Typography>
            </Grid>
          </Grid>
       </Paper>

       <Paper className={classes.paper}>
          <Grid container wrap="nowrap" spacing={4}>
           <Grid item />
           <Grid item >
               <Avatar src="/assets/mongodb.svg" className={classes.largeAvatar} />
            </Grid>
            <Grid item >
              <Typography variant="h5" component="strong" noWrap className={classes.largeAvatar}>MongoDB</Typography>
            </Grid>
          </Grid>
       </Paper>

    <Paper className={classes.paper}>
       <Grid container wrap="nowrap" spacing={4}>
        <Grid item />
        <Grid item>
            <Avatar src="/assets/jvm.png" className={classes.largeAvatar} />
         </Grid>
         <Grid item >
           <Typography variant="h5" component="strong" className={classes.largeAvatar}>Java</Typography>
         </Grid>
       </Grid>
    </Paper>

      <Paper className={classes.paper}>
         <Grid container wrap="nowrap" spacing={4}>
          <Grid item />
          <Grid item>
              <Avatar src="/assets/spring.svg" className={classes.largeAvatar} />
           </Grid>
           <Grid item >
             <Typography variant="h5" component="strong" className={classes.largeAvatar}>Spring</Typography>
           </Grid>
         </Grid>
      </Paper>

    <Paper className={classes.paper}>
       <Grid container wrap="nowrap" spacing={4}>
        <Grid item />
        <Grid item>
            <Avatar src="/assets/graphQL.svg" className={classes.largeAvatar} />
         </Grid>
         <Grid item >
           <Typography variant="h5" component="strong" className={classes.largeAvatar}>GraphQL</Typography>
         </Grid>
       </Grid>
    </Paper>

      <Paper className={classes.paper}>
         <Grid container wrap="nowrap" spacing={4}>
          <Grid item />
          <Grid item>
              <Avatar src="/assets/swagger.png" className={classes.largeAvatar} />
           </Grid>
           <Grid item >
             <Typography variant="h5" component="strong" >Swagger</Typography>
           </Grid>
         </Grid>
      </Paper>

      <Paper className={classes.paper}>
         <Grid container wrap="nowrap" spacing={4}>
          <Grid item />
          <Grid item>
              <Avatar src="/assets/rest.png" className={classes.largeAvatar} />
           </Grid>
           <Grid item >
             <Typography variant="h5" component="strong" >REST</Typography>
           </Grid>
         </Grid>
      </Paper>

      <Paper className={classes.paper}>
         <Grid container wrap="nowrap" spacing={4}>
          <Grid item />
          <Grid item>
              <Avatar src="/assets/react.svg" className={classes.largeAvatar} />
           </Grid>
           <Grid item >
             <Typography variant="h5" component="strong" >React</Typography>
           </Grid>
         </Grid>
      </Paper>
      <Paper className={classes.paper} style={{height: '260px'}}/>
  </Grid>

  <Grid item xs={6}>

   <Paper className={classes.paper}>
      <Grid container spacing={4}>
        <Grid item />
        <Grid item />
        <Grid item >
          <Typography variant="h3" component="h2">Requisite Memes</Typography>
          <Typography>For hackfest compliance</Typography>
        </Grid>
      </Grid>
   </Paper>
   <Paper className={classes.paper}>
      <Grid container spacing={4}>
        <Grid item />
        <Grid item />
        <Grid item style={{'margin': 'auto'}}>
            <img src="/assets/meme1.png" />
        </Grid>
      </Grid>
   </Paper>
      <Paper className={classes.paper}>
         <Grid container spacing={4}>
           <Grid item />
           <Grid item />
           <Grid item style={{'margin': 'auto'}}>
               <img src="/assets/meme2.png" />
           </Grid>
         </Grid>
      </Paper>
         <Paper className={classes.paper}>
            <Grid container spacing={4}>
              <Grid item />
              <Grid item />
              <Grid item style={{'margin': 'auto'}}>
                  <img src="/assets/meme3.png" />
              </Grid>
            </Grid>
         </Paper>
  </Grid>

  </Grid>
  </Container>
  </>
  );
}
