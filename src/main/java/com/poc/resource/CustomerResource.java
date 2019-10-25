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
import com.poc.model.SearchCustomer;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
public class CustomerResource {

	CustomerDAO customerDAO;
	Map<String, String> response = null;
 
	@Inject
	public CustomerResource(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@POST
	@Path("/create")
	@Timed
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createCustomer(@NotNull @Valid final Customer customer) {
		Gson gson = new Gson();
		String json = gson.toJson(customer);
		customerDAO.createCustomer(new Document(BasicDBObject.parse(json)));
		response = new HashMap<>();
		response.put("message", "Customer created successfully");
		return Response.ok(response).build();
	}

	@POST
	@Path("/update")
	@Timed
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomer(@NotNull @Valid final Customer customer) {
		customerDAO.updateCustomer("firstName", "lastName", "email","phone",customer);
		response = new HashMap<>();
		response.put("message", "Customer Updated successfully");
	    return Response.ok(response).build();
	}

	@GET
	@Timed
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomers() {
		List<Document> documents = customerDAO.getCustomers();
		return Response.ok(documents).build();
	}

	@POST
	@Timed
	@Path("/search")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCustomerPage(@NotNull @Valid final SearchCustomer searchCustomer) {
		List<Document> documents = customerDAO.getPaginatedData(searchCustomer);
		return Response.ok(documents).build();
	}
}
