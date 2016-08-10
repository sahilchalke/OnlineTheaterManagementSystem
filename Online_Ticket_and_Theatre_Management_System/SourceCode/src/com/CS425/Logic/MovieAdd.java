package com.CS425.Logic;

import java.util.ArrayList;
import java.util.Scanner;

import com.CS425.Db.DBMovieDetails;
import com.CS425.Db.DBStaffProcessing;
import com.CS425.bean.MovieInsert;
import com.CS425.bean.StaffDetails;

public class MovieAdd {

	static Scanner sc=new Scanner(System.in);

	public static void addMovieToTheatre() {

		boolean dbSuccess = false;
		while(!dbSuccess){
			System.out.println("Enter the movie name");
			String mname=sc.nextLine();

			System.out.println("Enter the year ");
			String year=sc.nextLine();

			System.out.println("Enter the genre");
			String genre=sc.nextLine();

			System.out.println("Enter the description");
			String desc=sc.nextLine();

			System.out.println("Enter the director");
			String director=sc.nextLine();

			System.out.println("Enter ratings for "+mname);
			float rating=Float.parseFloat(sc.nextLine());

			System.out.println("Enter the length of movie in munutes");
			int mlength=Integer.parseInt(sc.nextLine());

			System.out.println("Enter the Star Name");
			String starName=sc.nextLine();

			MovieInsert minsert= new MovieInsert(mname, year, genre, desc, director, 1, rating, mlength);

			if(DBMovieDetails.addMovie(minsert, starName))
			{
				System.out.println("The Movie is inserted successfully!!!!!\n");
				dbSuccess=true;
			}
			else
			{
				System.out.println("Error while inserting.Please try again");
			}



		}
	}

	public static void setMovieSchedule(StaffDetails staffD) {
		// TODO Auto-generated method stub
		boolean dbSuccess = false;
		Scanner sc=new Scanner(System.in);

		System.out.println("There are following movies which are currnetly running:-\n");

		ArrayList<String> movieList=new ArrayList<>();
		movieList=DBMovieDetails.displayMovieList();

		for(String movie: movieList)
		{
			System.out.println(movie);
		}
		System.out.print("\nPlease enter the movie: ");
		String name=sc.nextLine();
		
		String theatreName = "";
		if(staffD.getStaffId() == 1000){
			System.out.print("Please Select the theatre from the Below List:-\n");
			ArrayList<String> theatreList=new ArrayList<>();

			theatreList=DBMovieDetails.displayTheatre();

			for(String theatre: theatreList)
			{
				System.out.println(theatre);
			}

			theatreName=sc.nextLine();
		}
		else{
			DBStaffProcessing dbStaff = new DBStaffProcessing();
			theatreName = dbStaff.getTheatreFromId(staffD.getTheatreId());
		}
		System.out.println("Please select screen:");

		ArrayList<String> screenList=new ArrayList<>();

		screenList=DBMovieDetails.displayScreen(theatreName);
		for(String screen: screenList)
		{
			System.out.println(screen);
		}
		int screenNo=Integer.parseInt(sc.nextLine());

		System.out.println("Please enter the ShowTimes from Below List:-");
		System.out.println("1. 9A.M - 12P.M");
		System.out.println("2. 12P.M - 3P.M");
		System.out.println("3. 3P.M - 6P.M");
		System.out.println("4. 6P.M - 9P.M");
		System.out.println("5. 9P.M - 12P.M");

		String schedule=sc.nextLine();

		System.out.print("Please the the price for one ticket: ");
		int price=Integer.parseInt(sc.nextLine());

		System.out.print("Please enter the date (MM/DD/YYYY): ");
		String date=sc.nextLine();


		if(DBMovieDetails.insertSchedule(screenNo,theatreName, schedule,price,date,name))
		{
			System.out.println("The Movie Schedule is inserted successfully!!!!!\n");

		}
		else
		{
			System.out.println("Wromg entry");
		}
	}


}
