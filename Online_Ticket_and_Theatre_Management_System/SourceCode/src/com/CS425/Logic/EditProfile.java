package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.UserDetails;

public class EditProfile
{
	int option;
		
	
	public boolean editProfile(UserDetails userDetails)
	{
		boolean flag = true;
		Scanner input = new Scanner(System.in);
		while(flag)
		{
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - Update phone number ");
			System.out.println("2 - Update address ");
			System.out.println("3 - Previous Menu");
			System.out.println("4 - Logout");
			
			option = Integer.parseInt(input.nextLine());
			
			switch(option)
			{
			case 1:
				{
					System.out.println("Enter new phone number :");
					String value = input.nextLine();
					DBQueries.updateProfile(userDetails.getMemberId(), option, value);
					break;
				}
			case 2:
				{
					System.out.println("Enter new address :");
					String value = input.nextLine();
					DBQueries.updateProfile(userDetails.getMemberId(), option, value);
					break;
				}
			case 3:
				{
					flag = false;
					return true;
					
				}
			case 4:
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
