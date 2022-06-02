package com.amazonaws.lambda.demo.table;

import java.util.Map;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class MgClientTableV2 {

	public String id;
//	public ClientInfo2 client_info;
	public Map<String, String> client_info;
	public String created_at;
	public String updated_at;
	public String insert_user;
	public String updated_user;
	public int version;

	@DynamoDbPartitionKey
	public String getId() {
		return id;
	}

//	public ClientInfo2 getClient_info() {
//		return client_info;
//	}

	@DynamoDbSortKey
	public String getCreated_at() {
		return created_at;
	}


	public Map<String, String> getClient_info() {
		return client_info;
	}

	public void setClient_info(Map<String, String> client_info) {
		this.client_info = client_info;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public String getInsert_user() {
		return insert_user;
	}

	public String getUpdated_user() {
		return updated_user;
	}

	public int getVersion() {
		return version;
	}

	public void setId(String id) {
		this.id = id;
	}

//	public void setClient_info(ClientInfo2 client_info) {
//		this.client_info = client_info;
//	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public void setInsert_user(String insert_user) {
		this.insert_user = insert_user;
	}

	public void setUpdated_user(String updated_user) {
		this.updated_user = updated_user;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "MgClientTableV2 [id=" + id + ", client_info=" + client_info + ", created_at=" + created_at
				+ ", updated_at=" + updated_at + ", insert_user=" + insert_user + ", updated_user=" + updated_user
				+ ", version=" + version + "]";

	}
}// end class.
