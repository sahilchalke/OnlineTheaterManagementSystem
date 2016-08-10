package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.Db.FetchData;
import com.CS425.bean.*;

public class UserHome
{
	int option;
	//need to get credit card details as well
	public void userHomeMenu(UserDetails userDetails, UserCCDetails ccDetails)
	{
		boolean flag = true;
		Scanner input = new Scanner(System.in);
		
		FetchData data =  new FetchData();
		
		while(flag)
		{
			System.out.printf("%n**************Welcome %s **************%n", userDetails.getUserName());
			System.out.println("----------------------------------------------");
			
			/*Recommendations*/
			DBQueries.movieRecommendations(userDetails.getMemberId());
			
			System.out.println("----------------------------------------------");
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - View/Edit Profile");
			System.out.println("2 - View Orders");
			System.out.println("3 - Search Movie");
			System.out.println("4 - Search Theatre");
			System.out.println("5 - Logout");
			option = Integer.parseInt(input.nextLine());
			
			switch(option)
			{
			case 1:
				{
					ViewEditProfile vep = new ViewEditProfile();
					boolean ret = vep.viewEditProfile(userDetails);
					flag = ret;
					break;
				}
			case 2:
				{
					UserOrderHistory uOrder = new UserOrderHistory();
					flag = uOrder.viewOrderHistory(userDetails.getMemberId());
					break;
				}
			case 3:
				{
					System.out.println("Enter Movie name: ");
					String movie = input.nextLine();
					if(data.validateMovie(movie))
						flag = MovieDetails.viewMovieDetail(movie, userDetails, ccDetails);
					else
						System.out.println("**Movie not found.**\n");
					break;
				}
			case 4:
				{
					String theatre;
					while(true){
						System.out.println("Enter Theatre name: ");
						theatre = input.nextLine();
						if(data.validateTheatre(theatre)){
							flag = TheatreDetails.viewTheatreDetails(theatre, userDetails, ccDetails);
							break;
						}// if
						else
							System.out.println("**Theatre not found.**\n");
					} //while
					break;
				}
			case 5:
				{
					flag = false;
					break;
				}
			default :
				{
					System.out.println("Please select from one of the provided option\n");
					break;
				}
			}
		}
	}
}
