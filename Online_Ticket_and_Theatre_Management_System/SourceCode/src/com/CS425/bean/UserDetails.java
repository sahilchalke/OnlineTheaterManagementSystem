package com.CS425.bean;

import java.sql.Date;

public class UserDetails {
	
	private int memberId;
	private String userName;
	private String address;
	private String phone;
	private String dateOfBirth;
	private String emailId;
	private String gender;
	private int creditPoints;
	private int memberShipPoints;
	private String status;
	private String role;
	
	public UserDetails(String userName, String address, String phone, String date,
			String emailId, String gender, int creditPoints,
			int memberShipPoints, String status, String role) {
		super();
		this.userName = userName;
		this.address = address;
		this.phone = phone;
		this.dateOfBirth = date;
		this.emailId = emailId;
		this.gender = gender;
		this.creditPoints = creditPoints;
		this.memberShipPoints = memberShipPoints;
		this.status = status;
		this.role = role;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getMemberId() {
		return memberId;
	}

	public String getUserName() {
		return userName;
	}

	public String getAddress() {
		return address;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public String getEmailId() {
		return emailId;
	}

	public String getGender() {
		return gender;
	}

	public int getCreditPoints() {
		return creditPoints;
	}

	public int getMemberShipPoints() {
		return memberShipPoints;
	}

	public String getStatus() {
		return status;
	}

	public String getRole() {
		return role;
	}
	
	public String getPhone() {
		return phone;
	}
}
