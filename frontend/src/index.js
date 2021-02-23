import React from 'react';
import ReactDOM from 'react-dom';

import Header from './components/Header';
import CustomForm from './components/Forms';


ReactDOM.render(
  <Header />,
  document.getElementById('header')
);

ReactDOM.render(
  <CustomForm />,
  document.getElementById('forms')
);
