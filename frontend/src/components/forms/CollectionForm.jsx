import React,{useState,useEffect} from 'react';
import Form from "@rjsf/material-ui";


import TablePagination from '@material-ui/core/TablePagination';
import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';
import { defaultStyles } from '../styles';

export default function Forms({data}) {
  const classes = defaultStyles();
  var schema = JSON.parse(data.jsonSchema);
  var [collectionData, setCollectionData] = useState([]);
  var getCollectionData = () => {
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
        console.log(myJson)
        setCollectionData([{}].concat(myJson))
      });
  }
  useEffect(()=>{
    getCollectionData()
  },[])

    const [page, setPage] = useState(0);
    const [formData, setFormData] = useState({});
    const handleChangePage = (event, newPage) => {
      setPage(newPage);
      setFormData(collectionData[newPage])
    };

  return (
    <Container maxWidth={false} className={classes.container}>
      <Grid container spacing={3} justify="center">
          <Grid item xs={12} md={8} lg={12}>
            <Paper elevation={3}>
               <TablePagination
                 component="div"
                 count={-1}
                 page={page}
                 onChangePage={handleChangePage}
                 rowsPerPage={1}
                 rowsPerPageOptions={[]}
                 labelDisplayedRows={({ from, to, count }) => ''}
               />
                <Form schema={schema} className={classes.padded} formData={formData}/>
               <TablePagination
                 component="div"
                 count={-1}
                 page={page}
                 onChangePage={handleChangePage}
                 rowsPerPage={1}
                 rowsPerPageOptions={[]}
                 labelDisplayedRows={({ from, to, count }) => ''}
               />
            </Paper>
          </Grid>
      </Grid>
    </Container>
  );
}
