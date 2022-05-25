package com.amazonaws.lambda.demo.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.amazonaws.util.StringUtils;
import com.amazonaws.lambda.demo.table.MgMemberTable;
import com.amazonaws.lambda.demo.table.MgClientTable;

public class DynamoDBUtils {

	
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
	
	
	public static void insertCommonItem(MgClientTable mgClientTable) {	
		mgClientTable.setCreated_at(getTransactionTimeNow());
		mgClientTable.setUpdated_at(getTransactionTimeNow());
		mgClientTable.setInsert_user("insert-user");
		mgClientTable.setUpdated_user("insert-user");
		mgClientTable.setVersion(0);
	}
	
	public static void updateCommonItem(MgClientTable mgClientTable) {
		mgClientTable.setUpdated_at(getTransactionTimeNow());
		mgClientTable.setUpdated_at("update-user");
		mgClientTable.setVersion(mgClientTable.getVersion() + 1);
	}
	
	private static String getTransactionTimeNow() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss'Z'");
		return ZonedDateTime.now(ZoneId.of("UTC")).format(formatter);
	}
}
