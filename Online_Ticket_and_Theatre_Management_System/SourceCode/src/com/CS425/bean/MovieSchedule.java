package com.CS425.bean;

public class MovieSchedule {
	
	private String movieName;
	private int screen;
	private String scheduleTime;
	private int availability;
	private int price;
	private String day;
	private int scheduleId;
	
	public MovieSchedule(String movieName, int screen, String scheduleTime,
			int availability, int price, String day, int scheduleId) {
		super();
		this.movieName = movieName;
		this.screen = screen;
		this.scheduleTime = scheduleTime;
		this.availability = availability;
		this.price = price;
		this.day = day;
		this.scheduleId = scheduleId;
	}

	public String getMovieName() {
		return movieName;
	}

	public int getScreen() {
		return screen;
	}

	public String getScheduleTime() {
		return scheduleTime;
	}

	public int getAvailability() {
		return availability;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public int getPrice() {
		return price;
	}

	public String getDay() {
		return day;
	}
}
