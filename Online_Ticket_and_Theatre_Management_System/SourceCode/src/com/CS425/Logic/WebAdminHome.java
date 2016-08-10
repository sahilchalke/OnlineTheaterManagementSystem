package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;
import com.CS425.bean.StaffDetails;

public class WebAdminHome
{
	public static void webAdminHome(StaffDetails staffD)
	{
		Scanner sc=new Scanner(System.in);
		StaffProcessing staffP = new StaffProcessing();
		boolean flag=true;

		
		while(flag){
			System.out.println("*************Welcome " + staffD.getName() + "**************");
			System.out.println("----------------------------------------------");
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - View All Tables");
			System.out.println("2 - Update a table");
			System.out.println("3 - View registered users details");
			System.out.println("4 - Logout");

			int option=Integer.parseInt(sc.nextLine());
			switch(option)
			{
			case 1: 
			{

				ViewAllTables vt = new ViewAllTables();
				vt.ViewAllTables();
				break;
			}
			case 2:
			{
				System.out.println("Please enter update query you want to run on Database");
				String query = sc.nextLine();
				DBQueries.updateTable(query);
				break;
			}
			case 3:
			{
				ViewRegisteredUserDetails vrud = new ViewRegisteredUserDetails();
				vrud.viewRegisteredUserDetails();
				break;
			}
			case 4:
			{
				flag = false;
				break;
			}
			
			default:
				System.out.println("Please enter right option");
				break;
			}//switch
		}//while
	}
}
