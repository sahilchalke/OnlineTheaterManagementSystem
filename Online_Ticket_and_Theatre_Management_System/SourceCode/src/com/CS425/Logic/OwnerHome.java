package com.CS425.Logic;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.Scanner;

import com.CS425.Db.FetchData;
import com.CS425.bean.StaffDetails;

public class OwnerHome {

	static int option;

	public static void viewOwnerHome(StaffDetails staffD)
	{
		Scanner sc=new Scanner(System.in);
		StaffProcessing staffP = new StaffProcessing();
		boolean flag=true;

		while(flag){
			System.out.println("*************Welcome " + staffD.getName() + "**************");
			System.out.println("----------------------------------------------");
			System.out.println("\nPlease enter option number from below menu : \n");
			System.out.println("----------------------------------------------");
			System.out.println("1 - Hire Staff");
			System.out.println("2 - Manage Staff Schedule");
			System.out.println("3 - Set Credit Points Policy");
			System.out.println("4 - View Registered Users Details");
			System.out.println("5 - View All Tables");
			//System.out.println("6 - Add Movie to the Theatre");
			System.out.println("6 - Set Movie Schedule");
			System.out.println("7 - Delegate Responsibilities");
			System.out.println("8 - Logout");

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
				ViewRegisteredUserDetails vrud = new ViewRegisteredUserDetails();
				vrud.viewRegisteredUserDetails();
				break;
			}
			case 5:
			{
				ViewAllTables vt = new ViewAllTables();
				vt.ViewAllTables();
				break;
			}
			/*case 6:
			{	
				MovieAdd.addMovieToTheatre();
				break;
			}*/
			case 6:
			{
				MovieAdd.setMovieSchedule(staffD);
				break;
			}
			case 7:
			{
				delegateResponsibilities();
				break;
			}
			case 8:
				flag = false;
				break;
			default:
				System.out.println("Please enter right option");
				break;
			}//switch
		}//while
	}//method

	private static void delegateResponsibilities() {

		Scanner sc = new Scanner(System.in);
		FetchData data = new FetchData();
		ArrayList<String> managerList = new ArrayList<String>();
		int staffId = 0;
		System.out.println("------------------------------------\nFollowing managers work at respective locations:-");
		managerList = data.getManagers();
		for(String temp : managerList)
			System.out.println(temp);

		System.out.println("\nSelect from following option: \n1. Give Priviledges\n2. Remove Priviledges");
		int opt = Integer.parseInt(sc.nextLine());
		switch(opt){
		case 1:
			System.out.print("Enter staff_id: ");
			staffId = Integer.parseInt(sc.nextLine());
			if(!data.setPriviledge(staffId))
				System.out.println("Staff already has been given priviledges.");
			else
				System.out.println("Priviledges give for staff id: " + staffId);
			break;
		case 2:
			System.out.println("Enter staff_id:");
			staffId = Integer.parseInt(sc.nextLine());
			if(!data.removePriviledge(staffId))
				System.out.println("No priviledges were set for staff.");
			else
				System.out.println("Priviledges removed for staff id: " + staffId);
			break;
		}//switch
		System.out.println("-------------------------------------------");
	}
}
