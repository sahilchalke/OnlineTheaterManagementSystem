package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;

public class LikeTheatreComment {
	
	public void like(String theatre)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the review number which you want to like : ");
		int review_id = Integer.parseInt(input.nextLine());
		DBQueries.likeComment(review_id);
		DBQueries.showTheatreReviews(theatre);
		
	}

}
