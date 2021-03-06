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
	
	
//	public MgClientService2() { //??????????????? ???????????? ????????? ????????????????????????
//		ddb = DynamoDbClient.builder().region(software.amazon.awssdk.regions.Region.AP_NORTHEAST_1).build();
//		enhancedClient = software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient.builder().dynamoDbClient(ddb)
//				.build();
//	}
	
	public void putClient() {
		try {
			//????????? ?????????????????? ??????
			DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class)); //?????????????????? ?????? ???????????? ?????????.
			
			//????????? ???????????? ????????????????????? ????????????
			MgClientTableV2 mgClient = new MgClientTableV2();
			
			//????????? ???????????? ???????????? ???????????? ?????? ??????
			/*?????? ????????????????????? v1????????? ????????? ????????? v2?????? ?????????????????? object??? ?????????????????????
			 * \> v2????????? Map??? ??????????????????????????? ???????????? ????????? ??????..*/
//			ClientInfo2 clientInfo = new ClientInfo2();
//			clientInfo.setAddress("Tokyou, komagome");
//			clientInfo.setEmail_address("mg@sample.com");
//			clientInfo.setFullName("Client");
			
			//????????? ???????????? ???????????? ???????????? ?????? ?????? (Map??????)
			Map<String, String> clientInfo = new HashMap<>();
			clientInfo.put("address", "Tokyou, komagome");
			clientInfo.put("email_address", "mg@sample.com");
			clientInfo.put("full_name", "Client");
			
			//?????????????????? set
			mgClient.setId("client1");
			mgClient.setClient_info(clientInfo);
			mgClient.setCreated_at(getNowTime());
			mgClient.setUpdated_at(getNowTime());
			mgClient.setInsert_user("insert-Client");
			mgClient.setUpdated_user("update-Client");
			mgClient.setVersion(0);
			logger.info("mgClient.toString() : " + mgClient.toString());
			//????????? ???????????? ???????????? ????????? put??????
			mgClientTable.putItem(mgClient);
		} catch (DynamoDbException e) {
			logger.info(e.getMessage());
		}
	}


	// dynamoDB sdk_v2 ?????? ?????? (1)
	public void putClients(DynamoDbEnhancedClient enhancedClient) {// ????????????.
		/*
		 * --Code Samle guilde =
		 * https://docs.aws.amazon.com/ko_kr/code-samples/latest/catalog/javav2-dynamodb
		 */
		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
//		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TABLE_SCHEMA); //?????????????????? ????????? ?????? ??????.
		
		//?????????????????? ????????? ??????
		List<MgClientTableV2> mgClientTableList = new ArrayList<MgClientTableV2>();
		//????????? ?????? ??????
		MgClientTableV2 mg15 = new MgClientTableV2();

		//???????????? ???????????? map??? ?????????
		Map<String, String> clientInfo = new HashMap<>();
		clientInfo.put("address", "Tokyou, komagome");
		clientInfo.put("email_address", "mg@sample.com");
		clientInfo.put("full_name", "Client");
		
		//?????????????????? set
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
		logger.info(mgClientTableList.get(0).id + " ??????!!" + mgClientTableList.get(0).toString());
		logger.info(mgClientTableList.get(1).id + " ??????!!" + mgClientTableList.get(1).toString());


	}
	
	//dynamoDB sdk_v2 ?????? ??????. -- for???.
	public void createClients(DynamoDbEnhancedClient enhancedClient, Object input) { //dynamoDB sdk_v2 ?????? ??????. -- for???.
		try {

			// input ???????????? ????????? ??????
//			Map<String, Object> m = (Map<String, Object>) input;
			Map<String, String> m = (Map<String, String>) input;
			
			
			logger.info("m.get(Constants.ID)? " + m.get(Constants.ID));
			logger.info("m.get(\"client_info\")? " + m.get("client_info"));
			logger.info(m.toString());
			
			// DynamoDb table ??????
			// TODO ????????? ?????? table??? ???????????? Table????????? ???????????? ????????? ???????????? ????????????.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
//					TableSchema.fromBean(MgMemberTableV2.class));
			// TODO ?????????, ??????????????? TABLE_SCHEMA??? ???????????? ????????? AttributeName, AttributeValue??? ????????? ??? ????????? ??????.
			 
			DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
				
				Map<String,String> mgClient = new HashMap<>();
				mgClient.put(m.get(ConstantsClient.ADDRESS), "komagome");
				mgClient.put(m.get(ConstantsClient.EMAIL_ADDRESS), "mg@sample.com");
				mgClient.put(m.get(ConstantsClient.FULL_NAME), "global");
				
			List<MgClientTableV2> insertClients = new ArrayList<>();
			
			int num = 101;
			for (int i = 0; i < 5; i++) {
				// 1?????? ???????????? MgMemberTable??? ??????????????????
				String id = m.get(Constants.ID).substring(0, m.get(Constants.ID).length() - 1) + String.valueOf(num);
				MgClientTableV2 insertMember = TableMappingUtil.clientMapToMgClientTable(id , mgClient);
				DynamoDBUtils.insertCommonItemV2(insertMember);
				// ?????? ????????? ????????? ??????
				logger.info("????????? ????????? ??????  mgMember.toString() : " + insertMember.toString());
				// ???????????? add?????? ??????
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
			 * ????????? ??????
			 */
			// ????????? ????????? ??????

			// close()
//			ddb.close(); //handler??? ???????????? ?????????????????? ?????? ???????????? ???????????????
		} catch (DynamoDbException e) {
			logger.error(e.getMessage());
		}

	}

	/* 1??? ?????? --***???????????? ?????? ?????????????????? ??????????????? sortkey?????? ???????????? ?????????????????? ????????? ??????.
		\>??????????????? ??????????????? sortkey?????? ???????????? ????????????????????? The provided key element does not match the schema
	    \>(????????? ??? ????????? ???????????? ???????????? ????????????.)?????? ????????? ??????.
		\>???????????? ?????? ?????????????????? ??????????????? ???????????? ???????????? ?????? ???????????? ???. */
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

	// 1??? ??????(?)
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
	
	// ?????? ??????
	/*???????????? : MG_CLIENT ???????????? SortKey??? ???????????? ?????? sortkey??? ?????? ???????????? sortkey??? ???????????? ??????????????????
	 * \>sortkey??? ???????????? ????????? ???????????? ???????????? ???????????? ????????????.*/
	public final void deleteBatchClients(DynamoDbEnhancedClient enhancedClient) {

		
		//????????? enhancedClient??????
		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TableSchema.fromBean(MgClientTableV2.class));
//		DynamoDbTable<MgClientTableV2> mgClientTable = enhancedClient.table("MG_CLIENT", TABLE_SCHEMA);
		
		//?????????????????? ????????? ??????
		List<MgClientTableV2> mgClientTableList = new ArrayList<MgClientTableV2>();
		
		//??????1
		MgClientTableV2 mgClientTable1 = new MgClientTableV2();	//????????? ?????? ??????
		mgClientTable1.setId("client1"); // set ????????????
		mgClientTable1.setCreated_at("2022/06/02 04:39:24"); //set ?????????
		mgClientTableList.add(mgClientTable1); //MgClientTableV2 ????????? ???????????? set??? ????????? ?????????
		
		//??????2
		MgClientTableV2 mgClientTable2 = new MgClientTableV2();//????????? ?????? ??????
		mgClientTable2.setId("client1");//set ????????????
		mgClientTable2.setCreated_at("2022/06/02 05:04:14");//set ?????????
		mgClientTableList.add(mgClientTable2); //MgClientTableV2 ????????? ???????????? set??? ????????? ?????????
		
		//BatchWriteItemEnhancedRequest??? ????????? ?????? ?????? ??????.
		BatchWriteItemEnhancedRequest batchWriteItemEnhancedRequest = BatchWriteItemEnhancedRequest.builder()
				.writeBatches(WriteBatch.builder(MgClientTableV2.class).mappedTableResource(mgClientTable)
						.addDeleteItem(mgClientTable1)
						.addDeleteItem(mgClientTable2)
						.build())
				.build();
		enhancedClient.batchWriteItem((batchWriteItemEnhancedRequest));
		logger.info(mgClientTableList.get(0).id + " ??????!!");
		logger.info(mgClientTableList.get(1).id + " ??????!!");
		
	}	
	
	// dynamoDB sdk_v2 1??? ????????????
	/*sortkey??? ?????????????????? ?????????????????? sortkey??? ???????????? ????????????*/
	public void updateTableClientV(DynamoDbClient ddb) {
		String tableName = "MG_CLIENT";
		String key = "id";
		String keyVal = "mg";
		String sortkey = "created_at";
		String sortkeyVal = "2022/05/22 15:21:38";
		String name = "updated_user";
		String updateVal = "updated-admin";

		HashMap<String, AttributeValue> itemKey = new HashMap<>();
		itemKey.put(key, AttributeValue.builder().s(keyVal).build()); //???????????? ??????
		itemKey.put(sortkey, AttributeValue.builder().s(sortkeyVal).build()); //????????? ??????
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
	
	//scan?????? ?????? ??????
    public void scanClient(DynamoDbEnhancedClient enhancedClient) { //??????2.

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
    
    //query ???????????? ????????? ??????
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
	
	
	private String getNowTime() { //?????? ??? ?????? ???????????? ?????????
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime ?????? ?????????????????? JST ????????????
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;
	}

}