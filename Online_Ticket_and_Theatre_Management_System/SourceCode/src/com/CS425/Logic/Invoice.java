package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBTheatreDetails;
import com.CS425.Db.FetchData;
import com.CS425.bean.MovieSchedule;
import com.CS425.bean.UserCCDetails;

public class Invoice {

	public static boolean printInvoice(MovieSchedule selectedMovie, int memberId, String cardNumber, String theatre, int quantity) {

		Scanner sc = new Scanner(System.in);
		FetchData fdata = new FetchData(); 

		System.out.println("============Invoice===========");
		System.out.println("Order ID: " + fdata.getOrderId(cardNumber, selectedMovie.getScheduleId()));
		System.out.println("Movie: " + selectedMovie.getMovieName());
		System.out.println("Theatre: " + theatre);
		System.out.println("Address: " + DBTheatreDetails.getTheatreDetails(theatre));
		System.out.println("Day: " + selectedMovie.getDay());
		System.out.println("Time: " + selectedMovie.getScheduleTime());
		System.out.println("Quantity: " + quantity);
		System.out.println("Credit Card Number: Ending with " + cardNumber.substring(12, 16));
		System.out.println("Total Price: "+ quantity * selectedMovie.getPrice());
		System.out.println("==============================");

		System.out.println("\n1. Send Email");
		System.out.println("2. Back");
		if(memberId != 0)
			System.out.println("3. Logout");

		int option = Integer.parseInt(sc.nextLine());
		while(true){
			switch(option){
			case 1:
				System.out.println("Email sent....");
				return true;
			case 2:
				return true;
			case 3:
				return false;
			default:
				System.out.println("Invalid option.");
			}//switch
		}// while
	}
}