package com.CS425.Logic;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.CS425.Db.DBStaffProcessing;
import com.CS425.bean.StaffDetails;

public class StaffProcessing {

	Scanner sc = new Scanner(System.in);
	DBStaffProcessing dbstaff = new DBStaffProcessing();

	public boolean setStaffSchedule(StaffDetails staff) {

		boolean exit = true;
		int staff_id;
		String date;
		String startTime;
		String endTime;
		ArrayList<String> unscheduledStaff = new ArrayList<String>();
		ArrayList<String> scheduledStaff = new ArrayList<String>();
		DBStaffProcessing dbStaff = new DBStaffProcessing();

		while(exit)
		{
			System.out.println("\n**Staff Schedule Manager**");
			System.out.println("Please enter your choice:");
			System.out.println("1 - Set Staff Schedule");
			System.out.println("2 - Update Staff Schedule");
			System.out.println("3 - Exit");
			String in = sc.nextLine();
			switch (in)
			{
			case "1" :
				unscheduledStaff = dbStaff.getUnscheduledStaffList(staff.getTheatreId(), staff.getStaffId());
				if(unscheduledStaff.size() == 0){
					System.out.println("No staff hired.");
					break;
				}
				if(staff.getStaffId() == 1000)
					System.out.println("Staff currently working in AMC Group of Theater: \nStaff ID\tStaff Name\t Works At");
				else
					System.out.println("Staff currently working in your theater: \nStaff ID\tStaff Name\t Works At");
				for(String temp : unscheduledStaff)
					System.out.println(temp);
				while(true){
					System.out.print("Enter the staff ID: ");
					staff_id = Integer.parseInt(sc.nextLine());
					System.out.print("Enter date in yyyy-mm-dd: ");
					date = sc.nextLine();
					System.out.print("Enter the start time of the shift in 24hrs  e.g 13.30: ");
					startTime = sc.nextLine();
					System.out.print("Enter the end time of the shift in 24hrs  e.g 20.30: ");
					endTime = sc.nextLine();

					if(dbStaff.addSchedule(staff_id, date, startTime, endTime))
						break;
				}
				break;

			case "2" :
				scheduledStaff = dbStaff.getScheduledStaffList(staff.getTheatreId(), staff.getStaffId());
				if(scheduledStaff.size() == 0){
					System.out.println("Schedule has not been set for any staff. Please set staff schedule first.");
					break;
				}
				if(staff.getStaffId() == 1000)
					System.out.println("Staff currently working in AMC Group of Theater: \nStaff ID\tStaff Name\t Works At");
				else
					System.out.println("Staff currently working in your theater: \nStaff ID\tStaff Name\t Works At");
				
				for(String temp : scheduledStaff)
					System.out.println(temp);

				while(true){
					System.out.printf("Enter the staff ID: ");
					staff_id = Integer.parseInt(sc.nextLine());
					dbStaff.getSchedule(staff_id);
					System.out.print("Enter date in yyyy-mm-dd for which schedule needs to be updated: ");
					date = sc.nextLine();
					System.out.print("Enter the start time of the shift in 24hrs  e.g 13.30: ");
					startTime = sc.nextLine();
					System.out.print("Enter the end time of the shift in 24hrs  e.g 20.30: ");
					endTime = sc.nextLine();
					if(dbStaff.updateSchedule(staff_id, date, startTime, endTime))
						break;	   
				}
				break;
			case "3":
				return true;
			default:
				System.out.println("Please enter valid input");
				break;
			}		    
		}
		return true;
	}

	public boolean hireStaff(StaffDetails staff) {

		boolean dbSuccess = false;
		while(!dbSuccess){
			StaffDetails staffD = new StaffDetails();
			if(staff.getStaffId() == 1000)
				System.out.println("\n*-----Staff Hiring Form for AMC group of Theaters-----*\n");
			else
				System.out.println("\n*-----Staff Hiring Form-----*\n"); 

			System.out.print("Full Name: ");
			staffD.setName(sc.nextLine());

			System.out.print("Address: ");
			staffD.setAddress(sc.nextLine());

			System.out.print("Phone No: ");
			staffD.setPhone(sc.nextLine());

			System.out.print("Email: ");
			staffD.setEmail(sc.nextLine());

			System.out.print("9-digit SSN: ");
			staffD.setSsn(sc.nextLine());

			System.out.print("Date of joining(MM-DD-YYYY): ");
			staffD.setDateOfJoining(sc.nextLine());

			boolean done = false;
			while(!done){
				System.out.println("Select from following work category: ");
				System.out.println("\ta. Cleaning");
				System.out.println("\tb. Ticketing");
				System.out.println("\tc. Snack Provider");
				System.out.println("\td. Security");
				if(staff.getStaffId() == 1000)
					System.out.println("\te. Manager");

				switch(sc.nextLine().toLowerCase()){
				case "a":
					staffD.setDescId(1015);
					done = true;
					break;
				case "b":
					staffD.setDescId(1014);
					done = true;
					break;
				case "c":
					staffD.setDescId(1013);
					done = true;
					break;
				case "d":
					staffD.setDescId(1012);
					done = true;
					break;
				case "e":
					staffD.setDescId(1011);
					done = true;
					break;
				default:
					System.out.println("Invalid option. Enter again.\n");
				}// switch
			}// while

			if(staff.getStaffId() == 1000){
				ArrayList<String> allTheaterList = new ArrayList<String>();
				allTheaterList = dbstaff.fetchTheatreList();
				System.out.println("Select 'Location Id' following work locations: ");
				for(String temp : allTheaterList)
					System.out.println("\t" + temp);
				System.out.print("Work Location Id: ");
				staffD.setTheatreId(Integer.parseInt(sc.nextLine()));
			}
			else
				staffD.setTheatreId(staff.getTheatreId());

			System.out.println("Confirm.\n1. Yes\n2. No");
			if(sc.nextLine().equalsIgnoreCase("No")){
				System.out.println("Action cancelled");
				return true;
			}

			if(dbstaff.insertStaffToDb(staffD)){
				System.out.println("Staff registered succesfully. Redirecting to home page.\n");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dbSuccess = true;
			}// if
			else{
				System.out.println("Error while registering user. Please enter details again.");
			}// else
		}// while
		return true;
	}
}