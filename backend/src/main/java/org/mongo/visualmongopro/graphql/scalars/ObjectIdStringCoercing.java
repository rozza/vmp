package org.mongo.visualmongopro.graphql.scalars;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import org.bson.types.ObjectId;

public final class ObjectIdStringCoercing implements Coercing<ObjectId, String> {
  @Override
  public String serialize(Object dataFetcherResult) throws CoercingSerializeException {
    if (dataFetcherResult instanceof ObjectId) {
      return ((ObjectId) dataFetcherResult).toHexString();
    } else {
      throw new CoercingSerializeException();
    }
  }

  @Override
  public ObjectId parseValue(Object input) throws CoercingParseValueException {
    if (input instanceof String) {
      try {
        return new ObjectId((String)input);
      } catch (RuntimeException e) {
        throw new CoercingParseValueException("Could not parse string as ObjectId", e);
      }
    } else {
      throw new CoercingParseValueException("Could not parse " + input.getClass().getSimpleName() + " as ObjectId");
    }
  }

  @Override
  public ObjectId parseLiteral(Object input) throws CoercingParseLiteralException {
    if (input instanceof StringValue) {
      return parseValue(((StringValue)input).getValue());
    } else {
      throw new CoercingParseValueException("Could not parse " + input.getClass().getSimpleName() + " as ObjectId");
    }
  }
}
