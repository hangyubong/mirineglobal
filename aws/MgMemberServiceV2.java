package com.amazonaws.lambda.demo.service;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.backup.EnhancedPutItem;
import com.amazonaws.lambda.demo.table.MgMember;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;
import com.amazonaws.lambda.demo.utils.TableMappingUtil;

import constants.Constants;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryRequest;
import software.amazon.awssdk.services.dynamodb.model.QueryResponse;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class MgMemberServiceV2 {
	
	final Logger logger = LogManager.getLogger(EnhancedPutItem.class);
	
	private DynamoDbEnhancedClient enhancedClient;
	private DynamoDbClient ddb;

	//--???????????????. dynamoDB SDK v2????????? ??????????????? ????????? ?????????????????? ??????????????? ?????????????????? ???????????????.
	//	\> ????????????????????? DynamoDbTable<MgMemberTableV2> memberTable = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
	//	\> ??????????????? fromBean??? ?????? ????????? ????????? ????????????, **?????? ????????? ????????? ????????? ???????????????????????? ???????????? ?????? ???????????? ??????????????? ????????? ??????????????? ??????.**
	private final TableSchema<MgMemberTableV2> TABLE_SCHEMA = TableSchema.builder(MgMemberTableV2.class)
			.newItemSupplier(MgMemberTableV2::new)
			.addAttribute(String.class,
					a -> a.name(Constants.ID).getter(MgMemberTableV2::getId).setter(MgMemberTableV2::setId)
							.tags(StaticAttributeTags.primaryPartitionKey()))
			.addAttribute(String.class,
					a -> a.name(Constants.NAME).getter(MgMemberTableV2::getMgName).setter(MgMemberTableV2::setMgName))
			.addAttribute(String.class,
					a -> a.name(Constants.BIRTHDATE).getter(MgMemberTableV2::getBirth_date)
							.setter(MgMemberTableV2::setBirth_date))
			.addAttribute(String.class,
					a -> a.name(Constants.EMAIL_ADDRESS).getter(MgMemberTableV2::getEmail_address)
							.setter(MgMemberTableV2::setEmail_address))
			.addAttribute(String.class,
					a -> a.name(Constants.CREATED_AT).getter(MgMemberTableV2::getCreated_at)
							.setter(MgMemberTableV2::setCreated_at))
			.addAttribute(String.class,
					a -> a.name(Constants.UPDATED_AT).getter(MgMemberTableV2::getUpdated_at)
							.setter(MgMemberTableV2::setUpdated_at))
			.addAttribute(String.class,
					a -> a.name(Constants.INSERT_USER).getter(MgMemberTableV2::getInsert_user)
							.setter(MgMemberTableV2::setInsert_user))
			.addAttribute(String.class,
					a -> a.name(Constants.UPDATED_USER).getter(MgMemberTableV2::getUpdated_user)
							.setter(MgMemberTableV2::setUpdated_user))
			.addAttribute(int.class, a -> a.name(Constants.VERSION).getter(MgMemberTableV2::getVersion)
					.setter(MgMemberTableV2::setVersion))
			.build();

	public MgMemberServiceV2() { //??????????????? ??????????????? ????????????
		ddb = DynamoDbClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_1).build();
		enhancedClient = software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.builder().dynamoDbClient(ddb)
				.build();
	}

	// dynamoDB sdk_v2 1??? ??????
	public void putMember(DynamoDbEnhancedClient enhancedClient) {

		try {
			
//			DynamoDbTable<MgMemberTableV2> mgMemberTableV2 = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
			// \ ->v2????????? TableSchema<>??? ??????????????? ??????????????????. ?????? ????????????????????????????????? ??? ?????? ???????????? ?????????????????? ??????????????? ??????????????? ?????????????????? ???????????? ????????????????????? ?????????????????????
			// \ -->v2????????? TableSchema<>??? ???????????? ??????????????? ????????????.
			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TABLE_SCHEMA);
			// Populate the Table.
			MgMemberTableV2 MgMember = new MgMemberTableV2();

			MgMember.setId("mg16");
			MgMember.setMgName("hana");
			MgMember.setBirth_date("1999/11/02");
			MgMember.setEmail_address("hana@gmail.com");
			MgMember.setCreated_at(getNowTime());
			MgMember.setUpdated_at(getNowTime());
			MgMember.setInsert_user("insert-admin");
			MgMember.setUpdated_user("insert-admin");
			MgMember.setVersion(0);
			// Put the customer data into an Amazon DynamoDB table.
			mgMemberTable.putItem(MgMember);
			logger.info(MgMember.getMgName().toString());

			// Put the customer data into an Amazon DynamoDB table.
			mgMemberTable.putItem(MgMember);

		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
		}

	}

	// dynamoDB sdk_v2 ?????? ??????
	public final void putBatchRecords(DynamoDbEnhancedClient enhancedClient) {//
		/*
		 * --Code Samle guilde =
		 * https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb
		 */

		DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TABLE_SCHEMA);
		List<MgMemberTableV2> members = new ArrayList<MgMemberTableV2>();
		MgMemberTableV2 mg15 = new MgMemberTableV2();
		mg15.setId("mg15");
		mg15.setMgName("rara");
		mg15.setBirth_date("1999/11/02");
		mg15.setEmail_address("rara@gmail.com");
		mg15.setCreated_at(getNowTime());
		mg15.setUpdated_at(getNowTime());
		mg15.setInsert_user("insert-admin");
		mg15.setUpdated_user("insert-admin");
		mg15.setVersion(0);
		members.add(mg15);

		MgMemberTableV2 mg16 = new MgMemberTableV2();
		mg16.setId("mg16");
		mg16.setMgName("hana");
		mg16.setBirth_date("1999/11/02");
		mg16.setEmail_address("hana@gmail.com");
		mg16.setCreated_at(getNowTime());
		mg16.setUpdated_at(getNowTime());
		mg16.setInsert_user("insert-admin");
		mg16.setUpdated_user("insert-admin");
		mg16.setVersion(0);
		members.add(mg16);
		
		MgMemberTableV2 mg17 = new MgMemberTableV2();
		mg17.setId("mg17");
		mg17.setMgName("hana");
		mg17.setBirth_date("1999/11/02");
		mg17.setEmail_address("hana@gmail.com");
		mg17.setCreated_at(getNowTime());
		mg17.setUpdated_at(getNowTime());
		mg17.setInsert_user("insert-admin");
		mg17.setUpdated_user("insert-admin");
		mg17.setVersion(0);
		members.add(mg17);
		
		MgMemberTableV2 mg18 = new MgMemberTableV2();
		mg18.setId("mg18");
		mg18.setMgName("hana");
		mg18.setBirth_date("1999/11/02");
		mg18.setEmail_address("hana@gmail.com");
		mg18.setCreated_at(getNowTime());
		mg18.setUpdated_at(getNowTime());
		mg18.setInsert_user("insert-admin");
		mg18.setUpdated_user("insert-admin");
		mg18.setVersion(0);
		members.add(mg18);

		BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
				.writeBatches(WriteBatch.builder(MgMemberTableV2.class).mappedTableResource(mgMemberTable)
						.addPutItem(r -> r.item(mg15))
						.addPutItem(r -> r.item(mg16))
						.addPutItem(r -> r.item(mg17))
						.addPutItem(r -> r.item(mg18)).build())
				.build();
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));
		logger.info(members.get(0).id + " ??????!!");
		logger.info(members.get(1).id + " ??????!!");
		logger.info(members.get(2).id + " ??????!!");
		logger.info(members.get(3).id + " ??????!!");

	}
	
	/**
	 * ????????? ??????
	 * 
	 * @param input
	 * ***Map??? input?????? ????????? ????????????????????? ????????????????????? json????????????. Map??? input?????? ???????????? ???????????????????????? nullpoint?????? ???????????? ??????.
	 * \>?????? event test??? ?????????????????? handler?????? ????????? ?????? ???????????? event??? ????????? ?????????????????? ????????????. 
	 */

	public void createMembers(Object input) { //dynamoDB sdk_v2 ?????? ??????. -- for???.
		try {

			// input ???????????? ????????? ??????
			Map<String, String> m = (Map<String, String>) input;
			// DynamoDb table ??????
			// TODO ????????? ?????? table??? ???????????? Table????????? ???????????? ????????? ???????????? ????????????.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
//					TableSchema.fromBean(MgMemberTableV2.class));
			// TODO ?????????, ??????????????? TABLE_SCHEMA??? ???????????? ????????? AttributeName, AttributeValue??? ????????? ???
			// ????????? ??????.
			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TABLE_SCHEMA);

			List<MgMember> members = new ArrayList<>();
			int num = 101;
			for (int i = 0; i < 5; i++) {
				// ????????? ????????? ??????
				MgMember member = new MgMember(m.get(Constants.ID), m.get(Constants.NAME), m.get(Constants.BIRTHDATE),
						m.get(Constants.EMAIL_ADDRESS));
				// string ?????? ???????????? id??????
				String id = m.get(Constants.ID).substring(0, m.get(Constants.ID).length() - 1) + String.valueOf(num);
				// ??????????????? ???????????? ?????? id??? ????????? ????????? ??????
				member.setId(id);
				members.add(member);
				// id += 1
				num++;
			}

			// dynamoDB Table ??????
			List<MgMemberTableV2> insertMembers = new ArrayList<>();
			for (MgMember mem : members) {
				// 1?????? ???????????? MgMemberTable??? ??????????????????
				MgMemberTableV2 insertMember = TableMappingUtil.memberToMgMemberTable(mem);
				DynamoDBUtils.insertCommonItemV2(insertMember);
				// ?????? ????????? ????????? ??????
				logger.info("????????? ????????? ??????  mgMember.toString() : " + insertMember.toString());
				// ???????????? add?????? ??????
				insertMembers.add(insertMember);
			}
			BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
					.writeBatches(WriteBatch.builder(MgMemberTableV2.class).mappedTableResource(mgMemberTable)
							.addPutItem(r -> r.item(insertMembers.get(0))).addPutItem(r -> r.item(insertMembers.get(1)))
							.addPutItem(r -> r.item(insertMembers.get(2))).addPutItem(r -> r.item(insertMembers.get(3)))
							.addPutItem(r -> r.item(insertMembers.get(4))).build())
					.build();

			// Add these two items to the table.
			enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
			logger.info("batchWriteItem() end");
			/**
			 * ????????? ??????
			 */
			// ????????? ????????? ??????

			// close()
			ddb.close();
		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
		}

	}
	
	
	// dynamoDB sdk_v2 ?????? ??????
	/*???????????? ??????????????? ????????? ?????? ????????? ????????? ????????? set??? ????????? ???????????? ?????? ?????? ????????????
	 * \>>>(*??????*)???????????? ?????? ???????????? ???????????? ???????????? ?????? ???????????? ??????????????? ????????????*/
	public final void deleteBatchMembers(DynamoDbEnhancedClient enhancedClient) {
		/*
		 * --Code Samle guilde =
		 * https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb
		 */

		DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TABLE_SCHEMA);
		List<MgMemberTableV2> members = new ArrayList<MgMemberTableV2>();
		MgMemberTableV2 member1 = new MgMemberTableV2();
		member1.setId("j1101");
		members.add(member1);

		MgMemberTableV2 member2 = new MgMemberTableV2();
		member2.setId("j1102");
		members.add(member2);

		BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
				.writeBatches(WriteBatch.builder(MgMemberTableV2.class).mappedTableResource(mgMemberTable)
						.addDeleteItem(member1)
						.addDeleteItem(member2).build())
				.build();
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));
		logger.info(members.get(0).id + " ??????!!");
		logger.info(members.get(1).id + " ??????!!");
		
	}	
	
	



	// 1??? ?????? (??????1)
	public void deleteDymamoDBItem(DynamoDbClient ddb) {
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg16";

		HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());

		DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();

		try {
			ddb.deleteItem(deleteReq);
		} catch (DynamoDbException e) {

		}
	}
	
	// 1??? ?????? (??????2) --**?????????????????? ??????????????? ????????????
	public void deleteMember(DynamoDbClient ddb ) {
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg16";
        HashMap<String,AttributeValue> keyToGet = new HashMap<String,AttributeValue>();

        keyToGet.put(key, AttributeValue.builder()
                .s(keyVal).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyToGet)
                .tableName(tableName)
                .build();

        try {
            Map<String,AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem != null) {
                Set<String> keys = returnedItem.keySet();
                System.out.println("Amazon DynamoDB table attributes: \n");

                for (String key1 : keys) {
                    System.out.format("%s: %s\n", key1, returnedItem.get(key1).toString());
                }
            } else {
                System.out.format("No item found with the key %s!\n", key);
            }
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
		
	}


	// dynamoDB sdk_v2 1??? ????????????
	public void updateTableItem(DynamoDbClient ddb) {
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg14";
		String name = "updated_user";
		String updateVal = "insert-admin";

		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(key, AttributeValue.builder().s(keyVal).build());
		HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();

		updatedValues.put(name, AttributeValueUpdate.builder().value(AttributeValue.builder().s(updateVal).build())
				.action(AttributeAction.PUT).build());

		UpdateItemRequest request = UpdateItemRequest.builder().tableName(tableName).key(itemKey)
				.attributeUpdates(updatedValues).build();

		try {
			ddb.updateItem(request);
		} catch (ResourceNotFoundException e) {

		} catch (DynamoDbException e) {

		}

	}


	
	
	
	
    public void scanItems( DynamoDbClient ddb) { //??????1.
    	MgMemberTableV2 memberV2 = new MgMemberTableV2();
    		String tableName = "MG_MEMBER";

        try {
            ScanRequest scanRequest = ScanRequest.builder()
                    .tableName(tableName)
                    .build();

            ScanResponse response = ddb.scan(scanRequest);
            for (Map<String, AttributeValue> item : response.items()) {
                Set<String> keys = item.keySet();
                for (String key : keys) {
                    logger.info("The key name is "+key +"\n" );
                    logger.info("The value is "+item.get(key).s());
                }
            }

        } catch (DynamoDbException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    public void scan(DynamoDbEnhancedClient enhancedClient) { //??????2.

        try{
            DynamoDbTable<MgMemberTableV2> memberTableV2 = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
            Iterator<MgMemberTableV2> results = memberTableV2.scan().items().iterator();
            while (results.hasNext()) {

            	MgMemberTableV2 rem = results.next();
                logger.info("getId? "+rem.getId());
                logger.info("getMgName? " +rem.getMgName());
                logger.info("getBirth_date? " +rem.getBirth_date());
                logger.info("getMgName? " +rem.getEmail_address());
            }

        } catch (DynamoDbException e) {
        	logger.info(e.getMessage());
        }
        System.out.println("Done");
    }
    public int queryTable(DynamoDbClient ddb) { //??????1 - ??????????????? ??????????????? ??? ??????
        
	    String tableName = "MG_MEMBER";
	    String partitionKeyName = "id";
	    String partitionKeyVal = "mg16";
	    String partitionAlia = "#a";

	    MgMemberTableV2 memberV2 = new MgMemberTableV2();
	    List<MgMemberTableV2> result = new ArrayList<MgMemberTableV2>();
	    
        // Set up an alias for the partition key name in case it's a reserved word.
	    // (????????? ????????? ?????? ????????? ??? ????????? ?????? ????????? ???????????????.)
        HashMap<String,String> attrNameAlias = new HashMap<String,String>();
//        attrNameAlias.put(partitionAlia, partitionKeyName);
        attrNameAlias.put(partitionKeyName, partitionKeyVal);

        // Set up mapping of the partition name with the value.
        // (????????? ????????? ?????? ????????? ???????????????.)
        HashMap<String, AttributeValue> attrValues = new HashMap<>();
        attrValues.put(" : " + partitionKeyName, AttributeValue.builder()
                .s(partitionKeyVal)
                .build());
        QueryRequest queryReq = QueryRequest.builder()
                .tableName(tableName)
                .keyConditionExpression(partitionAlia + " = :" + partitionKeyName)
                .expressionAttributeNames(attrNameAlias)
                .expressionAttributeValues(attrValues)
                .build();

        try {
            QueryResponse response = ddb.query(queryReq);
            return response.count();
        } catch (DynamoDbException e) {
            logger.info("get id?" + result.get(0).getId());
        }
        return -1;
    }
    
    public String queryTable2(DynamoDbEnhancedClient enhancedClient) {

        try{
            DynamoDbTable<MgMemberTableV2> mappedTable = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue("mg14")
                            .build());

            // Get items in the table and write out the ID value.
            Iterator<MgMemberTableV2> results = mappedTable.query(queryConditional).items().iterator();
            String result="";

            while (results.hasNext()) {
            	MgMemberTableV2 rem = results.next();
                result = rem.getId();
                logger.info("getMgName ? " + rem.getMgName());
                logger.info("getBirth_date ? " + rem.getBirth_date());
                logger.info("getEmail_address ? " + rem.getEmail_address());
            }
            return result;

        } catch (DynamoDbException e) {
        	logger.info(e.getMessage());
        	logger.traceExit();
        }
        return "";
    }   
    
	private static String getNowTime() { // ?????? ??? ?????? ???????????? ?????????
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime ?????? ?????????????????? JST ????????????
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowDate = formatter.format(zdtjst);

		return nowDate;

	}// end getNowTime()

    
    
    
}//END class
