package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.UserDetails;

public class ViewEditProfile
{
	int option;
	
	public boolean viewEditProfile(UserDetails userDetails)
	{
		System.out.println("\nYour Account details :");
		
		DBQueries.viewProfile(userDetails.getMemberId());
		
		boolean flag = true;
		
		Scanner input = new Scanner(System.in);
		
		while(flag)
		{
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - Edit profile");
			System.out.println("2 - Previous Menu");
			System.out.println("3 - Logout");
			
			option = Integer.parseInt(input.nextLine());
			
			switch(option)
			{
			case 1:
				{
					EditProfile ep = new EditProfile();
					boolean ret = ep.editProfile(userDetails);
					flag = ret;
					if(flag == false)
						return false;
					break;
				}
			case 2:
				{
					flag = false;
					return true;
					
				}
			case 3:
				{
					flag = false;
					return false;
				}
			
			default :
				{
					System.out.println("Please select from one of the provided option\n");
					break;
				}
			}
			

		}
		return true;
		
	}
}
