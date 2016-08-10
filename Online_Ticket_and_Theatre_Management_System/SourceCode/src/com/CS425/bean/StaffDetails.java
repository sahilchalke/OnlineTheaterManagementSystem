package com.CS425.bean;

public class StaffDetails {

	private int staffId;
	private String name;
	private String phone;
	private String ssn;
	private String dateOfJoining;
	private int descId;
	private String address;
	private String email;
	private int TheatreId;
	
	public StaffDetails(int staffId, String name, String phone, String ssn,
			String dateOfJoining, int descId, String address, String email,
			int theatreId) {
		super();
		this.staffId = staffId;
		this.name = name;
		this.phone = phone;
		this.ssn = ssn;
		this.dateOfJoining = dateOfJoining;
		this.descId = descId;
		this.address = address;
		this.email = email;
		TheatreId = theatreId;
	}
	
	public StaffDetails(){};

	public int getStaffId() {
		return staffId;
	}

	public String getPhone() {
		return phone;
	}

	public String getSsn() {
		return ssn;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public int getDescId() {
		return descId;
	}

	public String getAddress() {
		return address;
	}

	public String getEmail() {
		return email;
	}

	public int getTheatreId() {
		return TheatreId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public void setDescId(int descId) {
		this.descId = descId;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTheatreId(int theatreId) {
		TheatreId = theatreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
