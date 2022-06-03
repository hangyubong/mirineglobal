package com.amazonaws.lambda.demo.service;
/*
 * --Code Samle guilde =
 * https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/code-catalog-javav2-example_code-dynamodb.html
 */

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
import org.apache.logging.log4j.util.Strings;

import com.amazonaws.lambda.demo.table.ClientInfo;
import com.amazonaws.lambda.demo.table.ClientInfo2;
import com.amazonaws.lambda.demo.table.MgClient;
import com.amazonaws.lambda.demo.table.MgClientTableV2;
import com.amazonaws.lambda.demo.table.MgMember;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;
import com.amazonaws.lambda.demo.utils.TableMappingUtil;
import com.amazonaws.lambda.demo.table.MgClientTableV2;

import constants.Constants;
import constants.ConstantsClient;
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
import software.amazon.awssdk.services.dynamodb.model.DeleteRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

public class MgClientService2 {

	static final Logger logger = LogManager.getLogger(MgClientService2.class);
	private DynamoDbEnhancedClient enhancedClient;
//	private DynamoDbClient ddb;

//	private final TableSchema<MgClientTableV2> TABLE_SCHEMA = TableSchema.builder(MgClientTableV2.class)
//			.newItemSupplier(MgClientTableV2::new)
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.ID).getter(MgClientTableV2::getId).setter(MgClientTableV2::setId)
//							.tags(StaticAttributeTags.primaryPartitionKey()))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.CLIENT_INFO).getter(MgClientTableV2::getClientInfo)
//							.setter(MgClientTableV2::setClientInfo))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.ADDRESS).getter(MgClientTableV2::getAddress)
//							.setter(MgClientTableV2::setEmail_address))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.EMAIL_ADDRESS).getter(MgClientTableV2::getEmail_address)
//							.setter(MgClientTableV2::setEmail_address))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.FULL_NAME).getter(MgClientTableV2::getFullName)
//							.setter(MgClientTableV2::setFullName))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.CREATED_AT).getter(MgClientTableV2::getCreated_at)
//							.setter(MgClientTableV2::setCreated_at))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.UPDATED_AT).getter(MgClientTableV2::getUpdated_at)
//							.setter(MgClientTableV2::setUpdated_at))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.INSERT_USER).getter(MgClientTableV2::getInsert_user)
//							.setter(MgClientTableV2::setInsert_user))
//			.addAttribute(String.class,
//					a -> a.name(ConstantsClient.UPDATED_USER).getter(MgClientTableV2::getUpdated_user)
//							.setter(MgClientTableV2::setUpdated_user))
//			.addAttribute(int.class, a -> a.name(Constants.VERSION).getter(MgClientTableV2::getVersion)
//					.setter(MgClientTableV2::setVersion))
//			.build();
	
	
//	public MgClientService2() { //핸들러에서 사용하면 여기서 사용하지않아도됨
//		ddb = DynamoDbClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_1).build();
//		enhancedClient = software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.builder().dynamoDbClient(ddb)
//				.build();
//	}
	
	public void putClient() {
		try {
			//테이블 클라이언트에 연동
			DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class)); //테이블스키마 별도 선언없이 쓸경우.
			
			//테이블 데이터를 등록할수있도록 객체생성
			MgClientTableV2 mgClient = new MgClientTableV2();
			
			//테이블 오브젝트 데이터를 가져오는 객체 생성
			/*아래 주석막은부분은 v1에서는 등록이 잘되나 v2에서 이방법으로는 object에 잘들어가지않음
			 * \> v2에서는 Map에 오브젝트아이템들을 넣어주면 등록이 잘됨..*/
