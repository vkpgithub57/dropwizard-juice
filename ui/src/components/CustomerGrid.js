import React from 'react';
import ReactTable from 'react-table';
import {
  Button
} from 'reactstrap';

const CustomerGrid = ({ data = [], loading = false , onPaginateAndSort, pages=10 , onEditClick }) => {

  const columns = [{
    Header: 'First Name',
    accessor: 'firstName' // String-based value accessors!
  }, {
    Header: 'Last Name',
    accessor: 'lastName' // String-based value accessors!
  }, {
    Header: 'Email',
    accessor: 'email' // String-based value accessors!
  },{
    Header: 'Phone',
    accessor: 'phone' // String-based value accessors!
  },
  {
    Header: 'Edit',
    accessor: '_id',
    sortable: false,
    Cell: ( { row = {} }) => {
        return (
          <Button color="primary" onClick={()=>{  
            let data = {
              email: row.email,
              firstName: row.firstName,
              lastName: row.lastName,
              phone: row.phone,
              _id: row._id
            };
            onEditClick(data);
          }}>Edit</Button>
        )
    }
  }
]

  return <ReactTable
    data={data}
    columns={columns}
    loading={loading}
    manual={true}
    pages={pages}
    defaultPageSize={20}
    sortable={ true}
    onFetchData={(state, instance) => { 
      let qPrm = {
        limit: state.pageSize,
        page: state.page+1
      };
      if(state.sorted && state.sorted.length > 0) {
        qPrm.sort = {
          "key":state.sorted[0].id,
          "value": state.sorted[0].desc ? 'dsc' : 'asc'
       };
      }
      onPaginateAndSort(qPrm);
    }}
  />
};

export default CustomerGrid;