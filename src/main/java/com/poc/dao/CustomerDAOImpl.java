package com.poc.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.poc.model.Customer;
import com.poc.model.SearchCustomer;
import com.poc.model.SortCustomer;
import com.poc.util.MongoDBConnection;

public class CustomerDAOImpl implements CustomerDAO {
	
	
	MongoCollection<Document> collection = MongoDBConnection.getCollection();
	
	public void createCustomer(Document document) {
		collection.insertOne(document);
    }
	
	public void updateCustomer(String fName, String lName, String email,String phone,
			Customer customer) {
		collection.updateOne(new Document(fName, customer.getFirstName()),
                new Document("$set", new Document(lName, customer.getLastName()).append(email, customer.getEmail())));
		
	}
	public List<Document> getCustomers() {
        return collection.find().into(new ArrayList<>());
    }

	public List<Document> getPaginatedData(SearchCustomer searchCustomer) {
		
		SortCustomer sortCustomerObj = searchCustomer.getSort();
		String key = sortCustomerObj.getKey();
		String value = sortCustomerObj.getValue();
		
		int sort = 0;
		if(value.equals("asc")) {
			sort = 1;
		} else if(value.equals("dsc")) {
			sort = -1;
		} else {
			sort = 0;
		}
		
		int limit = searchCustomer.getLimit();
		int page =  searchCustomer.getPage();
		int skip = (page-1)*limit;
		
		AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
		        new Document("$sort", new Document(key, sort)),
		        new Document("$skip", skip),
		        new Document("$limit", limit),
		        Aggregates.match(
						Filters.or(
								Filters.regex("firstName", searchCustomer.getSearch()),
								Filters.regex("lastName", searchCustomer.getSearch())
						)
					)
		));
		
		List<Document> documents = new ArrayList<>();
		for (Document dbObject : output)
		{
			documents.add(dbObject);
		    System.out.println(dbObject);
		}
		return documents;
	}
}
