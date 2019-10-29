package com.poc.dao;

import java.util.List;

import org.bson.Document;

import com.poc.model.Customer;
import com.poc.model.CustomerUpdateRequest;
import com.poc.model.SearchCustomer;


public interface CustomerDAO {

	public void createCustomer(Document document);
	public Boolean checkEmailExist(Customer customer);
	public Boolean checkEmailUpdate(CustomerUpdateRequest customer);
    public void updateCustomer(String fName, String lName, String email,String phone, CustomerUpdateRequest customer);
    public List<Document> getCustomers();
	public List<CustomerUpdateRequest> getPaginatedData(SearchCustomer searchCustomer);
	public int getCollectionRecordCount();
}
