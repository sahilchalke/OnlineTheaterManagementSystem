package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;

public class ViewRegisteredUserDetails {


	public void viewRegisteredUserDetails()
	{
		DBQueries.viewRegisteredUserDetails();
		Scanner input = new Scanner(System.in);
		while(true){
			System.out.println("Enter member ID to see more details or R to return");
			String member_id = input.nextLine();
			if(member_id.toUpperCase().equals("R"))
				return;
			else
				DBQueries.viewDetailedRegisteredUserDetails(member_id);
		}
	}
}
