package com.poc.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.Document;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mongodb.BasicDBObject;
import com.poc.dao.CustomerDAO;
import com.poc.model.Customer;
import com.poc.model.CustomerUpdateRequest;
import com.poc.model.SearchCustomer;

@Path("/customer")
public class CustomerResource {

	CustomerDAO customerDAO;
	Map<String, Object> response = null;
 
	@Inject
	public CustomerResource(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@POST
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(@NotNull @Valid final Customer customer) {
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		response = new HashMap<>();
		// Email Validation
		Boolean checkEmail = customerDAO.checkEmailExist(customer);
		if(!checkEmail){
			response.put("message", "Email Already Exist.");
			response.put("status", false);
			return Response.ok(response).build();
		}
		customerDAO.createCustomer(new Document(BasicDBObject.parse(json)));
		response.put("message", "Customer created successfully");
		response.put("status", true);
		return Response.ok(response).build();
	}

	@POST
	@Path("/update")
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(@NotNull @Valid final CustomerUpdateRequest customer) {
		response = new HashMap<>();
		Boolean checkEmail = customerDAO.checkEmailUpdate(customer);
		if(!checkEmail){
			response.put("message", "Email Already Exist for other customer.");
			response.put("status", false);
			return Response.ok(response).build();
		}
		customerDAO.updateCustomer("firstName", "lastName", "email","phone",customer);
		response.put("message", "Customer Updated successfully");
		response.put("status", true);
	    return Response.ok(response).build();
	}

	@POST
	@Path("/dashboard")
	@Timed
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLimitedCustomers(@NotNull @Valid final SearchCustomer searchCustomer) {
		
		/*
		 * For Dashboard, By Default Limit 20, Page:1, sort: ascending, search: nil
		 */
		List<Document> documents = customerDAO.getCustomers();
		response = new HashMap<>();
		
		if(documents.size()<1) {
			response.put("message", "Data not found");
			response.put("data", documents);
			response.put("status", false);
			return Response.ok(response).build();
		}
		int count = customerDAO.getCollectionRecordCount();
		response.put("message", "Data available");
		response.put("data", documents);
		response.put("record_count", count);
		response.put("status", true);
		return Response.ok(response).build();
	}

	@POST
	@Timed
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerPage(@NotNull @Valid final SearchCustomer searchCustomer) {
		List<CustomerUpdateRequest> documents = customerDAO.getPaginatedData(searchCustomer);
		response = new HashMap<>();
		if(documents.size()<1) {
			response.put("message", "Data not found");
			response.put("data", documents);
			response.put("status", false);
			return Response.ok(response).build();
		}
		int count = customerDAO.getCollectionRecordCount();
		response.put("message", "Data available");
		response.put("data", documents);
		response.put("record_count", count);
		response.put("status", true);
		return Response.ok(response).build();

	}
}
