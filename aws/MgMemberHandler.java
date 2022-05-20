package com.amazonaws.lambda.demo.handler;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.lambda.demo.PersonResponse;
import com.amazonaws.lambda.demo.service.MgMemberService;
import com.amazonaws.lambda.demo.table.MgMemberTable;

public class MgMemberHandler implements RequestHandler<Object, String> {

    private DynamoDB dynamoDb;
    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
    private DynamoDBMapper dynamoDBMapper;

    private MgMemberService service = new MgMemberService(client);
    
    private String DYNAMODB_TABLE_NAME = "MG_MEMBER";

    public String handleRequest(Object event, Context context) {
    	
        service.insertMember();
        
        return "Successfully";
    }

}