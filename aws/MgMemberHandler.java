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

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.text.ParseException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.Handler;
import com.amazonaws.lambda.demo.guide.DynamoDBMapperQueryScanExample;
import com.amazonaws.lambda.demo.service.MgMemberService;
import com.amazonaws.lambda.demo.service.DocumentAPIItemCRUDExample;
import com.amazonaws.lambda.demo.service.DynamoDBMapperCRUDExample;
import com.amazonaws.lambda.demo.service.EnhancedPutItem;
import com.amazonaws.lambda.demo.service.MgClientService;
import com.amazonaws.lambda.demo.table.ClientInfo;
import com.amazonaws.lambda.demo.table.MgClientTable;
import com.amazonaws.lambda.demo.table.MgMemberTable;

public class MgMemberHandler implements RequestHandler<Object, String> {
	DynamoDbClient ddb;
	static final Logger logger = LogManager.getLogger(MgMemberHandler.class);
//	private static final DynamoDbEnhancedClient DynamoDbEnhancedClient = null;
//    private DynamoDB dynamoDb;
//    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
//    private DynamoDBMapper dynamoDBMapper; 
//    private MgMemberService service = new MgMemberService(client);
//    private MgClientService clientService = new MgClientService(client);
//    private DynamoDBMapperQueryScanExample QueryScantService = new DynamoDBMapperQueryScanExample(client);
//    private DocumentAPIItemCRUDExample crude = new DocumentAPIItemCRUDExample();
//    private DynamoDBMapperCRUDExample MapperCRUD = new DynamoDBMapperCRUDExample();
    
    private EnhancedPutItem enhancedPutItem = new EnhancedPutItem(); //dynamoDB sdk_v2 
    
    private String DYNAMODB_TABLE_NAME = "MG_MEMBER";
//    private String DYNAMODB_TABLE_NAME = "MG_CLIENT";
   
    public String handleRequest(Object event, Context context) {
    	DynamoDbClient ddb = DynamoDbClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_1).build();
    	DynamoDbEnhancedClient enhancedClient = software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.builder().dynamoDbClient(ddb)
    				.build();
    	
//    	enhancedPutItem.putRecord(enhancedClient); //dynamoDB sdk_v2 1건 등록.
    	enhancedPutItem.putBatchRecords(enhancedClient); //dynamoDB sdk_v2 복수 등록.
//    	enhancedPutItem.batchMembers();
    	
//    	enhancedPutItem.deleteDymamoDBItem(ddb);//dynamoDB sdk_v2 1건 삭제.
//    	enhancedPutItem.updateTableItem(ddb);//dynamoDB sdk_v2 1건 업데이트.
    	   	
//    	QueryScantService.getMember(); //Query 및 Scant으로 데이터 취득. / global index로 데이터 취득.	
//    	logger.info(event.toString(), QueryScantService.getMember());
    	
//--event test json으로 등록시-- ===========================================================    	
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
//    	crude.deleteItem();
    	
//    	MapperCRUD.testCRUDOperations(); //member 1건 삭제
//    	service.batchDelete();
    	
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