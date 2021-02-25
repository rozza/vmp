import React from 'react';
import { GraphQL, GraphQLProvider as GQLProvider } from 'graphql-react';

const graphql = new GraphQL();

const GraphqlProvider = ({ children }) => (
  <GQLProvider graphql={graphql}>{children}</GQLProvider>
);

export default GraphqlProvider;
