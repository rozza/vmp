import React,{useState,useEffect} from 'react';
import Form from "@rjsf/material-ui";
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { defaultStyles } from '../styles';

export default function Forms({data}) {
  console.log(data)
  const classes = defaultStyles();
  var schema = JSON.parse(data.jsonSchema);
  var [formData, setData] = useState([]);
  var getData = () => {
    fetch("/api/docs/" + data.databaseName + "/" + data.collectionName + "/0/100"
    ,{
      headers : {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
       }
    }
    )
      .then(function(response){
        return response.json();
      })
      .then(function(myJson) {
        setData(myJson)
      });
  }
  useEffect(()=>{
    getData()
  },[])

  return (
    <Container maxWidth={false} className={classes.container}>
      <Grid container spacing={3} justify="center">
          <Grid item xs={12} md={8} lg={12}>
            <Paper elevation={3}>
             <Form schema={schema} className={classes.padded} formData={formData[0]}/>
            </Paper>
          </Grid>
      </Grid>
    </Container>
  );
}
