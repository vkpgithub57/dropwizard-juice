package com.poc.dao;

import java.util.List;

import org.bson.Document;

import com.poc.model.Customer;
import com.poc.model.SearchCustomer;


public interface CustomerDAO {

	public void createCustomer(Document document);
    public void updateCustomer(String fName, String lName, String email,String phone, Customer customer);
    public List<Document> getCustomers();
	public List<Document> getPaginatedData(SearchCustomer searchCustomer);
}
