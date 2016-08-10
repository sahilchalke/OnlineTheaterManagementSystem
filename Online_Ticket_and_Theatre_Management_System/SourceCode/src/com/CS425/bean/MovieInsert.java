package com.CS425.bean;

public class MovieInsert {
	
	private int movieId;
	private String title;
	private String year;
	private String genre;
	private String desc;
	private String director;
	private int now_showing;
	private float rating;
	private int length;
	
	
	public MovieInsert( String title, String year, String genre, String desc, String director,
			int now_showing, float rating, int length) {
		super();
		this.movieId = movieId;
		this.title = title;
		this.year = year;
		this.genre = genre;
		this.desc = desc;
		this.director = director;
		this.now_showing = now_showing;
		this.rating = rating;
		this.length = length;
	}
	
	
	
	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}



	public int getMovieId() {
		return movieId;
	}
	public String getTitle() {
		return title;
	}
	public String getYear() {
		return year;
	}
	public String getGenre() {
		return genre;
	}
	public String getDesc() {
		return desc;
	}
	public String getDirector() {
		return director;
	}
	public int getNow_showing() {
		return now_showing;
	}
	public float getRating() {
		return rating;
	}
	public int getLength() {
		return length;
	}
	
	
	
	
}
