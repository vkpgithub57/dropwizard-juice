package com.poc.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Collation;
import com.mongodb.client.model.CollationStrength;
import com.mongodb.client.model.Filters;
import com.poc.model.Customer;
import com.poc.model.CustomerUpdateRequest;
import com.poc.model.SearchCustomer;
import com.poc.model.SortCustomer;
import com.poc.util.MongoDBConnection;

public class CustomerDAOImpl implements CustomerDAO {
	
	
	MongoCollection<Document> collection = MongoDBConnection.getCollection();
	
	public void createCustomer(Document document) {
		collection.insertOne(document);
    }
	
	public void updateCustomer(String fName, String lName, String email,String phone,
			CustomerUpdateRequest customer) {
		ObjectId idCast = new ObjectId(customer.get_id());
		collection.updateOne(new Document("_id",idCast),
				new Document("$set", new Document(fName, customer.getFirstName()).append(lName, customer.getLastName()).append(email, customer.getEmail()).append(phone, customer.getPhone())));
	}
	public List<Document> getCustomers() {
		
		
		// By Default, limit 20, First 20 records will be shown in Dashboard
		AggregateIterable<Document> output = collection.aggregate(Arrays.asList(
		        new Document("$limit", 20)
		));
		List<Document> documents = new ArrayList<>();
		for (Document dbObject : output)
		{
			documents.add(dbObject);
		}
		return documents;
    }
	
public List<CustomerUpdateRequest> getPaginatedData(SearchCustomer searchCustomer) {
		
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
		AggregateIterable<Document> output = null;
		Collation collation = null;
		if(!key.equals("_id")) {
			collation = Collation.builder().caseLevel(false).locale("en").collationStrength(CollationStrength.fromInt(2)).build();
			if(searchCustomer.getSearch()==null || searchCustomer.getSearch()=="") {
				output = collection.aggregate(Arrays.asList(
						new Document("$sort", new Document(key, sort)),
				        new Document("$skip", skip),
				        new Document("$limit", limit)
				)).collation(collation);
			}
			output = collection.aggregate(Arrays.asList(
			        
					Aggregates.match(
							Filters.or(
									Filters.regex("firstName", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("lastName", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("email", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("phone", searchCustomer.getSearch())
							)
					),
					new Document("$sort", new Document(key, sort)),
			        new Document("$skip", skip),
			        new Document("$limit", limit)
			)).collation(collation);
		} else {
			if(searchCustomer.getSearch()==null || searchCustomer.getSearch()=="") {
				output = collection.aggregate(Arrays.asList(
						new Document("$sort", new Document(key, sort)),
				        new Document("$skip", skip),
				        new Document("$limit", limit)
				));
			}
			output = collection.aggregate(Arrays.asList(
			        
					Aggregates.match(
							Filters.or(
									Filters.regex("firstName", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("lastName", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("email", searchCustomer.getSearch().toLowerCase()),
									Filters.regex("phone", searchCustomer.getSearch())
							)
					),
					new Document("$sort", new Document(key, sort)),
			        new Document("$skip", skip),
			        new Document("$limit", limit)
			));
		}
		
		List<CustomerUpdateRequest> documents = new ArrayList<>();
		CustomerUpdateRequest customerObj = null;
		for (Document dbObject : output)
		{
			customerObj = new CustomerUpdateRequest();
			customerObj.set_id(dbObject.getObjectId("_id").toHexString());
			customerObj.setFirstName(dbObject.getString("firstName"));
			customerObj.setLastName(dbObject.getString("lastName"));
			customerObj.setEmail(dbObject.getString("email"));
			customerObj.setPhone(dbObject.get("phone").toString());
			documents.add(customerObj);
		}
		return documents;
	}



	public int getCollectionRecordCount() {
		
		return (int) collection.count();
	}

	public Boolean checkEmailExist(Customer customer) {

		FindIterable<Document> output =  collection.find(new Document("email", customer.getEmail()));
		List<Document> documents = new ArrayList<>();
		for (Document dbObject : output)
		{
			documents.add(dbObject);
		}
		if(documents.size()>0) {
			return false;
		}else {
			return true;
		}
	}

	@Override
	public Boolean checkEmailUpdate(CustomerUpdateRequest customer) {
		
		FindIterable<Document> output =  collection.find(
				Filters.and(
						new Document("email", customer.getEmail()),
						Filters.not(new Document("_id",new ObjectId(customer.get_id())))
				));
		List<Document> documents = new ArrayList<>();
		ObjectId existId = null;
		for (Document dbObject : output)
		{
			existId = dbObject.getObjectId("_id");
			documents.add(dbObject);
		}
		if(documents.size()>0 && (new ObjectId(customer.get_id())==existId)) {
			return true;
		}
		else if(documents.size()>0 && (new ObjectId(customer.get_id())!=existId)) {
			return false;
		}{
			return true;
		}
	}
}
