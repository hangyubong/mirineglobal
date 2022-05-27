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

import com.amazonaws.lambda.demo.handler.MgMemberHandler;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDeleteExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import com.amazonaws.services.dynamodbv2.document.api.BatchGetItemApi;
import com.amazonaws.services.dynamodbv2.model.BatchGetItemResult;
import com.amazonaws.services.dynamodbv2.xspec.DeleteItemExpressionSpec;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;

 

public class MgMemberService {
	static final Logger logger = LogManager.getLogger(MgMemberService.class);
	private DynamoDBMapper dynamoDBMapper;

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

	

	private String getNowTime() { //등록 및 갱신 현재시간 메소드
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;

	}//end getNowTime()
	

}//end class