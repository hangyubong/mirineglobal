package com.amazonaws.lambda.demo.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDBTable(tableName = "")
public class BaseTable {

	String createdAt;
	String updatedAt;
	String insertUser;
	String updatedUser;
	long version;
	
	@DynamoDBAttribute(attributeName = "created_at")
	public String getCreatedAt() {
		return createdAt;
	}
	@DynamoDBAttribute(attributeName = "updated_at")
	public String getUpdatedAt() {
		return updatedAt;
	}
	@DynamoDBAttribute(attributeName = "insert_user")
	public String getInsertUser() {
		return insertUser;
	}
	@DynamoDBAttribute(attributeName = "updated_user")
	public String getUpdatedUser() {
		return updatedUser;
	}
	@DynamoDBAttribute(attributeName = "version")
	public long getVersion() {
		return version;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public void setInsertUser(String insertUser) {
		this.insertUser = insertUser;
	}
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}
	public void setVersion(long version) {
		this.version = version;
	}
	
	
}
