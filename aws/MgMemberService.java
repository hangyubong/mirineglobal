package com.amazonaws.lambda.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.lambda.demo.guide.DynamoDBMapperQueryScanExample;
import com.amazonaws.lambda.demo.handler.MgMemberHandler;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.api.BatchGetItemApi;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.xspec.DeleteItemExpressionSpec;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;

 

public class MgMemberService {
	static final Logger logger = LogManager.getLogger(MgMemberService.class);
	private DynamoDBMapper dynamoDBMapper;	
    public static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    public static DynamoDB dynamoDB = new DynamoDB(client);

    static String tableName = "MG_MEMBER";

	public MgMemberService(AmazonDynamoDB client) {

		dynamoDBMapper = new DynamoDBMapper(client);

	}

	public void insertMember() {

//		String my = "hangyubong";
//		
////		//현재시간설정(일본시간.) -- ***Calendar API는 사용시 get타입불투명, 모호한상수, 월계산형식, 불변이아니므로 캘린도 공유시 변경값 같이 적용 등등 결함이 많아 잘사용하지않음

        //포맷설정
		String birthDay = "1992/09/06";

		//member -- 일반 등록시.	
		MgMemberTable item = new MgMemberTable();
        item.setId("mg13");
        item.setMgName("Jeny");     
        item.setBirth_date(birthDay);
        item.setEmail_address("Jeny@gmail.com");
        item.setCreated_at(getNowTime());
        item.setUpdated_at(getNowTime());
        item.setInsert_user("hangyubong");
        item.setUpdated_user("hangyubong");
        item.setVersion(0);
		// mapper.save
        dynamoDBMapper.save(item); //일반등록시.		           

        
		//Batch Put Item==============================
//  	  	 MgMemberTable item1 = new MgMemberTable();
//		  	  item1.setId("mg20");
//	    	  item1.setMgName("semi");     
//	    	  item1.setBirth_date(birthDay);
//	    	  item1.setEmail_address("semi@gmail.com");
//	    	  item1.setCreated_at(getNowTime());
//	    	  item1.setUpdated_at(getNowTime());
//	    	  item1.setInsert_user("insert-admin");
//	    	  item1.setUpdated_user("insert-admin");
//	    	  item1.setVersion(0);
//
//    	  MgMemberTable item2 = new MgMemberTable();
//	    	  item2.setId("mg21");
//	    	  item2.setMgName("semi");     
//	    	  item2.setBirth_date(birthDay);
//	    	  item2.setEmail_address("semi@gmail.com");
//	    	  item2.setCreated_at(getNowTime());
//	    	  item2.setUpdated_at(getNowTime());
//	    	  item2.setInsert_user("insert-admin");
//	    	  item2.setUpdated_user("insert-admin");
//	    	  item2.setVersion(0);
//
//    	  MgMemberTable item3 = new MgMemberTable();
//	    	  item3.setId("mg22");
//	    	  item3.setMgName("semi");     
//	    	  item3.setBirth_date(birthDay);
//	    	  item3.setEmail_address("semi@gmail.com");
//	    	  item3.setCreated_at(getNowTime());
//	    	  item3.setUpdated_at(getNowTime());
//	    	  item3.setInsert_user("insert-admin");
//	    	  item3.setUpdated_user("insert-admin");
//	    	  item3.setVersion(0);
//    	  dynamoDBMapper.batchSave(Arrays.asList(item1, item2, item3));
    	//END Batch Put Item==============================

        //member -- mapper로 복수등록시.(리스트사용)
//		MgMemberTable item = new MgMemberTable();
//		List<MgMemberTable> itemList = new ArrayList<MgMemberTable>();
//
//		itemList.add(item.setId("mg20"),
//		        item.setMgName("semi"),     
//		        item.setBirth_date(birthDay),
//		        item.setEmail_address("semi@gmail.com"),
//		        item.setCreated_at(getNowTime()),
//		        item.setUpdated_at(getNowTime()),
//		        item.setInsert_user("insert-admin"),
//		        item.setUpdated_user("insert-admin"),
//		        item.setVersion(0));
		
//        itemList.add(new MgMemberTable());
//        itemList.add("mg20", "haru", "1991/04/13", "haru@gmail.com", getNowTime(), getNowTime(), "insert-admin", "insert-admin", 0);
		
		// mapper.save                     
//        dynamoDBMapper.save(itemList);  //리스트로 복수등록
        
		
//		//==lambda test json으로 등록시==
//		// dynamoDB Table 매핑
//		DynamoDBUtils.insertCommonItem(member);
//		// 갱신시에는 아래와같이 셋팅하여 save()
////		DynamoDBUtils.updateCommonItem(updateMember);
//		// 1건 등록
////		dynamoDBMapper.save(insertMember);
//		dynamoDBMapper.save(member); //Map에 event저장하여 등록시.	

	}//END insertMember()	

