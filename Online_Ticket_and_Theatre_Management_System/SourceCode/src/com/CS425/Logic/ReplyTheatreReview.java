package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.UserDetails;

public class ReplyTheatreReview {
	
	public void replyTheatreReview(UserDetails userDetails, String theatre)
	{
		int review_id;
		String review;
		Scanner input = new Scanner(System.in);
		System.out.println("\nPlease enter review number on which you want to comment : ");
		review_id = Integer.parseInt(input.nextLine());
		boolean validate = DBQueries.verifyReviewID(review_id);
		if(!validate)
		{
			System.out.println("The review number you have entered is not valid\n");
			return;
		}
		
		System.out.println("Please enter your comment :");
		review = input.nextLine();
		DBQueries.insertMovieReviewsReply(userDetails.getMemberId(),review_id , review);
		DBQueries.showTheatreReviews(theatre);
	}

}
