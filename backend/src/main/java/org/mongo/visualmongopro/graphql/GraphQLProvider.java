package org.mongo.visualmongopro.graphql;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

import java.io.IOException;
import java.net.URL;
import javax.annotation.PostConstruct;

import org.mongo.visualmongopro.graphql.scalars.Decimal128StringCoercing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider {

  private GraphQL graphQL;
  @Autowired GraphQLDataFetchers graphQLDataFetchers;
  @Autowired CollectionDataFetcher collectionDataFetchers;

  @Bean
  public GraphQL graphQL() {
    return graphQL;
  }

  @SuppressWarnings("UnstableApiUsage")
  @PostConstruct
  public void init() throws IOException {
    URL url = Resources.getResource("schema.graphqls");
    String sdl = Resources.toString(url, Charsets.UTF_8);
    GraphQLSchema graphQLSchema = buildSchema(sdl);
    this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
  }

  private GraphQLSchema buildSchema(String sdl) {
    TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
    RuntimeWiring runtimeWiring = buildWiring();
    SchemaGenerator schemaGenerator = new SchemaGenerator();
    return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
  }

  private RuntimeWiring buildWiring() {
    return RuntimeWiring.newRuntimeWiring()
        .scalar(ExtendedScalars.DateTime)
        .scalar(
            GraphQLScalarType.newScalar()
                .name("Decimal128")
                .description(
                    "An IEEE 754-2008 binary integer decimal representation of a 128-bit decimal value, "
                        + "supporting 34 decimal digits of significand and an exponent range * of -6143 to +6144")
                .coercing(new Decimal128StringCoercing())
                .build())
        // These are just testing... ignore
        .type(
            newTypeWiring("Query").dataFetcher("books", graphQLDataFetchers.getBooksDataFetcher()))
        .type(
            newTypeWiring("Query")
                .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
        .type(
            newTypeWiring("Query")
                .dataFetcher("booksByPrice", graphQLDataFetchers.getBooksByPriceDataFetcher()))

        .type(
            newTypeWiring("Query")
              .dataFetcher("collections", collectionDataFetchers.getCollectionsDataFetcher())
        )
        .type(
            newTypeWiring("Query")
                .dataFetcher("collectionsByDatabase", collectionDataFetchers.getCollectionsByDatabaseDataFetcher())
        )
        .type(
            newTypeWiring("Query")
                .dataFetcher("collectionByNamespace", collectionDataFetchers.getCollectionsByNamespaceDataFetcher())
        )
        .build();
  }
}
