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
	
	public void insertMember() throws ParseException {

		String my = "hangyubong";
		
//		//현재시간설정(일본시간.) -- ***Calendar API는 사용시 타입불투명, 모호한상수, 월계산형식, 불변이아니므로 캘린도 공유시 변경값 같이 적용 등등 결함이 많아 잘사용하지않음
//		Date nowDate = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(nowDate);
//        calendar.set(Calendar.HOUR,calendar.get(Calendar.HOUR)+9);//
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        String date2 = simpleDateFormat.format(calendar.getTime());

		LocalDateTime ldtjst = LocalDateTime.now(ZoneId.of("Asia/Tokyo")); // LocalDateTime 사용 일본표준시간 JST 현재시간
		ZonedDateTime zdtjst = ZonedDateTime.now(ZoneId.of("Asia/Tokyo")); // ZonedDateTime 사용 일본표준시간 JST 현재시간
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        String nowDate = formatter.format(ldtjst); 
        String nowDate2 = formatter.format(zdtjst);
		
        //포맷설정
		String birthDay = "19910716";
		SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat newDtFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date formatDate = dtFormat.parse(birthDay);
		String strNewDtFormat = newDtFormat.format(formatDate);

		//member	
		MgMemberTable item = new MgMemberTable();
        item.setId("mg11");
        item.setMgName("semi");     
        item.setBirth_date(strNewDtFormat);
        item.setEmail_address("semi@gmail.com");
        item.setCreated_at(nowDate);
        item.setUpdated_at(nowDate2);
        item.setInsert_user(my);
        item.setUpdated_user(my);
        item.setVersion(0);
        
        
		// mapper.save
        dynamoDBMapper.save(item);		      

	
	}

}