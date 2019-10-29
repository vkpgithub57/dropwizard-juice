import React, { useState } from 'react';
import { Formik, ErrorMessage } from 'formik';
import * as Yup from 'yup';
import {
  Col, Form,
  FormGroup, Label, Input, Button
} from 'reactstrap';
import { createCustomer, updateCustomer } from '../Api';

const CustomerForm = ({onSuccess, data}) => {
  const [ isSubmitting, setSubmitting ] = useState(false);
  const initialValue = {
    firstName: data.firstName ?  data.firstName :  '', 
    lastName: data.lastName ?  data.lastName : '', 
    email: data.email ?  data.email : '', 
    phone: data.phone ?  data.phone : '', 
  };
  if(data._id) {
    initialValue._id = data._id;
  }
  return (
    <Formik
      initialValues={initialValue}
      validationSchema={Yup.object({
        firstName: Yup.string()
          .min(2, 'Must be more than 2 characters')
          .max(50, 'Must be less than 50 characters')
          .required('First name is required'),
        lastName: Yup.string()
          .min(2, 'Must be more than 2 characters')
          .max(50, 'Must be less than 50 characters')
          .required('Lastname is required'),
        email: Yup.string()
          .email('Invalid email addresss')
          .required('Email is required'),
        phone: Yup.string()
                  .min(8, 'Must be more than 8 characters')
                  .max(20, 'Must be less than 20 characters')
                  .required('Phone number is required'),
      })}
      onSubmit={(values) => {
        setSubmitting(true);
        if(initialValue._id) {
          updateCustomer({ ...values,_id: initialValue._id }).then((response)=>{
            if(response.data.status) {
              alert(response.data.message)
              onSuccess();
            } else {
              setSubmitting(false);
              alert(response.data.message)
            }
          }, (error)=>{
            console.log(error);
            setSubmitting(false);
            alert(error.message);
          });
        } else {
          createCustomer(values).then((response)=>{
            if(response.data.status) {
              alert(response.data.message)
              onSuccess();
            } else {
              setSubmitting(false);
              alert(response.data.message)
            }
          }, (error)=>{
            console.log(error);
            setSubmitting(false);
            alert(error.message);
          });
        }
      }}
    >
      {({handleSubmit, getFieldProps, touched, errors}) => (
        <Form className="form" onSubmit={handleSubmit}>
          <Col>
            <FormGroup>
              <Label for="firstName">First Name</Label>
              <Input
                type="text"
                name="firstName"
                id="firstName"
                placeholder="First Name"
                {...getFieldProps('firstName')}
              />
              {touched.firstName && errors.firstName ? (
                <ErrorMessage name="firstName" component="div" className="text-danger" />
              ) : null}
            </FormGroup>
          </Col>
          <Col>
            <FormGroup>
              <Label for="lastName">Last Name</Label>
              <Input
                type="text"
                name="lastName"
                id="lastName"
                placeholder="Last Name"
                {...getFieldProps('lastName')}
              />
              {touched.lastName && errors.lastName ? (
                <ErrorMessage name="lastName" component="div" className="text-danger" />
              ) : null}
            </FormGroup>
          </Col>
          <Col>
            <FormGroup>
              <Label for="email">Email</Label>
              <Input
                type="email"
                name="email"
                id="email"
                placeholder="Email"
                {...getFieldProps('email')}
              />
              {touched.email && errors.email ? (
                <ErrorMessage name="email" component="div" className="text-danger" />
              ) : null}
            </FormGroup>
          </Col>
          <Col>
            <FormGroup>
              <Label for="phone">Phone</Label>
              <Input
                type="string"
                name="phone"
                id="phone"
                placeholder="Phone"
                {...getFieldProps('phone')}
              />
              {touched.phone && errors.phone ? (
                <ErrorMessage name="phone" component="div" className="text-danger" />
              ) : null}
            </FormGroup>
          </Col>
          <Col>
            <Button type="submit" color="primary" disabled={isSubmitting}>{isSubmitting ? 'Saving...' : 'Save Customer'}</Button>{' '}
          </Col>          
        </Form>        
      )}
    </Formik>
  );
};

export default CustomerForm;