package com.amazonaws.lambda.demo.table;

public class MgClient {
	
	String id;
	String address;
	String email_address;
	String full_name;
	
	public MgClient(String id, String address, String email_address, String full_name) {
		super();
		this.id = id;
		this.address = address;
		this.email_address = email_address;
		this.full_name = full_name;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getEmail_address() {
		return email_address;
	}
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	
	
	@Override
	public String toString() {
		return "MgClient [id=" + id + ", address=" + address + ", email_address=" + email_address + ", full_name="
				+ full_name + "]";
	}
	


	
	
}
