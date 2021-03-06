package com.amazonaws.lambda.demo.service;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.table.BaseTable;
import com.amazonaws.lambda.demo.table.MgMember;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;
import com.amazonaws.lambda.demo.table.MgMemberTableV3;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;
import com.amazonaws.lambda.demo.utils.TableMappingUtil;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.sun.tools.javac.util.Log;

/*
 * DynamoDB SDK_v2??? ???????????? ????????? ??????.
 * Before running this code example, create an Amazon DynamoDB table named Customer with these columns:
 *   - id - the id of the record that is the key
 *   - custName - the customer name
 *   - email - the email value
 *   - registrationDate - an instant value when the item was added to the table
 *
 * Also, ensure that you have set up your development environment, including your credentials.
 *
 * For information, see this documentation topic:
 *
 * https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/get-started.html
 * 
 * Code Sample url = https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/code-catalog-javav2-example_code-dynamodb.html
 */
@DynamoDbBean
public class EnhancedPutItem {// 1???, ????????? - ??????, ??????, ??????
	
	static final Logger logger = LogManager.getLogger(EnhancedPutItem.class);
	private static DynamoDbEnhancedClient enhancedClient;
//	private static DynamoDbClient ddb;

	// dynamoDB sdk_v2 1??? ??????
	public static final void putRecord(DynamoDbEnhancedClient enhancedClient) {

		try {
			
			DynamoDbTable<MgMemberTableV2> mgMemberTableV2 = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));

			// Populate the Table.
			MgMemberTableV2 MgMemberRecord = new MgMemberTableV2();

			MgMemberRecord.setId("mg15");
			MgMemberRecord.setMgName("nami");
			MgMemberRecord.setBirth_date("1988/11/02");
			MgMemberRecord.setEmail_address("nami@gmail.com");
			MgMemberRecord.setCreated_at(getNowTime());
			MgMemberRecord.setUpdated_at(getNowTime());
			MgMemberRecord.setInsert_user("insert-admin");
			MgMemberRecord.setUpdated_user("insert-admin");
			MgMemberRecord.setVersion(0);

			// Put the customer data into an Amazon DynamoDB table.
			mgMemberTableV2.putItem(MgMemberRecord);
			logger.info(MgMemberRecord.getMgName().toString());

			// Put the customer data into an Amazon DynamoDB table.
			mgMemberTableV2.putItem(MgMemberRecord);

		} catch (DynamoDbException e) {

		}

	}

	
	// dynamoDB sdk_v2 ?????? ??????
	public final static void putBatchRecords(DynamoDbEnhancedClient enhancedClient) {// ????????????. -- ????????????????????? ??????????????? ??????????????? ?????????.

		/* --Code Samle guilde = https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb-src-main-java-com-example-dynamodb-EnhancedBatchWriteItems.java.html
		 * --.addPutItem??? ??????????????? ???????????????????????? ???????????????????????? ????????????.(?????????????????????.) --BatchWriteItemEnhancedRequest????????? set?????? ?????? ?????????????????? ????????????.
		 * -- ???????????? ?????? ???????????? ?????? add??? ????????????????????? ????????? ??????????????? ???????????? ????????? ?????? ??????????????? item??? ??????????????? ?????????.
		 * 	-->(ex:"mg15"??????????????? members.get(0).id??? "mg15"??? ?????????,?????? ???????????? ??? ???????????? ??????????????? \
		 *          members.get(0).id, members.get(1).id??? ????????? ?????? ????????? "mg16"??? ?????? ????????????..
		 */
		
		DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",TableSchema.fromBean(MgMemberTableV2.class));
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
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest =
                BatchWriteItemEnhancedRequest.builder().writeBatches( WriteBatch.builder(MgMemberTableV2.class)
                                        .mappedTableResource(mgMemberTable)
                                        .addPutItem(r -> r.item(mg15)).build()).build();     
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));			
		logger.info(members.get(0).id);

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
		logger.info(members.get(0).id);
		logger.info(members.get(1).id);		
		
        BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest2 =
                BatchWriteItemEnhancedRequest.builder().writeBatches(WriteBatch.builder(MgMemberTableV2.class)
                                        .mappedTableResource(mgMemberTable)
                                        .addPutItem(r -> r.item(mg16)).build()).build();     
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest2));
		logger.info(members.get(0).id);
		logger.info(members.get(1).id);
		
	}
	

	
	/**
	 * ????????? ??????
	 * @param input
	 */
