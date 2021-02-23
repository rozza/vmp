import React from 'react';
import Form from "@rjsf/material-ui";

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

export default function CustomForm() {
  return (
    <Form schema={schema} />
  );
}

