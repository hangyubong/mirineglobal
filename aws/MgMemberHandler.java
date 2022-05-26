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
import com.amazonaws.lambda.demo.guide.DynamoDBMapperQueryScanExample;
import com.amazonaws.lambda.demo.service.MgMemberService;
import com.amazonaws.lambda.demo.service.DocumentAPIItemCRUDExample;
import com.amazonaws.lambda.demo.service.MgClientService;
import com.amazonaws.lambda.demo.table.ClientInfo;
import com.amazonaws.lambda.demo.table.MgClientTable;
import com.amazonaws.lambda.demo.table.MgMemberTable;

public class MgMemberHandler implements RequestHandler<Object, String> {

	static final Logger logger = LogManager.getLogger(MgMemberHandler.class);
    private DynamoDB dynamoDb;
    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
    private DynamoDBMapper dynamoDBMapper; 
    private MgMemberService service = new MgMemberService(client);
    private MgClientService clientService = new MgClientService(client);
    private DynamoDBMapperQueryScanExample QueryScantService = new DynamoDBMapperQueryScanExample(client);
    private DocumentAPIItemCRUDExample crude = new DocumentAPIItemCRUDExample();

    
    private String DYNAMODB_TABLE_NAME = "MG_MEMBER";
//    private String DYNAMODB_TABLE_NAME = "MG_CLIENT";
    
    public String handleRequest(Object event, Context context) {
    	
    	QueryScantService.getMember();
    	
//    	logger.info(event.toString(), QueryScantService.getMember());
//    	// event Map에 저장
//    	Map<String, String> eventMap = (Map<String, String>) event;
//    	// Map을 MgMemberTable에 저장
//    	MgMemberTable member = mapToMgMemberTable(eventMap);
    	// MgMemberTable 등록
//    	service.insertMember();
    	
    	
//    	//event를 Map에 저장
//    	Map<String, String> eventMapC = (Map<String, String>) event;
//    	//Map을 MgClientTable에 저장
//    	MgClientTable client = mapToMgClientTable(eventMapC);
//    	//MgClientTable 등록 실행
    	
//    	clientService.createTable(client);
    	
//    	crude.createItems();
    	
    	/**
    	 * batch 
    	 */
    	// event Map에 저장
//    	Map<String, String> eventMap = (Map<String, String>) event;
//    	service.batchMembers();
    	
		return "success!!" +  DYNAMODB_TABLE_NAME;

    }
    
    
    //MgClientTable에 등록할 데이터 생성
//	private MgClientTable mapToMgClientTable(Map<String, String> event) {
//		MgClientTable client = new MgClientTable();
//		ClientInfo clientInfo = new ClientInfo();
//		
//		client.setId(event.get("id"));
//		clientInfo.setAddress(event.get("address"));
//		clientInfo.setEmail_address(event.get("email_address"));
//		clientInfo.setFullName(event.get("full_name"));
//		client.setClientInfo(clientInfo);
//				
//		return client;
//	}
    
//    private MgMemberTable mapToMgMemberTable(Map<String, String> event) {
//    	//json으로 등록할 데이터 생성
//    	MgMemberTable member = new MgMemberTable();
//    	member.setId(event.get("id"));
//    	member.setMgName(event.get("mg_name"));
//    	member.setBirth_date(event.get("birth_date"));
//    	member.setEmail_address(event.get("email_address"));
//    	
//    	return member;
//    }

    	

    
}