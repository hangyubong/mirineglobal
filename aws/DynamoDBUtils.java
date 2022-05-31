package com.amazonaws.lambda.demo.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.util.StringUtils;

import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;
import com.amazonaws.lambda.demo.table.MgMemberTableV3;
import com.amazonaws.lambda.demo.table.BaseTable;
import com.amazonaws.lambda.demo.table.MgClientTable;

public class DynamoDBUtils {

//	====================================== dynamo sdk v1 Client ==============================================	
	public static void insertCommonItem(MgClientTable clientTable) {
		clientTable.setCreated_at(getTransactionTimeNow());
		clientTable.setUpdated_at(getTransactionTimeNow());
		clientTable.setInsert_user("insert-user");
		clientTable.setUpdated_user("insert-user");
		clientTable.setVersion(0);
	}
	
	public static void updateCommonItem(MgClientTable clientTable) {
		clientTable.setUpdated_at(getTransactionTimeNow());
		clientTable.setUpdated_at("update-user");
		clientTable.setVersion(clientTable.getVersion() + 1);
	}
	
//	====================================== END dynamo sdk v1 Client ==============================================	
	
	
//	====================================== dynamo sdk v1 Member ==============================================	
	public static void insertCommonItem(MgMemberTable mgMemberTable) {
		mgMemberTable.setCreated_at(getTransactionTimeNow());
		mgMemberTable.setUpdated_at(getTransactionTimeNow());
		mgMemberTable.setInsert_user("insert-user");
		mgMemberTable.setUpdated_user("insert-user");
		mgMemberTable.setVersion(0);
	}
	
	public static void updateCommonItem(MgMemberTable mgMemberTable) {
		mgMemberTable.setUpdated_at(getTransactionTimeNow());
		mgMemberTable.setUpdated_at("update-user");
		mgMemberTable.setVersion(mgMemberTable.getVersion() + 1);
	}
	
//	====================================== END dynamo sdk v1 Member ==============================================	
	
//	====================================== dynamo sdk v2 Member ==============================================		
	public static void insertCommonItemV(BaseTable baseTable) {
		baseTable.setCreatedAt(getTransactionTimeNow());
		baseTable.setUpdatedAt(getTransactionTimeNow());
		baseTable.setInsertUser("insert-user");
		baseTable.setUpdatedUser("insert-user");
		baseTable.setVersion(0);
	}
	
	public static void insertCommonItemV2(MgMemberTableV2 insertMember) {
		insertMember.setCreated_at(getTransactionTimeNow());
		insertMember.setUpdated_at(getTransactionTimeNow());
		insertMember.setInsert_user("insert-user");
		insertMember.setUpdated_at("insert-user");
		insertMember.setVersion(0);
	}
	
	public static void updateCommonItemV2(MgMemberTableV2 updateMember) {
		updateMember.setUpdated_at(getTransactionTimeNow());
		updateMember.setUpdated_at("update-user");
		updateMember.setVersion(updateMember.getVersion() + 1);
	}
//	====================================== END dynamo sdk v2 Member ==============================================	
	
	//ZonedDateTime UTC 현재시간 설정 
	private static String getTransactionTimeNow() { 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
		return ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);
	}
}
