package com.amazonaws.lambda.demo.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBDocument
public class ClientInfo {

	public static String address;
	public static String email_address;
	public static String full_name;
	
	
	@DynamoDBAttribute(attributeName = "address")
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	@DynamoDBAttribute(attributeName = "email_address")
	public String getEmail_address() {
		return email_address;
	}
	
	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}
	
	@DynamoDBAttribute(attributeName = "full_name")
	public String getFullName() {
		return full_name;
	}
	
	public void setFullName(String full_name) {
		this.full_name = full_name;
	}
	
}
