package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.UserDetails;

public class CreateReviewThread
{
	public void createThread(UserDetails userDetails, String movie)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter your review comment : ");
		String review = input.nextLine();
		DBQueries.createNewMovieReviewThread(userDetails.getMemberId(), movie, review);
		DBQueries.showMovieReviews(movie);
	}
	
}
