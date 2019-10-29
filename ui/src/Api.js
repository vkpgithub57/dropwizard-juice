import axios from 'axios';

const BASE_URL = "http://106.51.64.111:4000/customer";

export function createCustomer(customer) {
    return axios.post(`${BASE_URL}/`, customer);
}

export function updateCustomer(customer) {
    return axios.post(`${BASE_URL}/update`, customer);
}

export function searchCustomer(params) {
    return axios.post(`${BASE_URL}/search`,  params);
}
