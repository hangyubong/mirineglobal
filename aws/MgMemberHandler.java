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
import com.amazonaws.lambda.demo.backup.DocumentAPIItemCRUDExample;
import com.amazonaws.lambda.demo.backup.DynamoDBMapperCRUDExample;
import com.amazonaws.lambda.demo.backup.EnhancedPutItem;
import com.amazonaws.lambda.demo.backup.Query;
import com.amazonaws.lambda.demo.guide.DynamoDBMapperQueryScanExample;
import com.amazonaws.lambda.demo.service.MgMemberService;
import com.amazonaws.lambda.demo.service.MgMemberServiceV2;
import com.amazonaws.lambda.demo.service.MgClientService;
import com.amazonaws.lambda.demo.service.MgClientService2;
import com.amazonaws.lambda.demo.table.ClientInfo;
import com.amazonaws.lambda.demo.table.MgClientTable;
import com.amazonaws.lambda.demo.table.MgMemberTable;

public class MgMemberHandler implements RequestHandler<Object, String> {
	DynamoDbClient ddb;
	static final Logger logger = LogManager.getLogger(MgMemberHandler.class);
    private DynamoDB dynamoDb;
    private AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.AP_NORTHEAST_1).build();
    private DynamoDBMapper dynamoDBMapper; 
    private MgMemberService service = new MgMemberService(client);
    private MgMemberServiceV2 serviceV2 = new MgMemberServiceV2();
    private MgClientService clientService = new MgClientService(client);
    private MgClientService2 clientService2 = new MgClientService2();
	private MgMemberServiceV2 dbScanItems = new MgMemberServiceV2();
	private Query query = new Query();
    
    private EnhancedPutItem enhancedPutItem; //dynamoDB sdk_v2 
    
//    private String DYNAMODB_TABLE_NAME = "MG_MEMBER";
    private String DYNAMODB_TABLE_NAME = "MG_CLIENT";
   
    public String handleRequest(Object event, Context context) {
    	DynamoDbClient ddb = DynamoDbClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_1).build();
    	DynamoDbEnhancedClient enhancedClient = software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.builder().dynamoDbClient(ddb)
    				.build();
    	
    	excuteV2();
    	//================MG_CLIENT / SDK V2===================================
//    	clientService2.putClient(); //1??? ??????
//    	clientService2.putClients(enhancedClient); //?????? ??????
    	clientService2.createClients(enhancedClient, event); //?????? ?????? - json?????? / for??? 
//    	clientService2.deleteClient(ddb); //1??? ??????
//    	clientService2.getClient(ddb); //1??? ??????
//    	clientService2.deleteBatchClients(enhancedClient); //?????? ??????
//    	clientService2.updateTableClientV(ddb); //1??? ????????????
//    	clientService2.scanClient(enhancedClient); //scan
//    	clientService2.queryCliient(enhancedClient); //query
    	
    	
    	//================MG_MEMBER / SDK V2===================================
//    	serviceV2.putMember(enhancedClient); //dynamoDB sdk_v2 1??? ??????.
//    	serviceV2.deleteDymamoDBItem(ddb);//dynamoDB sdk_v2 1??? ??????.
//    	serviceV2.deleteMember(ddb);//dynamoDB sdk_v2 1??? ??????.
//    	serviceV2.updateTableItem(ddb);//dynamoDB sdk_v2 1??? ????????????.	
//    	serviceV2.createMembers(event); //dynamoDB sdk_v2 ?????? ??????. -- for???.
//    	serviceV2.putBatchRecords(enhancedClient); //dynamoDB sdk_v2 ?????? ??????.
//    	serviceV2.deleteBatchMembers(enhancedClient);//dynamoDB sdk_v2 ?????? ??????.
//    	dbScanItems.scanItems(ddb); //dynamoDB sdk_v2 scan ????????? ??????.
//    	dbScanItems.scan(enhancedClient); //dynamoDB sdk_v2 scan ????????? ??????.
//    	serviceV2.queryTable2(enhancedClient); //dynamoDB sdk_v2 query ????????? ??????.
//    	serviceV2.queryTable(ddb) ; //dynamoDB sdk_v2 query ????????? ??????.	
//    	//================END MG_MEMBER / SDK V2===================================
    	
//=================================================event test json?????? ?????????-- ===========================================================  
    	excuteV1();
//    	service.getMember(); //Query ??? Scan?????? ????????? ??????. / global index??? ????????? ??????.	
//    	logger.info(event.toString(), service.getMember());    	
    	
    	
//    	// event Map??? ??????
//    	Map<String, String> eventMap = (Map<String, String>) event;
//    	// Map??? MgMemberTable??? ??????
//    	MgMemberTable member = mapToMgMemberTable(eventMap);
    	// MgMemberTable ??????
    	
//    	service.insertMember();
    	
//    	//event??? Map??? ??????
//    	Map<String, String> eventMapC = (Map<String, String>) event;
//    	//Map??? MgClientTable??? ??????
//    	MgClientTable client = mapToMgClientTable(eventMapC);
//    	//MgClientTable ?????? ??????
    	
//    	clientService.createTable(client);
    	
//    	crude.createItems();
//    	crude.deleteItem();
    	
//    	MapperCRUD.testCRUDOperations(); //member 1??? ??????
//    	service.batchDelete();
    	
    	/**
    	 * batch 
    	 */
    	// event Map??? ??????
//    	Map<String, String> eventMap = (Map<String, String>) event;
//    	service.batchMembers();
    	
		return "success!!" +  DYNAMODB_TABLE_NAME;

    }

private void excuteV1() {
	// TODO Auto-generated method stub
	
}

private void excuteV2() {
	// TODO Auto-generated method stub
	

	
}
    

    //MgClientTable??? ????????? ????????? ??????
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
//    	//json?????? ????????? ????????? ??????
//    	MgMemberTable member = new MgMemberTable();
//    	member.setId(event.get("id"));
//    	member.setMgName(event.get("mg_name"));
//    	member.setBirth_date(event.get("birth_date"));
//    	member.setEmail_address(event.get("email_address"));
//    	
//    	return member;
//    }

    	

    
}