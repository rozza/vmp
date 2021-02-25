import React from 'react';
import Form from "@rjsf/material-ui";
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { defaultStyles } from './styles';

const schema = {
  title: "Test form",
  type: "object",
  properties: {
    name: {
      type: "string"
    },
    age: {
      type: "number"
    }
  }
};

export default function Forms() {
  const classes = defaultStyles();
  return (
    <Container maxWidth={false} className={classes.container}>
      <Grid container spacing={3} justify="center">
          <Grid item xs={12} md={8} lg={12}>
            <Paper elevation={3}>
             <Form schema={schema} className={classes.padded}/>
            </Paper>
          </Grid>
      </Grid>
    </Container>
  );
}

