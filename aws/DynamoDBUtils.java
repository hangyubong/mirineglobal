package com.amazonaws.lambda.demo.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.util.StringUtils;

import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;
import com.amazonaws.lambda.demo.table.BaseTable;
import com.amazonaws.lambda.demo.table.MgClientTable;

public class DynamoDBUtils {

//	====================================== dynamo sdk v1 ==============================================	
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
	
	//===========================================================================================================
	public static void insertCommonItemV2(MgMemberTableV2 insertMember) {
		insertMember.setCreated_at(getTransactionTimeNow());
		insertMember.setUpdated_at(getTransactionTimeNow());
		insertMember.setInsert_user("insert-user");
		insertMember.setUpdated_user("insert-user");
		insertMember.setVersion(0);
	}
	
	public static void updateCommonItemV2(MgMemberTableV2 updateMember) {
		updateMember.setUpdated_at(getTransactionTimeNow());
		updateMember.setUpdated_at("update-user");
		updateMember.setVersion(updateMember.getVersion() + 1);
	}
	
	//ZonedDateTime UTC 현재시간 설정 
	private static String getTransactionTimeNow() { 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
		return ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);
	}
}
