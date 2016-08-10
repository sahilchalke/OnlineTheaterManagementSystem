package com.CS425.bean;

public class GuestDetails {
	
	private String name;
	private String email;
	private String phone;
	private String cardNumber;
	private String cardType;
	private String expiry;
	
	public String getName() {
		return name;
	}
	public String getEmail() {
		return email;
	}
	public String getPhone() {
		return phone;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
}
