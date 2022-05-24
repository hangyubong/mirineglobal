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
 
import java.text.ParseException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.Handler;
import com.amazonaws.lambda.demo.guide.PersonResponse;
import com.amazonaws.lambda.demo.service.MgMemberService;
import com.amazonaws.lambda.demo.service.DocumentAPIItemCRUDExample;
import com.amazonaws.lambda.demo.table.MgMemberTable;

 

public class MgMemberHandler implements RequestHandler<Object, String> {

	static final Logger logger = LogManager.getLogger(MgMemberHandler.class);
    private DynamoDB dynamoDb;
    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
    private DynamoDBMapper dynamoDBMapper; 
    private MgMemberService service = new MgMemberService(client);
    private DocumentAPIItemCRUDExample crude = new DocumentAPIItemCRUDExample();

    private String DYNAMODB_TABLE_NAME = "MG_MEMBER";

    public String handleRequest(Object event, Context context) {
    	
    	logger.info(event.toString());
    	// event Map에 저장
    	Map<String, String> eventMap = (Map<String, String>) event;
    	// Map을 MgMemberTable에 저장
    	MgMemberTable member = mapToMgMemberTable(eventMap);
    	// MgMemberTable 등록
    	service.insertMember(member);
    	
//    	crude.createItems();
		return "success!!" +  DYNAMODB_TABLE_NAME;
        
    }
    
    private MgMemberTable mapToMgMemberTable(Map<String, String> event) {
    	MgMemberTable member = new MgMemberTable();
    	member.setId(event.get("id"));
    	member.setMgName(event.get("mg_name"));
    	member.setBirth_date(event.get("birth_date"));
    	member.setEmail_address(event.get("email_address"));
    	member.setCreated_at(event.get("created_at"));
    	member.setUpdated_at(event.get("updated_at"));
    	member.setInsert_user(event.get("insert_user"));
    	member.setUpdated_user(event.get("updated_user"));
    	member.setVersion(event.get("version").compareTo(DYNAMODB_TABLE_NAME));
    	
    	return member;
    }

}