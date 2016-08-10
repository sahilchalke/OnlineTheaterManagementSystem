package com.CS425.bean;

public class TheatreSchedule {
	
	private String theatreName;
	private String scheduleTime;
	private int availability;
	
	private int price;
	private String day;
	private int screenNumber;
	private int seatsAvailable;
	private int scheduleId;
	private int available;
	
	public TheatreSchedule(String theatreName, String scheduleTime, int availability, int price, String day,
			int screenNumber, int seatsAvailable, int scheduleId, int available) {
		super();
		this.theatreName = theatreName;
		this.scheduleTime = scheduleTime;
		this.availability = availability;
		this.price = price;
		this.day = day;
		this.screenNumber = screenNumber;
		this.seatsAvailable = seatsAvailable;
		this.scheduleId=scheduleId;
		this.available=available;
	}
	
	public int getAvailable() {
		return available;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public int getAvailability() {
		return availability;
	}

	public int getPrice() {
		return price;
	}

	public String getDay() {
		return day;
	}

	public int getScreenNumber() {
		return screenNumber;
	}

	public int getSeatsAvailable() {
		return seatsAvailable;
	}

	
	
	
	
}
