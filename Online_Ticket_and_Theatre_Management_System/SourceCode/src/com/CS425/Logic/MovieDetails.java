 package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBMovieDetails;
import com.CS425.Db.DBQueries;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class MovieDetails {
	
	public static boolean viewMovieDetail(String movie, UserDetails userDetails, UserCCDetails ccDetails)
	{	
		Scanner sc=new Scanner(System.in);
		
		DBMovieDetails.getMovieDetails(movie);
		DBQueries.showMovieReviews(movie);
		
		boolean flag = true;
		
		while(flag)
		{
			System.out.println("\nSelect from below options for the "+movie+" movie");
			System.out.println("1. Buy Ticket");
			System.out.println("2. Previous Screen");
			System.out.println("3. Like the comment");
			if(userDetails != null)
			{
				System.out.println("4. Create a Discussion Thread");
				System.out.println("5. Reply on Discussion");
				
				System.out.println("6. Logout");
			}
			
			
			int choice=Integer.parseInt(sc.nextLine());
			
			switch(choice)
			{
				case 1:
					if(userDetails != null)
					{
						buyTicket(movie, ccDetails, userDetails);
					}
					else 
						buyGuestTickets(movie);
					break;
				case 2:
					{
						flag = false;
						return true;
					}
				
				case 3 :
				{
					LikeComment like = new LikeComment();
					like.like(movie);
					break;
				}
					
				case 4:
					{
						CreateReviewThread review = new CreateReviewThread();
						review.createThread(userDetails, movie);
						
						break;
					}
				
				case 5 :
				{
					ReplyMovieReview replyReview = new ReplyMovieReview();
					replyReview.replyMovieReview(userDetails, movie);
					break;
				}
				
				
				case 6 :
				{
					flag = false;
					return false;
				}
				
			}
			
		}
		
		return true;
		
	}
	
	private static void buyGuestTickets(String movie) {
		// TODO Auto-generated method stub
		MovieDetailSchedule mds= new MovieDetailSchedule();
		mds.viewMovieScheduleGuest(movie);
		
	}

	public static void buyTicket(String movie, UserCCDetails userC, UserDetails userD)
	{
		MovieDetailSchedule mds= new MovieDetailSchedule();
		mds.viewMovieSchedule(movie, userC, userD);
		
		
	}
}
