package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.UserDetails;

public class CreateTheatreReviewThread {
	
	public void createTheatreReviewThread(UserDetails userDetails, String theatre)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your review comment : ");
		String review = input.nextLine();
		DBQueries.createNewTheatreReviewThread(userDetails.getMemberId(), theatre, review);
		DBQueries.showTheatreReviews(theatre);
	}
	

}