//	private static final TableSchema<MgMemberTableV2> TABLE_SCHEMA =
//            StaticTableSchema.builder(MgMemberTableV2.class)
//                    .newItemSupplier(MgMemberTableV2::new)
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.id)
//                            .getter(MgMemberTableV2::getId)
//                            .setter(MgMemberTableV2::setId)
//                            .tags(StaticAttributeTags.primaryPartitionKey()))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.mg_name)
//                            .getter(MgMemberTableV2::getMgName)
//                            .setter(MgMemberTableV2::setMgName))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.birth_date)
//                            .getter(MgMemberTableV2::getBirth_date)
//                            .setter(MgMemberTableV2::setBirth_date))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.email_address)
//                            .getter(MgMemberTableV2::getEmail_address)
//                            .setter(MgMemberTableV2::setEmail_address))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.created_at)
//                    		.getter(MgMemberTableV2::getCreated_at)
//                    		.setter(MgMemberTableV2::setCreated_at))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.updated_at)
//                    		.getter(MgMemberTableV2::getUpdated_at)
//                    		.setter(MgMemberTableV2::setUpdated_at))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.insert_user)
//                    		.getter(MgMemberTableV2::getInsert_user)
//                    		.setter(MgMemberTableV2::setInsert_user))
//                    .addAttribute(String.class, a -> a.name(MgMemberTableV2.updated_user)
//                    		.getter(MgMemberTableV2::getUpdated_user)
//                    		.setter(MgMemberTableV2::setUpdated_user))
//                    .addAttribute(int.class, a -> a.name(MgMemberTableV2.version)
//                    		.getter(MgMemberTableV2::getVersion)
//                    		.setter(MgMemberTableV2::setVersion))
//                    .build();
	
