//package com.amazonaws.lambda.demo.service;
//​
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//​
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//​
//​
//import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
//import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
//import software.amazon.awssdk.enhanced.dynamodb.Expression;
//import software.amazon.awssdk.enhanced.dynamodb.Key;
//import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags;
//import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticTableSchema;
//import software.amazon.awssdk.enhanced.dynamodb.model.BatchWriteItemEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
//import software.amazon.awssdk.enhanced.dynamodb.model.WriteBatch;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
//import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
//import study.lambda.constants.Constants;
//import study.lambda.model.MgMember;
//import study.lambda.table.MgMemberTable;
//import study.lambda.table.MgMemberTableV2;
//import study.lambda.utils.DynamoDBUtils;
//import study.lambda.utils.TableMappingUtil;
//​
//public class DynamoDbStudyServiceV2 {
//​
//	static final Logger logger = LogManager.getLogger(DynamoDbStudyServiceV2.class);
//​
//	private DynamoDbClient dynamoDbClient;
//	private DynamoDbEnhancedClient enhancedClient;
//​
//	public DynamoDbStudyServiceV2() {}
//​
//	private void createCredential() {
//		EnvironmentVariableCredentialsProvider credentialProvider = EnvironmentVariableCredentialsProvider.create();
//		Region region = Region.AP_NORTHEAST_1;
//		dynamoDbClient = DynamoDbClient.builder().region(region).credentialsProvider(credentialProvider).build();
//		enhancedClient = DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
//		
//	}
//	private static final TableSchema<MgMemberTableV2> TABLE_SCHEMA =
//            StaticTableSchema.builder(MgMemberTableV2.class)
//                    .newItemSupplier(MgMemberTableV2::new)
//                    .addAttribute(String.class, a -> a.name(Constants.ID)
//                            .getter(MgMemberTableV2::getId)
//                            .setter(MgMemberTableV2::setId)
//                            .tags(StaticAttributeTags.primaryPartitionKey()))
//                    .addAttribute(String.class, a -> a.name(Constants.NAME)
//                            .getter(MgMemberTableV2::getMgName)
//                            .setter(MgMemberTableV2::setMgName))
//                    .addAttribute(String.class, a -> a.name(Constants.BIRTHDATE)
//                            .getter(MgMemberTableV2::getBirthDate)
//                            .setter(MgMemberTableV2::setBirthDate))
//                    .addAttribute(String.class, a -> a.name(Constants.EMAIL_ADDRESS)
//                            .getter(MgMemberTableV2::getEmailAddress)
//                            .setter(MgMemberTableV2::setEmailAddress))
//                    .addAttribute(String.class, a -> a.name(Constants.CREATED_AT)
//                    		.getter(MgMemberTableV2::getCreatedAt)
//                    		.setter(MgMemberTableV2::setCreatedAt))
//                    .addAttribute(String.class, a -> a.name(Constants.UPDATED_AT)
//                    		.getter(MgMemberTableV2::getUpdatedAt)
//                    		.setter(MgMemberTableV2::setUpdatedAt))
//                    .addAttribute(String.class, a -> a.name(Constants.INSERT_USER)
//                    		.getter(MgMemberTableV2::getInsertUser)
//                    		.setter(MgMemberTableV2::setInsertUser))
//                    .addAttribute(String.class, a -> a.name(Constants.UPDATED_USER)
//                    		.getter(MgMemberTableV2::getUpdatedUser)
//                    		.setter(MgMemberTableV2::setUpdatedUser))
//                    .addAttribute(long.class, a -> a.name(Constants.VERSION)
//                    		.getter(MgMemberTableV2::getVersion)
//                    		.setter(MgMemberTableV2::setVersion))
//                    .build();
//	
//	
//	/**
//	 * 1건 등록
//	 * @param input
//	 */
//	public void createMember(Object input) {
//		try {
//			createCredential();
//			
//			// input 데이터를 모델에 매핑
//			Map<String, String> m = (Map<String, String>) input;
//​
//			// DynamoDb table 생성
//			// TODO 아래와 같이 table을 생성하면 Table모델의 변수명이 그대로 매핑되어 등록된다.
////			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
////					TableSchema.fromBean(MgMemberTableV2.class));
//			// TODO 따라서, 아래와같이 TABLE_SCHEMA를 생성해서 테이블 AttributeName, AttributeValue를 지정해 줄 필요가 있다.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",TABLE_SCHEMA);
//			
//			// 등록할 member 생성
//			MgMemberTableV2 mgMember = new MgMemberTableV2(m.get(Constants.ID), m.get(Constants.NAME),
//					m.get(Constants.BIRTHDATE), m.get(Constants.BIRTHDATE));
//			DynamoDBUtils.insertCommonItem(mgMember);
//			// 등록 실행전 데이터 확인
//			logger.info("등록할 데이터 정보  mgMember.toString() : " + mgMember.toString());
//			// 등록실행
//			mgMemberTable.putItem(mgMember);
//			
//			/**
//			 * 1건 확인
//			 */
//			// TODO 등록된 데이터 확인
//​
//			
//			
//			// close()
//			dynamoDbClient.close();
//		} catch (DynamoDbException e) {
//			logger.error(e.getMessage());
//		}
//​
//	}
//	
//	/**
//	 * 복수건 등록
//	 * @param input
//	 */
//	public void createMembers(Object input) {
//		try {
//			createCredential();
//			
//			// input 데이터를 모델에 매핑
//			Map<String, String> m = (Map<String, String>) input;
//​
//			// DynamoDb table 생성
//			// TODO 아래와 같이 table을 생성하면 Table모델의 변수명이 그대로 매핑되어 등록된다.
////			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",
////					TableSchema.fromBean(MgMemberTableV2.class));
//			// TODO 따라서, 아래와같이 TABLE_SCHEMA를 생성해서 테이블 AttributeName, AttributeValue를 지정해 줄 필요가 있다.
//			DynamoDbTable<MgMemberTableV2> mgMemberTable = enhancedClient.table("MG_MEMBER",TABLE_SCHEMA);
//			
//			List<MgMember> members = new ArrayList<>();
//			int num = 101;
//			for (int i = 0; i < 5; i++) {
//				// 등록할 데이터 생성
//				MgMember member = new MgMember(m.get(Constants.ID), m.get(Constants.NAME), m.get(Constants.BIRTHDATE),
//						m.get(Constants.BIRTHDATE));
//				// string 으로 변환하여 id저장
//				String id = m.get(Constants.ID).substring(0, m.get(Constants.ID).length()-1) + String.valueOf(num);
//				// 복수데이터 생성하기 위해 id만 바꿔서 리스트 생성
//				member.setId(id);
//				members.add(member);
//				// id += 1
//				num++;
//			}
//			// dynamoDB Table 매핑
//			List<MgMemberTableV2> insertMembers = new ArrayList<>();
//			for(MgMember mem : members) {
//				// 1건씩 매핑하여 MgMemberTable을 생성해준다음
//				MgMemberTableV2 insertMember = TableMappingUtil.memberToMgMemberTableV2(mem);
//				DynamoDBUtils.insertCommonItem(insertMember);
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
//			
//​
//			
//			// close()
//			dynamoDbClient.close();
//		} catch (DynamoDbException e) {
//			logger.error(e.getMessage());
//		}
//​
//	}
//}