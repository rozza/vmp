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

const CustomForm = () => (
  <Form schema={schema} />
);

export default CustomForm;
