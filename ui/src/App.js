import React, { useState, useEffect, useCallback, useRef } from 'react';
import {
  Container, Row, Col,
  Button, Modal, ModalHeader, ModalBody, Form
} from 'reactstrap';
import CustomerForm from './components/CustomerForm';
import CustomerGrid from './components/CustomerGrid';
import './App.css';
import { searchCustomer } from './Api';
import { debounce } from "lodash";


const sendQuery = (query) => console.log(`Querying for ${query}`);
const App = () => {

  const [modal, setModal] = useState(false);
  const [customerList, setCustomerList] = useState([]);
  const [qryParam, setQueryParam] = useState({
    "search": "",
    "sort": {
      "key": "_id",
      "value": "dsc"
    },
    "limit": 20,
    "page": 1
  });
  const [pages, setPages] = useState(10);

  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({});
  const [search, setSearch] = useState('');

  const toggle = () => setModal(!modal);


  useEffect(() => {
    setLoading(true);
    searchCustomer(qryParam).then((response) => {
      if (response.status === 200) {
        setPages(Math.round(response.data.record_count / qryParam.limit))
        setCustomerList(response.data.data);
      }
      setLoading(false);
    }, (error) => {
      console.log(error);
      setLoading(false);
    });
  }, [qryParam]);

  const onEditClick = useCallback((data) => {
    setFormData(data);
    setModal(true);
  });

  // const debounceCallBack  = useCallback(debounce(()=>{
  //   setQueryParam({ ...qryParam, search: search });
  // }, 1000));

  return (
    <Container>
      <Row style={{ paddingTop: "50px" }}>
        <Col md="12">
          <Row>
            <Col md="auto" className="d-flex align-items-center mb-3">
              <Form className={"d-flex"} onSubmit={(event)=>{
                  event.preventDefault();
                  setQueryParam({ ...qryParam, search: search });
              }}>
                  Search: <input value={search} className="form-control" onChange={(event) => {
                            setSearch(event.target.value);
                          }} />
                  <Button color="primary" className="ml-1" onClick={() => { setQueryParam({ ...qryParam, search: search }); }}>Search</Button>
                  <Button color="primary" className="ml-1" onClick={() => { setSearch(''); setQueryParam({ ...qryParam, search: '' }); }}>Reset</Button>
              </Form>
            </Col>
            <Col md="auto" className="ml-auto text-right">
              <span style={{ marginLeft: "auto" }}>
                <Button color="primary" onClick={() => { setFormData({}); setModal(true) }}>Add Customer</Button>
              </span>
            </Col>
          </Row>

          <Row style={{ paddingTop: "10px" }}>
            <Col md="12">

              <CustomerGrid
                data={customerList}
                loading={loading}
                pages={pages}
                onPaginateAndSort={(sortPaginate) => {
                  setQueryParam({ ...qryParam, ...sortPaginate });
                }}
                onEditClick={onEditClick}
              />

              <Modal isOpen={modal} toggle={toggle}>
                <ModalHeader toggle={toggle}>Customer</ModalHeader>
                <ModalBody>
                  <CustomerForm data={formData} onSuccess={() => { toggle(); setQueryParam({ ...qryParam }); }} />
                </ModalBody>
              </Modal>

            </Col>
          </Row>
        </Col>
      </Row>
    </Container>
  );
}

export default App;
