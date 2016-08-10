package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;

public class LikeComment 
{
	public void like(String movie)
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the review number which you want to like : ");
		int review_id = Integer.parseInt(input.nextLine());
		DBQueries.likeComment(review_id);
		DBQueries.showMovieReviews(movie);
		
	}
}
