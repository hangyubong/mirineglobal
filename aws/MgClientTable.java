package com.amazonaws.lambda.demo.table;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "MG_CLIENT")
public class MgClientTable {
	
	public static String id;
	public static ClientInfo client_info;
	public static String created_at;
	public static String updated_at;
	public static String insert_user;
	public static String updated_user;
	public static int version;

	
	@DynamoDBHashKey(attributeName = "id")
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@DynamoDBAttribute(attributeName = "client_info")
	public ClientInfo getClientInfo() {
		return client_info;	
	}

	public void setClientInfo(ClientInfo client_info) {
		this.client_info = client_info;	
	}
	
	@DynamoDBRangeKey(attributeName = "created_at")
	public String getCreated_at() {
		return created_at;		
	}	
	
	public void setCreated_at(String created_at) {
		this.created_at = created_at;	
	}
	
	@DynamoDBAttribute(attributeName = "updated_at")
	public String getUpdated_at() {
		return updated_at;		
	}	
	
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;	
	}
	
	@DynamoDBAttribute(attributeName = "insert_user")
	public String getInsert_user() {
		return insert_user;		
	}	
	
	public void setInsert_user(String insert_user) {
		this.insert_user = insert_user;	
	}
	
	@DynamoDBAttribute(attributeName = "updated_user")
	public String getUpdated_user() {
		return updated_user;	
	}	
	
	public void setUpdated_user(String updated_user) {
		this.updated_user = updated_user;	
	}
	
	@DynamoDBAttribute(attributeName = "version")
	public int getVersion() {
		return version;	
	}	
	
	public void setVersion(int version) {
		this.version = version;	
	}


	
}//end class.
