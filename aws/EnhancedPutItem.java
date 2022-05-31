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
 * DynamoDB SDK_v2를 적용하여 데이터 등록.
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
public class EnhancedPutItem {// 1건, 복수건 - 등록, 갱신, 삭제
	
	static final Logger logger = LogManager.getLogger(EnhancedPutItem.class);
	private static DynamoDbEnhancedClient enhancedClient;
//	private static DynamoDbClient ddb;

	// dynamoDB sdk_v2 1건 등록
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

	
	// dynamoDB sdk_v2 복수 등록
	public final static void putBatchRecords(DynamoDbEnhancedClient enhancedClient) {// 복수등록. -- 가이드형식으로 처리시에는 중복키값이 발생됨.

		/* --Code Samle guilde = https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb-src-main-java-com-example-dynamodb-EnhancedBatchWriteItems.java.html
		 * --.addPutItem을 추가로시켜 복수등록하는걸로 가이드되어있으나 되지않음.(중복키에러가뜸.) --BatchWriteItemEnhancedRequest처리를 set밑에 각각 처리해주어야 등록가능.
		 * -- 리스트를 하나 사용하여 각각 add에 담아처리했지만 리스트 인덱스수는 늘어나나 마지막 등록 키값으로만 item이 덮어씌어져 들어옴.
		 * 	-->(ex:"mg15"처리시에는 members.get(0).id에 "mg15"가 있으나,이후 리스트에 더 추가하여 처리시에는 \
		 *          members.get(0).id, members.get(1).id을 확인시 나중 등록된 "mg16"이 두개 들어있음..
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
	 * 복수건 등록
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
//			// input 데이터를 모델에 매핑
//			Map<String, String> m = (Map<String, String>) input;
//			
//			// DynamoDb table 생성
//			// TODO 아래와 같이 table을 생성하면 Table모델의 변수명이 그대로 매핑되어 등록된다.
////			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
////					TableSchema.fromBean(MgMemberTableV2.class));
//			// TODO 따라서, 아래와같이 TABLE_SCHEMA를 생성해서 테이블 AttributeName, AttributeValue를 지정해 줄 필요가 있다.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER", TableSchema.fromBean(MgMemberTableV2.class));
//			
//			List<MgMember> members = new ArrayList<>();
//			int num = 101;
//			for (int i = 0; i < 5; i++) {
//				// 등록할 데이터 생성
//				MgMember member = new MgMember(m.get(MgMember.), m.get(MgMemberTableV2.mg_name), m.get(MgMemberTableV2.birth_date),
//						m.get(MgMemberTableV2.birth_date));
//				// string 으로 변환하여 id저장
//				String id = m.get(MgMemberTableV2.id).substring(0, m.get(MgMemberTableV2.id).length()-1) + String.valueOf(num);
//				// 복수데이터 생성하기 위해 id만 바꿔서 리스트 생성
//				member.setId(id);
//				members.add(member);
//				// id += 1
//				num++;
//			}
//			
//			// dynamoDB Table 매핑
//			List<MgMemberTableV3> insertMembers = new ArrayList<>();
//			for(MgMember mem : members) {
//				// 1건씩 매핑하여 MgMemberTable을 생성해준다음
//				MgMemberTableV3 insertMember = TableMappingUtil.memberToMgMemberTableV3(mem);
//				DynamoDBUtils.insertCommonItemV2(insertMember);
//				// 등록 실행전 데이터 확인	
//				logger.info("등록할 데이터 정보  mgMember.toString() : " + insertMember.toString());
//				// 리스트에 add하여 생성
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
//​
//			// Add these two items to the table.
//			enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
//			logger.info("batchWriteItem() end");
//			/**
//			 * 복수건 확인
//			 */
//			// TODO 등록된 데이터 확인
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

	// dynamoDB sdk_v2 -- delete 1건 삭제
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
	
	// dynamoDB sdk_v2 -- delete 복수 삭제
	public static void deleteDymamoDBItems(DynamoDbClient ddb) {//복수 삭제.
		/* -- 테이블명과 키값. 키 벨류값이 있고 동일테이블의 키값은 같으므로 벨류값만 찾아서 추가로 삭제처리 해주면 됨. */
	
		String tableName = "MG_MEMBER";
		String key = "id";
		String keyVal = "mg16";
	
		HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());
		DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();
		if (keyVal == "mg16") {
			logger.info(getNowTime() + keyVal + " 데이터가 삭제되었습니다!");
		}else if (keyVal == null) {
			logger.info(getNowTime() + keyVal + " 데이터가 없습니다.");
		}else {
			logger.info("로그 출력형식에 문제가있습니다.");
		}
		
		String keyVal2 = "mg15";
		keyToGet.put(key, AttributeValue.builder().s(keyVal2).build());
		DeleteItemRequest deleteReq2 = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();
		if (keyVal2 == "mg15") {
			logger.info(getNowTime() + keyVal2 + " 데이터가 삭제되었습니다!");
		}else if (keyVal2 == null) {
			logger.info(getNowTime() + keyVal2 + " 데이터가 없습니다.");
		}else {
			logger.info("로그 출력형식에 문제가있습니다.");
		}
		
		try {
			ddb.deleteItem(deleteReq);
			ddb.deleteItem(deleteReq2);
		} catch (DynamoDbException e) {

		}
	}

	// dynamoDB sdk_v2 1건 업데이트
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
	
	
	private static String getNowTime() { // 등록 및 갱신 현재시간 메소드
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		String nowDate = formatter.format(zdtjst);

		return nowDate;

	}// end getNowTime()



}