//			ClientInfo2 clientInfo = new ClientInfo2();
//			clientInfo.setAddress("Tokyou, komagome");
//			clientInfo.setEmail_address("mg@sample.com");
//			clientInfo.setFullName("Client");
			
			//테이블 오브젝트 데이터를 가져오는 객체 생성 (Map사용)
			Map<String, String> clientInfo = new HashMap<>();
			clientInfo.put("address", "Tokyou, komagome");
			clientInfo.put("email_address", "mg@sample.com");
			clientInfo.put("full_name", "Client");
			
			//등록할데이터 set
			mgClient.setId("client1");
			mgClient.setClient_info(clientInfo);
			mgClient.setCreated_at(getNowTime());
			mgClient.setUpdated_at(getNowTime());
			mgClient.setInsert_user("insert-Client");
			mgClient.setUpdated_user("update-Client");
			mgClient.setVersion(0);
			logger.info("mgClient.toString() : " + mgClient.toString());
			//등록할 데이터를 테이블에 담아서 put해줌
			mgClientTable.putItem(mgClient);
		} catch (DynamoDbException e) {
			logger.info(e.getMessage());
		}
	}


	// dynamoDB sdk_v2 복수 등록 (1)
	public void putClients(DynamoDbEnhancedClient enhancedClient) {// 복수등록.
		/*
		 * --Code Samle guilde =
		 * https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb
		 */
		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
//		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TABLE_SCHEMA); //스키마테이블 정의시 이쪽 사용.
		
		//복수등록따른 리스트 생성
		List<MgClientTableV2> mgClientTableList = new ArrayList<MgClientTableV2>();
		//테이블 객체 생성
		MgClientTableV2 mg15 = new MgClientTableV2();

		//오브젝트 데이터를 map에 넣어줌
		Map<String, String> clientInfo = new HashMap<>();
		clientInfo.put("address", "Tokyou, komagome");
		clientInfo.put("email_address", "mg@sample.com");
		clientInfo.put("full_name", "Client");
		
		//등록할데이터 set
		mg15.setId("client2");
		mg15.setClient_info(clientInfo);
		mg15.setCreated_at(getNowTime());
		mg15.setUpdated_at(getNowTime());
		mg15.setInsert_user("insert-Client");
		mg15.setUpdated_user("update-Client");
		mg15.setVersion(0);
		mgClientTableList.add(mg15);

		MgClientTableV2 mg16 = new MgClientTableV2();
		mg16.setId("client3");
		mg16.setClient_info(clientInfo);
		mg16.setCreated_at(getNowTime());
		mg16.setUpdated_at(getNowTime());
		mg16.setInsert_user("insert-Client");
		mg16.setUpdated_user("update-Client");
		mg16.setVersion(0);
		mgClientTableList.add(mg16);

		BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
				.writeBatches(WriteBatch.builder(MgClientTableV2.class).mappedTableResource(mgClientTable)
						.addPutItem(r -> r.item(mg15))
						.addPutItem(r -> r.item(mg16)).build())
				.build();
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));
		logger.info(mgClientTableList.get(0).id + " 등록!!" + mgClientTableList.get(0).toString());
		logger.info(mgClientTableList.get(1).id + " 등록!!" + mgClientTableList.get(1).toString());


	}
	
	//dynamoDB sdk_v2 복수 등록. -- for문.
	public void createClients(DynamoDbEnhancedClient enhancedClient, Object input) { //dynamoDB sdk_v2 복수 등록. -- for문.
		try {

			// input 데이터를 모델에 매핑
//			Map<String, Object> m = (Map<String, Object>) input;
			Map<String, String> m = (Map<String, String>) input;
			
			
			logger.info("m.get(Constants.ID)? " + m.get(Constants.ID));
			logger.info("m.get(\"client_info\")? " + m.get("client_info"));
			logger.info(m.toString());
			
			// DynamoDb table 생성
			// TODO 아래와 같이 table을 생성하면 Table모델의 변수명이 그대로 매핑되어 등록된다.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
//					TableSchema.fromBean(MgMemberTableV2.class));
			// TODO 따라서, 아래와같이 TABLE_SCHEMA를 생성해서 테이블 AttributeName, AttributeValue를 지정해 줄 필요가 있다.
			 
			DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
				
				Map<String,String> mgClient = new HashMap<>();
				mgClient.put(m.get(ConstantsClient.ADDRESS), "komagome");
				mgClient.put(m.get(ConstantsClient.EMAIL_ADDRESS), "mg@sample.com");
				mgClient.put(m.get(ConstantsClient.FULL_NAME), "global");
				
			List<MgClientTableV2> insertClients = new ArrayList<>();
			
			int num = 101;
			for (int i = 0; i < 5; i++) {
				// 1건씩 매핑하여 MgMemberTable을 생성해준다음
				String id = m.get(Constants.ID).substring(0, m.get(Constants.ID).length() - 1) + String.valueOf(num);
				MgClientTableV2 insertMember = TableMappingUtil.clientMapToMgClientTable(id , mgClient);
				DynamoDBUtils.insertCommonItemV2(insertMember);
				// 등록 실행전 데이터 확인
				logger.info("등록할 데이터 정보  mgMember.toString() : " + insertMember.toString());
				// 리스트에 add하여 생성
				insertClients.add(insertMember);
				num++;
			}
			
			BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
					.writeBatches(WriteBatch.builder(MgClientTableV2.class).mappedTableResource(mgClientTable)
							.addPutItem(r -> r.item(insertClients.get(0))).addPutItem(r -> r.item(insertClients.get(1)))
							.addPutItem(r -> r.item(insertClients.get(2))).addPutItem(r -> r.item(insertClients.get(3)))
							.addPutItem(r -> r.item(insertClients.get(4))).build())
					.build();

			// Add these two items to the table.
			enhancedClient.batchWriteItem(batchWriteItemEnhancedRequest);
			logger.info("batchWriteItem() end");
			
			/**
			 * 복수건 확인
			 */
			// 등록된 데이터 확인

			// close()
//			ddb.close(); //handler에 있는걸로 사용하고있는 경우 여기에서 닫을게없음
		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
		}

	}

	/* 1건 삭제 --***정렬키가 있는 테이블의경우 아래와같이 sortkey값과 벨류까지 제공해야지만 삭제가 가능.
		\>기본키값만 제공하거나 sortkey값에 벨류값이 일치하지않을시 The provided key element does not match the schema
	    \>(제공된 키 요소가 스키마와 일치하지 않습니다.)에러 발생에 주의.
		\>기본키만 있는 테이블의경우 파티션키와 벨류값만 명시하면 쉽게 삭제처리 됨. */
	public void deleteClient(DynamoDbClient ddb ) {
		String tableName = "MG_CLIENT";
		String key = "id";
		String keyVal = "cl101";
		String sortkey = "created_at";
		String sortkeyVal = "2022-06-03T00:50:31Z";

		HashMap<String, AttributeValue> keyToGet = new HashMap<>();
		keyToGet.put(key, AttributeValue.builder().s(keyVal).build());
		keyToGet.put(sortkey, AttributeValue.builder().s(sortkeyVal).build());

		DeleteItemRequest deleteReq = DeleteItemRequest.builder().tableName(tableName).key(keyToGet).build();

		try {
			ddb.deleteItem(deleteReq);
		} catch (DynamoDbException e) {

		}
	}

	// 1건 조회(?)
	public void getClient(DynamoDbClient ddb ) {
		String tableName = "MG_CLIENT";
		String key = "id";
		String keyVal = "client2";
		String sortkey = "created_at";
		String sortkeyVal = "2022/06/02 05:29:34";
        HashMap<String,AttributeValue> keyToGet = new HashMap<String,AttributeValue>();

        keyToGet.put(key, AttributeValue.builder().s(keyVal).build());
        keyToGet.put(sortkey, AttributeValue.builder().s(sortkeyVal).build());

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
	
	// 복수 삭제
	/*주의사항 : MG_CLIENT 테이블은 SortKey가 있으므로 항시 sortkey가 있는 테이블은 sortkey도 제공해야 처리가되므로
	 * \>sortkey의 벨류값을 정확히 일치시켜 넣어줘야 정삭삭제 처리된다.*/
	public final void deleteBatchClients(DynamoDbEnhancedClient enhancedClient) {

		
		//테이블 enhancedClient연동
		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
//		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TABLE_SCHEMA);
		
		//복수등록따른 리스트 생성
		List<MgClientTableV2> mgClientTableList = new ArrayList<MgClientTableV2>();
		
		//삭제1
		MgClientTableV2 mgClientTable1 = new MgClientTableV2();	//테이블 객체 생성
		mgClientTable1.setId("client1"); // set 파티션키
		mgClientTable1.setCreated_at("2022/06/02 04:39:24"); //set 정렬키
		mgClientTableList.add(mgClientTable1); //MgClientTableV2 테이블 리스트에 set한 정보를 넣어줌
		
		//삭제2
		MgClientTableV2 mgClientTable2 = new MgClientTableV2();//테이블 객체 생성
		mgClientTable2.setId("client1");//set 파티션키
		mgClientTable2.setCreated_at("2022/06/02 05:04:14");//set 정렬키
		mgClientTableList.add(mgClientTable2); //MgClientTableV2 테이블 리스트에 set한 정보를 넣어줌
		
		//BatchWriteItemEnhancedRequest를 이용한 복수 삭제 처리.
		BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
				.writeBatches(WriteBatch.builder(MgClientTableV2.class).mappedTableResource(mgClientTable)
						.addDeleteItem(mgClientTable1)
						.addDeleteItem(mgClientTable2)
						.build())
				.build();
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));
		logger.info(mgClientTableList.get(0).id + " 삭제!!");
		logger.info(mgClientTableList.get(1).id + " 삭제!!");
		
	}	
	
	// dynamoDB sdk_v2 1건 업데이트
	/*sortkey가 있는테이블은 기본키와같이 sortkey도 제공해야 처리가능*/
	public void updateTableClientV(DynamoDbClient ddb) {
		String tableName = "MG_CLIENT";
		String key = "id";
		String keyVal = "mg";
		String sortkey = "created_at";
		String sortkeyVal = "2022/05/22 15:21:38";
		String name = "updated_user";
		String updateVal = "updated-admin";

		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(key, AttributeValue.builder().s(keyVal).build()); //파티션키 체공
		itemKey.put(sortkey, AttributeValue.builder().s(sortkeyVal).build()); //정렬키 체공
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
	
	//scan으로 전체 취득
    public void scanClient(DynamoDbEnhancedClient enhancedClient) { //방법2.

        try{
            DynamoDbTable<MgClientTableV2> clientTableV2 = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
            Iterator<MgClientTableV2> results = clientTableV2.scan().items().iterator();
            while (results.hasNext()) {

            	MgClientTableV2 rem = results.next();
                logger.info("getId? "+rem.getId());
                logger.info("getMgName? " +rem.getClient_info());
            }

        } catch (DynamoDbException e) {
        	logger.info(e.getMessage());
        }
        System.out.println("Done");
    }
    
    //query 기본키로 데이터 취득
    public String queryCliient(DynamoDbEnhancedClient enhancedClient) {

        try{
            DynamoDbTable<MgClientTableV2> mappedTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
            QueryConditional queryConditional = QueryConditional
                    .keyEqualTo(Key.builder()
                            .partitionValue("mg")
                            .build());

            // Get items in the table and write out the ID value.
            Iterator<MgClientTableV2> results = mappedTable.query(queryConditional).items().iterator();
            String result="";

            while (results.hasNext()) {
            	MgClientTableV2 rem = results.next();
                result = rem.getId();
                logger.info("getMgName ? " + rem.getId());
                logger.info("getBirth_date ? " + rem.getClient_info());
            }
            return result;

        } catch (DynamoDbException e) {
        	logger.info(e.getMessage());
        	logger.traceExit();
        }
        return "";
    }   
	
	
	private String getNowTime() { //등록 및 갱신 현재시간 메소드
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;
	}

}