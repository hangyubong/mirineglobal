package com.amazonaws.lambda.demo.service;

import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.Item;

public class MgMemberService {

	private DynamoDBMapper dynamoDBMapper;
	
	public MgMemberService(AmazonDynamoDB client) {
		
		dynamoDBMapper = new DynamoDBMapper(client);
	}

	public void insertMember() {
		// member	
		MgMemberTable item = new MgMemberTable();
		
        item.setId("han");
        item.setMgName("hangyubong");     
        item.setBirth_date("2000/01/01");
        item.setEmail_address("hangyubong@88.gmail.com");
        item.setCreated_at("2022/05/20");
        item.setUpdated_at("2022/05/20");
        item.setInsert_user("han");
        item.setUpdated_user("han");
        item.setVersion(0);

		// mapper.save
        dynamoDBMapper.save(item);		
        
	}

	
}
