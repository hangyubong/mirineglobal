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

import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DeleteItemOutcome;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;

public class MgMemberService {

	private DynamoDBMapper dynamoDBMapper;

	public MgMemberService(AmazonDynamoDB client) {

		dynamoDBMapper = new DynamoDBMapper(client);
		
	}
	
	public void insertMember(MgMemberTable member) {

//		String my = "hangyubong";
//		
////		//현재시간설정(일본시간.) -- ***Calendar API는 사용시 get타입불투명, 모호한상수, 월계산형식, 불변이아니므로 캘린도 공유시 변경값 같이 적용 등등 결함이 많아 잘사용하지않음

//        //포맷설정
//		String birthDay = "1991/07/16";
//
//		//member -- 일반 등록시.	
//		MgMemberTable item = new MgMemberTable();
//        item.setId("mg11");
//        item.setMgName("semi");     
//        item.setBirth_date(birthDay);
//        item.setEmail_address("semi@gmail.com");
//        item.setCreated_at(getNowTime());
//        item.setUpdated_at(getNowTime());
//        item.setInsert_user(my);
//        item.setUpdated_user(my);
//        item.setVersion(0);
//        
//		// mapper.save
//        dynamoDBMapper.save(item); //일반등록시.		      
		dynamoDBMapper.save(member); //Map에 event저장하여 등록시.
	
	}
	
	private String getNowTime() {
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("UTC")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(zdtjst);
        
        return nowDate;
	}

}