package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.FetchData;
import com.CS425.bean.StaffDetails;

public class ManagerHome {
	static Scanner sc=new Scanner(System.in);
	static StaffProcessing staffP = new StaffProcessing();
	static int option;

	public static void viewManagerHome(StaffDetails staffD){

		FetchData data = new FetchData();
		boolean isPriviledgedManager = data.isManagerPriviledged(staffD.getStaffId());
		boolean flag=true;
				
		while(flag){
			System.out.println("*************Welcome " + staffD.getName() + "**************");
			System.out.println("----------------------------------------------");
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - Hire Staff");
			System.out.println("2 - Manage Staff Schedule");
			System.out.println("3 - Set Credit Points Policy");
			if(isPriviledgedManager){
				System.out.println("4 - Set Movie Schedule");
				System.out.println("5 - Logout");
			}
			else 
				System.out.println("4 - Logout");

			option=Integer.parseInt(sc.nextLine());

			switch(option)
			{
			case 1: 
			{

				flag=staffP.hireStaff(staffD);
				break;
			}
			case 2:
			{
				flag=staffP.setStaffSchedule(staffD);
				break;
			}
			case 3:
			{
				UpdateCreditPolicy ucp = new UpdateCreditPolicy();
				ucp.updateCreditPolicy();
				break;
			}
			case 4:
			{	
				if(isPriviledgedManager)
					MovieAdd.setMovieSchedule(staffD);
				else
					flag=false;
				break;
			}
			case 5:
			{
				flag=false;
				break;
			}
			default:
				System.out.println("Please select the right oprion");
				break;
			}
		}
	}
}
