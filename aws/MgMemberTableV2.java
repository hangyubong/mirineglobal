package com.amazonaws.lambda.demo.table;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.amazonaws.services.dynamodbv2.document.Item;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey; 

@DynamoDbBean
public class MgMemberTableV2 {

	public String id;
	public String mg_name;
	public String birth_date;
	public String email_address;
	public String created_at;
	public String updated_at;
	public String insert_user;
	public String updated_user;
	public int version;

	@DynamoDbPartitionKey
	public String getId() {
		return id;
	}
	
	public String getMgName() {
		return mg_name;		
	}
	
	public void setId(String id) {
		this.id = id;	
	}
	
	public void setMgName(String mg_name) {
		this.mg_name = mg_name;
	}

	public String getBirth_date() {
		return birth_date;
		
	}	
	public void setBirth_date(String birth_date) {
		this.birth_date = birth_date;	
	}
	
	public String getEmail_address() {
		return email_address;
		
	}	
	public void setEmail_address(String email_address) {
		this.email_address = email_address;	
	}
	
	public String getCreated_at() {
		return created_at;
		
	}	
	public void setCreated_at(String created_at) {
		this.created_at = created_at;	
	}
	
	public String getUpdated_at() {
		return updated_at;
		
	}	
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;	
	}
	
	public String getInsert_user() {
		return mg_name;
		
	}	
	public void setInsert_user(String insert_user) {
		this.insert_user = insert_user;	
	}
	
	public String getUpdated_user() {
		return updated_user;
		
	}	
	public void setUpdated_user(String updated_user) {
		this.updated_user = updated_user;	
	}
	
	public int getVersion() {
		return version;
		
	}	
	public void setVersion(int version) {
		this.version = version;	
	}

	public String getMg_name() {
		return mg_name;
	}

	@Override
	public String toString() {
		return "MgMemberTable [getId()=" + getId() + ", getMgName()=" + getMgName() + ", getBirth_date()="
				+ getBirth_date() + ", getEmail_address()=" + getEmail_address() + ", getCreated_at()="
				+ getCreated_at() + ", getUpdated_at()=" + getUpdated_at() + ", getInsert_user()=" + getInsert_user()
				+ ", getUpdated_user()=" + getUpdated_user() + ", getVersion()=" + getVersion() + "]";
	}
	
	
	

}
