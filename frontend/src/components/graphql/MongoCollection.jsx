import React from 'react';
import { GraphQL, GraphQLProvider, useGraphQL } from 'graphql-react';
import { useParams } from "react-router-dom";

import Container from '@material-ui/core/Container';
import Grid from '@material-ui/core/Grid';
import Paper from '@material-ui/core/Paper';

import Loader from '../Loader';
import { reactOptions } from './reactOptions';
import CollectionForm from '../forms/CollectionForm';
import { defaultStyles } from '../styles';

const CollectionGraphQL = ({databaseName, collectionName}) => {

  const classes = defaultStyles();
  const query = `
    {
      collectionByNamespace(databaseName: "${databaseName}", collectionName: "${collectionName}") {
        databaseName
        collectionName
        jsonSchema
        isSchemaAutoGenerated
        uiSchema
      }
    }
  `;


  // Memoization allows the `useGraphQL` hook to avoid work in following renders
  // with the same GraphQL operation.
  const operation = React.useMemo(
    () => ({
      query,
      variables: {
      },
    }),
    [databaseName, collectionName]
  );

    // The `useGraphQL` hook can be used for both queries and mutations.
  const { loading, cacheValue: { data, ...errors } = {} } = useGraphQL({
    operation,
    fetchOptionsOverride: reactOptions,
    loadOnMount: true,
    loadOnReload: true,
    loadOnReset: true
  });

  return (
  <Container maxWidth={false} className={classes.container}>
    <Grid container spacing={3} justify="center">
        <Grid item xs={12} md={8} lg={12}>
          <Paper elevation={3}>
          {data && ( <CollectionForm data={data.collectionByNamespace} /> )}
          {loading && <Loader />}
        </Paper>
     </Grid>
    </Grid>
  </Container>
  );
}

export default function MongoCollection() {
  let { databaseName, collectionName } = useParams();
  return (
    <CollectionGraphQL databaseName={databaseName} collectionName={collectionName} />
  );
};