    public void createItems() { //v1 - 1건 등록
        Table table = dynamoDB.getTable(tableName);
        
//        LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Tokyo"));
        ZonedDateTime zdt = ZonedDateTime.now(ZoneId.of("UTC")); //LocalDateTime보다는 ZoneID를 쓰기때문에 ZonedDateTime쓰도록함.
        DateTimeFormatter fomatter = DateTimeFormatter.ofPattern("yyyy/mm/dd HH:mm:ss");
//        String nowDate = fomatter.format(ldt);
        String nowDate2 = fomatter.format(zdt);
        
        try {

            Item item = new Item().withPrimaryKey("id", "mg13").withString("mg_name", "Jeny")
                .withString("birth_date", "1992/09/06")
                .withString("email_address", "Jeny@gmail.com")
                .withString("created_at", nowDate2)
                .withString("updated_at", nowDate2)
                .withString("insert_user", "insert-admin")
                .withString("updated_user", "insert-admin")
                .withNumber("version", 13);
            table.putItem(item);

            item = new Item().withPrimaryKey("id", "mg14").withString("mg_name", "clock")
                    .withString("birth_date", "1987/12/26")
                    .withString("email_address", "clock@gmail.com")
                    .withString("created_at", nowDate2)
                    .withString("updated_at", nowDate2)
                    .withString("insert_user", "insert-admin")
                    .withString("updated_user", "insert-admin")
                    .withNumber("version", 14);
                table.putItem(item);            

        }
        catch (Exception e) {
            System.err.println("Create items failed.");
            System.err.println(e.getMessage());
            
        }
    }
    
    
    public void testCRUDOperations() {//v1 - 1건 삭제 됨.
    	
    	MgMemberTable item = new MgMemberTable();
        item.setId("mg15");

        // Save the item (MgMember).
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(item);

        // Retrieve the updated item.
        DynamoDBMapperConfig config = DynamoDBMapperConfig.builder()
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
        .build();
        MgMemberTable updatedItem = mapper.load(MgMemberTable.class, "mg15", config);
        
        // Delete the item.
        mapper.delete(updatedItem);

        // Try to retrieve deleted item.
        MgMemberTable deletedItem = mapper.load(MgMemberTable.class, updatedItem.getId(), config);
        if (deletedItem == null) {
            logger.info("success! delete!!");
        }
    }
    
    public void batchSave() { //batchDelete 복수삭제 --수정 보완필요.
    	
        MgMemberTable item1 = new MgMemberTable();   
        item1.setId("mg14");
        item1.setMgName("clock");     
        item1.setBirth_date("1987/12/26");
        item1.setEmail_address("clock@gmail.com");
        item1.setCreated_at(getNowTime());
        item1.setUpdated_at(getNowTime());
        item1.setInsert_user("hangyubong");
        item1.setUpdated_user("hangyubong");
        item1.setVersion(0);
        
        MgMemberTable item2 = new MgMemberTable(); 
        item2.setId("mg13");
        item2.setMgName("Jeny");     
        item2.setBirth_date("1992/09/06");
        item2.setEmail_address("Jeny@gmail.com");
        item2.setCreated_at(getNowTime());
        item2.setUpdated_at(getNowTime());
        item2.setInsert_user("Jeny");
        item2.setUpdated_user("hangyubong");
        item2.setVersion(0);
       
	    dynamoDBMapper.batchSave(Arrays.asList(item1, item2));
    }
    
