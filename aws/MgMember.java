package com.amazonaws.lambda.demo.table;

public class MgMember {
	
	String id;
	String mgName;
	String birthDate;
	String emailAddress;
	
	public MgMember(String id, String mgName, String birthDate, String emailAddress) {
		super();
		this.id = id;
		this.mgName = mgName;
		this.birthDate = birthDate;
		this.emailAddress = emailAddress;
	}
	
	public String getId() {
		return id;
	}
	public String getMgName() {
		return mgName;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setMgName(String mgName) {
		this.mgName = mgName;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

//	@Override
//	public String toString() {
//		return "MgMember [id=" + id + ", mgName=" + mgName + ", birthDate=" + birthDate + ", emailAddress="
//				+ emailAddress + "]";
//	}

	
	
}
