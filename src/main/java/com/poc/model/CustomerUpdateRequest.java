package com.poc.model;

import org.bson.types.ObjectId;

public class CustomerUpdateRequest {

	private String _id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String string) {
		this._id = string;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String string) {
		this.phone = string;
	}	
}
