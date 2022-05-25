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

import com.amazonaws.lambda.demo.table.ClientInfo;
import com.amazonaws.lambda.demo.table.MgClientTable;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;

//import study.lambda.constants.Constants;
//import study.lambda.model.MgMember;
import com.amazonaws.lambda.demo.utils.DynamoDBUtils;


public class MgClientService {

	private DynamoDBMapper dynamoDBMapper;

	public MgClientService(AmazonDynamoDB client) {

		dynamoDBMapper = new DynamoDBMapper(client);
		
	}
	
	public void createTable(MgClientTable client) {

//		//client -- 일반 등록.	
//		MgClientTable item = new MgClientTable();
//		ClientInfo infoItem = new ClientInfo();
//		
//        item.setId("1");
//        infoItem.setAddress("渋谷1-2-3");
//        infoItem.setEmail_address("taro@gmail.com");
//        infoItem.setFullName("山田　太郎");
//        item.setClientInfo(infoItem);
//        item.setCreated_at(getNowTime());
//        item.setUpdated_at(getNowTime());
//        item.setInsert_user("insert-admin");
//        item.setUpdated_user("insert-admin");
//        item.setVersion(0);
//        
//		// mapper.save
//        dynamoDBMapper.save(item); //일반등록시.	
        
		//==lambda test json으로 등록시==
		// dynamoDB Table 매핑
		DynamoDBUtils.insertCommonItem(client);
		// 갱신시에는 아래와같이 셋팅하여 save()
//		DynamoDBUtils.updateCommonItem(updateMember);
		// 1건 등록
//		dynamoDBMapper.save(insertMember);
		dynamoDBMapper.save(client); //Map에 event저장하여 등록시.
		
        
	}
	
	private String getNowTime() { //등록 및 갱신 현재시간 메소드
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;
	}

}