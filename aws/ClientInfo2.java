package com.amazonaws.lambda.demo.table;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class ClientInfo2 {

	public String address;
	public String email_address;
	public String full_name;

	public String getAddress(String address) {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail_address(String email_address) {
		return email_address;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public String getFullName(String full_name) {
		return full_name;
	}

	public void setFullName(String full_name) {
		this.full_name = full_name;
	}

	@Override
	public String toString() {
		return "ClientInfo2 [address=" + address + ", email_address=" + email_address + ", full_name=" + full_name
				+ "]";
	}

	
}
