package com.CS425.bean;

public class UserCCDetails {

	private int memberId;
	private String cardType;
	private String cardNumber;
	private String expiry;
	private String nameOnCard;
	
	public UserCCDetails(String cardType, String cardNumber, String expiry,
			String nameOnCard) {
		super();
		this.cardType = cardType;
		this.cardNumber = cardNumber;
		this.expiry = expiry;
		this.nameOnCard = nameOnCard;
	}

	public int getMemberId() {
		return memberId;
	}

	public String getCardType() {
		return cardType;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public String getExpiry() {
		return expiry;
	}

	public String getNameOnCard() {
		return nameOnCard;
	}
	
	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}
}