//	public void createMembers(DynamoDbClient ddb, Object input) {
//		try {
//			
//			// input ???????????? ????????? ??????
//			Map<String, String> m = (Map<String, String>) input;
//			
//			// DynamoDb table ??????
//			// TODO ????????? ?????? table??? ???????????? Table????????? ???????????? ????????? ???????????? ????????????.
////			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
////					TableSchema.fromBean(MgMemberTableV2.class));
//			// TODO ?????????, ??????????????? TABLE_SCHEMA??? ???????????? ????????? AttributeName, AttributeValue??? ????????? ??? ????????? ??????.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
//			
//			List<MgMember> members = new ArrayList<>();
//			int num = 101;
//			for (int i = 0; i < 5; i++) {
//				// ????????? ????????? ??????
//				MgMember member = new MgMember(m.get(MgMember.), m.get(MgMemberTableV2.mg_name), m.get(MgMemberTableV2.birth_date),
//						m.get(MgMemberTableV2.birth_date));
//				// string ?????? ???????????? id??????
//				String id = m.get(MgMemberTableV2.id).substring(0, m.get(MgMemberTableV2.id).length()-1) + String.valueOf(num);
//				// ??????????????? ???????????? ?????? id??? ????????? ????????? ??????
//				member.setId(id);
//				members.add(member);
//				// id += 1
//				num++;
//			}
//			
//			// dynamoDB Table ??????
//			List<MgMemberTableV3> insertMembers = new ArrayList<>();
//			for(MgMember mem : members) {
//				// 1?????? ???????????? MgMemberTable??? ??????????????????
//				MgMemberTableV3 insertMember = TableMappingUtil.memberToMgMemberTableV3(mem);
//				DynamoDBUtils.insertCommonItemV2(insertMember);
//				// ?????? ????????? ????????? ??????	
//				logger.info("????????? ????????? ??????  mgMember.toString() : " + insertMember.toString());
//				// ???????????? add?????? ??????
//				insertMembers.add(insertMember);
//			}
//			BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
//					.writeBatches(WriteBatch.builder(MgMemberTableV2.class).mappedTableResource(mgMemberTable)
//							.addPutItem(r -> r.item(insertMembers.get(0)))
//							.addPutItem(r -> r.item(insertMembers.get(1)))
//							.addPutItem(r -> r.item(insertMembers.get(2)))
//							.addPutItem(r -> r.item(insertMembers.get(3)))
//							.addPutItem(r -> r.item(insertMembers.get(4))).build())
//					.build();
//???
//			// Add these two items to the table.
//			enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
//			logger.info("batchWriteItem() end");
//			/**
//			 * ????????? ??????
//			 */
//			// TODO ????????? ????????? ??????
//			
//			// close()
//			ddb.close();
//		} catch (DynamoDbException e) {
//			logger.error(e.getMessage());
//		}
//	}	
//	
	public void batchMembers() {

		try {
			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
					TableSchema.fromBean(MgMemberTableV2.class));
			List<MgMemberTableV2> MgMembers = new ArrayList<>();

			
			int cnt = 5;
			List<MgMember> members = new ArrayList<>();
			for (int i = 0; i < 2; i++) {
				MgMember me = new MgMember("mg015", "aaa", "1995/01/01", "test@mail.com");
				String id = "mg01" + String.valueOf(cnt);
				me.setId(id);
				members.add(me);
				
				cnt++;
			}
			
			for(MgMember m : members) {
				MgMembers.add(getMembers(m));
				logger.info(m.toString());
			}
			
		
//			MgMemberTableV2 MgMemberRecord1 = getMembers(new MgMember("mg015", "aaa", "1995/01/01", "test@mail.com"));
//			MgMembers.add(getMembers(new MgMember("mg015", "aaa", "1995/01/01", "test@mail.com")));
//			logger.info("who's name?" + MgMemberRecord1.getId());

//			MgMemberTableV2 MgMemberRecord2 = getMembers(new MgMember("mg016", "bbb", "1998/01/01", "test2@mail.com"));
//			MgMembers.add(getMembers(new MgMember("mg016", "bbb", "1998/01/01", "test2@mail.com")));
//			logger.info("who's name?" + MgMemberRecord2.getId());
//			logger.info("MgMemberRecord1 name?" + MgMemberRecord1.getMgName());
//			logger.info("MgMembers1 ?" + MgMembers.get(0).toString());
//			logger.info("MgMembers2 ?" + MgMembers.get(1).toString());
//			logger.info("MgMemberRecord2 name?" + MgMemberRecord2.getMgName());
//			BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
//					.writeBatches(WriteBatch.builder(MgMemberTableV2.class).mappedTableResource(mgMemberTable)
//							.addPutItem(r -> r.item(MgMembers.get(0)))
//							.addPutItem(r -> r.item(MgMembers.get(1)))
//							.build())
//					.build();
			
			 BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest =
	                    BatchWriteItemEnhancedRequest.builder()
	                            .writeBatches(
	                                    WriteBatch.builder(MgMemberTableV2.class)
	                                            .mappedTableResource(mgMemberTable)
	                                            .addPutItem(r -> r.item(MgMembers.get(0)))
	                                            .addPutItem(r -> r.item(MgMembers.get(1)))
	                                            .build())
	                            .build();
			// Add these two items to the table.
			enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);

		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
		}
	}

	private MgMemberTableV2 getMembers(MgMember m) {
		MgMemberTableV2 MgMemberRecord = new MgMemberTableV2();
		MgMemberRecord.setId(m.getId());
		MgMemberRecord.setMgName(m.getMgName());
		MgMemberRecord.setBirth_date(m.getBirthDate());
		MgMemberRecord.setEmail_address(m.getEmailAddress());
		MgMemberRecord.setCreated_at(getNowTime());
		MgMemberRecord.setUpdated_at(getNowTime());
		MgMemberRecord.setInsert_user("insert");
		MgMemberRecord.setUpdated_user("insert");
		MgMemberRecord.setVersion(0);
		return MgMemberRecord;
	}

	// dynamoDB sdk_v2 -- delete 1??? ??????
	public static void deleteDymamoDBItem(DynamoDbClient ddb) {
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg00";

		HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());

		DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();

		try {
			ddb.deleteItem(deleteReq);
		} catch (DynamoDbException e) {

		}
	}
	
	// dynamoDB sdk_v2 -- delete ?????? ??????
	public static void deleteDymamoDBItems(DynamoDbClient ddb) {//?????? ??????.
		/* -- ??????????????? ??????. ??? ???????????? ?????? ?????????????????? ????????? ???????????? ???????????? ????????? ????????? ???????????? ????????? ???. */
	
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg16";
	
		HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());
		DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();
		if (keyVal == "mg16") {
			logger.info(getNowTime() + keyVal + " ???????????? ?????????????????????!");
		}else if (keyVal == null) {
			logger.info(getNowTime() + keyVal + " ???????????? ????????????.");
		}else {
			logger.info("?????? ??????????????? ?????????????????????.");
		}
		
		String keyVal2 = "mg15";
		keyToGet.put(key, AttributeValue.builder().s(keyVal2).build());
		DeleteItemRequest deleteReq2 = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();
		if (keyVal2 == "mg15") {
			logger.info(getNowTime() + keyVal2 + " ???????????? ?????????????????????!");
		}else if (keyVal2 == null) {
			logger.info(getNowTime() + keyVal2 + " ???????????? ????????????.");
		}else {
			logger.info("?????? ??????????????? ?????????????????????.");
		}
		
		try {
			ddb.deleteItem(deleteReq);
			ddb.deleteItem(deleteReq2);
		} catch (DynamoDbException e) {

		}
	}

	// dynamoDB sdk_v2 1??? ????????????
    public static void updateTableItem(DynamoDbClient ddb) {
        String tableName = "MG_MEMBER";
        String key = "id";
        String keyVal = "mg14";
        String name = "updated_user";
        String updateVal = "insert-admin";

		HashMap<String,AttributeValue> itemKey = new HashMap<>();
		itemKey.put(key, AttributeValue.builder().s(keyVal).build());
		HashMap<String,AttributeValueUpdate> updatedValues = new HashMap<>();
		
		
		updatedValues.put(name, AttributeValueUpdate.builder()
		.value(AttributeValue.builder().s(updateVal).build())
		.action(AttributeAction.PUT)
		.build());
		
		UpdateItemRequest request = UpdateItemRequest.builder()
		.tableName(tableName)
		.key(itemKey)
		.attributeUpdates(updatedValues)
		.build();
		
		try {
		ddb.updateItem(request);
		} catch (ResourceNotFoundException e) {

		} catch (DynamoDbException e) {

		}
	
	}
	
	
	private static String getNowTime() { // ?????? ??? ?????? ???????????? ?????????
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime ?????? ?????????????????? JST ????????????
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowDate = formatter.format(zdtjst);

		return nowDate;

	}// end getNowTime()



}