package com.amazonaws.lambda.demo.utils;

import com.amazonaws.util.StringUtils;

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
}
