package com.amazonaws.lambda.demo.utils;

import com.amazonaws.util.StringUtils;

import software.amazon.awssdk.utils.CollectionUtils;

import java.util.Map;

import com.amazonaws.lambda.demo.table.ClientInfo2;
import com.amazonaws.lambda.demo.table.MgClient;
import com.amazonaws.lambda.demo.table.MgClientTableV2;
import com.amazonaws.lambda.demo.table.MgMember;
import com.amazonaws.lambda.demo.table.MgMemberTableV2;

public class TableMappingUtil {

	public static MgMemberTableV2 memberToMgMemberTable(MgMember member) {
		MgMemberTableV2 result = new MgMemberTableV2();

		if (!StringUtils.isNullOrEmpty(member.getId())) {
			result.setId(member.getId());
		}

		if (!StringUtils.isNullOrEmpty(member.getBirthDate())) {
			result.setBirth_date(member.getBirthDate());
		}
		
		if (!StringUtils.isNullOrEmpty(member.getMgName())) {
			result.setMgName(member.getMgName());
		}
		
		if (!StringUtils.isNullOrEmpty(member.getEmailAddress())) {
			result.setEmail_address(member.getEmailAddress());
		}

		return result;
	}
	
	public static MgClientTableV2 clientToMgClientTable(MgClient mgClient) {
		MgClientTableV2 result = new MgClientTableV2();
		ClientInfo2 result2 = new ClientInfo2();

		if (!StringUtils.isNullOrEmpty(mgClient.getId())) {
			result.setId(mgClient.getId());
		}

		if (!StringUtils.isNullOrEmpty(mgClient.getAddress())) {
			result2.setAddress(mgClient.getAddress());
		}
		
		if (!StringUtils.isNullOrEmpty(mgClient.getEmail_address())) {
			result2.setEmail_address(mgClient.getEmail_address());
		}
		
		if (!StringUtils.isNullOrEmpty(mgClient.getFull_name())) {
			result2.setFullName(mgClient.getFull_name());
		}

		return result;
	}
	
	public static MgClientTableV2 clientMapToMgClientTable(String id, Map<String, String> client_info) {
		MgClientTableV2 result = new MgClientTableV2();
		
		if (!StringUtils.isNullOrEmpty(id)) {
			result.setId(id);
		}

		if (!CollectionUtils.isNullOrEmpty(client_info)) {
			result.setClient_info(client_info);
		}
		return result;
	}
}