	public void batchDelete() { // batchDelete 복수삭제 --수정 보완필요.
		List<MgMemberTable> members = new ArrayList<MgMemberTable>();

		int num = 0;
		for (int i = 0; i < 3; i++) {
			String id = "mg0" + String.valueOf(num);
			MgMemberTable mg = getMembers(id);
			members.add(mg);
			num++;
		}
		dynamoDBMapper.batchDelete(members);
	}
	
	private MgMemberTable getMembers(String id) {
		MgMemberTable mg = new MgMemberTable();
		mg.setId(id);
		return mg;	
	}

    public void getMember() {
    	try {
		
//    		DynamoDBMapper mapper = new DynamoDBMapper(client);

    		
    		//scan으로 취득
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("19"));
//    		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression().withFilterExpression("begins_with(birth_date, :v1)")
//    				.withExpressionAttributeValues(values);
//    		List<MgMemberTable> result = dynamoDBMapper.scan(MgMemberTable.class, scanExpression);
//    		
//    		logger.info("birth? " + result.get(0).getBirth_date());
//    		logger.info("result size ? " + result.size());
    		
    		//query로 취득--query취득은 partition key먼저 입력후 찾아야함
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("mg17")); //파티션 키의 키값
//    		values.put(":v2", new AttributeValue().withS("mi"));  //위의 파티션키값에 속한 속성내에서 취득
//    		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
//    				.withKeyConditionExpression("id = :v1") //파티션 키 = 별도로 담기위해 :v1 으로 뺌
//					.withFilterExpression("begins_with(mg_name, :v2)")//어디서부터 찾을지. 
//    				.withExpressionAttributeValues(values);//별도로 담은 변수 값을 넣어 찾음
//    		List<MgMemberTable> result = dynamoDBMapper.query(MgMemberTable.class, queryExpression);
//    		
//    		logger.info("mg_name? " + result.get(0).getMgName());//get(0)은 목록의 첫번째 데이터를 가리킴 
//    		logger.info("result size ? " + result.size());//결과가 몇개인지.


    		//Global Secondary Index -scan으로 취득
//    		Map<String, AttributeValue> values = new HashMap<String, AttributeValue>();
//    		values.put(":v1", new AttributeValue().withS("1990"));
//    		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
//    				.withIndexName("MG_MEMBER_GSI_1")
//    				.withFilterExpression("birth_date >= :v1")
//    				.withExpressionAttributeValues(values);
//    		List<MgMemberTable> result = dynamoDBMapper.scan(MgMemberTable.class, scanExpression);
//    		
////    		logger.info("birth_date? " + result.get(0).getBirth_date());
//    		logger.info("birth_date? " + result.get(12).getBirth_date());
//    		logger.info("result size ? " + result.size());   		

    		//Global Secondary Index - query로 취득--query취득은 partition key로 해당정보내에서만 취득가능
    		HashMap<String, AttributeValue> values = new HashMap<String, AttributeValue>();
    		values.put(":v1",  new AttributeValue().withS("miura"));
    		values.put(":v2",  new AttributeValue().withS("19"));

    		DynamoDBQueryExpression queryExpression = new DynamoDBQueryExpression()
    		    .withIndexName("MG_MEMBER_GSI_1")
    		    .withConsistentRead(false)
    		    .withKeyConditionExpression("mg_name = :v1 and begins_with(birth_date, :v2)")
    		    .withExpressionAttributeValues(values);

    		List<MgMemberTable> result =  dynamoDBMapper.query(MgMemberTable.class, queryExpression);
    		logger.info("mg_name? " + result.get(0).getMgName());//get(0)은 목록의 첫번째 데이터를 가리킴 
    		logger.info("birth_date? " + result.get(0).getBirth_date());//get(0)은 목록의 첫번째 데이터를 가리킴 
    		logger.info("result size ? " + result.size());//결과가 몇개인지.


        }
        catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperQueryScanExample: " + t);
            t.printStackTrace();
        }
    }
	

	private String getNowTime() { //등록 및 갱신 현재시간 메소드
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;

	}//end getNowTime()
	

}//end class