package com.CS425.Logic;

import java.util.ArrayList;
import java.util.Scanner;

import com.CS425.Db.DBMovieDetails;
import com.CS425.Db.DBQueries;
import com.CS425.Db.DBTheatreDetails;
import com.CS425.Db.FetchData;
import com.CS425.bean.GuestDetails;
import com.CS425.bean.MovieSchedule;
import com.CS425.bean.OrderDetails;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class TheatreDetails {

	public static boolean viewTheatreDetails(String theatre, UserDetails userD, UserCCDetails userCC)
	{
		Scanner sc=new Scanner(System.in);
		String address;
		address = DBTheatreDetails.getTheatreDetails(theatre);
		boolean ret = true;

		DBQueries.showTheatreReviews(theatre);

		while(ret){
			System.out.println("\n----------------" + theatre +"----------------------");
			System.out.println("Address: " + address);
			System.out.println("\nSelect from below options for the "+theatre+" Theatre");
			System.out.println("1. Buy Ticket");
			System.out.println("2. Previous Screen");
			System.out.println("3. Like the comment");
			if(userD != null){
				System.out.println("4. Create Discussion Thread");
				System.out.println("5. Reply on Discussion");
				System.out.println("6. Logout");
			}

			int choice=Integer.parseInt(sc.nextLine());

			switch(choice)
			{
			case 1:
				if(userD == null)
					ret = buyTicketGuest(theatre);
				else
					ret = buyTicketUser(theatre, userD, userCC, sc);
				break;

			case 2:

				return true;
			case 3:
				LikeTheatreComment like = new LikeTheatreComment();
				like.like(theatre);
				break;
			case 4:
				CreateTheatreReviewThread review = new CreateTheatreReviewThread();
				review.createTheatreReviewThread(userD, theatre);

				break;

			case 5:
				ReplyTheatreReview replyReview = new ReplyTheatreReview();
				replyReview.replyTheatreReview(userD, theatre);
				break;

			case 6:

				return false;
			default:
				System.out.println("Invalid option. Please enter again.");
			}// Switch
		}// While
		return false;
	}

	private static boolean buyTicketGuest(String theatre) {

		ArrayList<MovieSchedule> mScheduleList = new ArrayList<MovieSchedule>(); 
		MovieSchedule selectedMovie = null;
		GuestDetails gDetails = new GuestDetails();
		mScheduleList = DBTheatreDetails.getTheatreSchedule(theatre);
		Scanner sc = new Scanner(System.in);

		if(mScheduleList == null){
			System.err.println("**No movies screened in the theatre. Select another theatre.***");
			return true;
		}// if
		System.out.println("\nEnter following details to purchase ticket:");

		System.out.println("--Personal Details--");
		System.out.print("Enter your name: ");
		gDetails.setName(sc.nextLine());
		System.out.print("Enter email address: ");
		gDetails.setEmail(sc.nextLine());
		System.out.print("Enter phone number: ");
		gDetails.setPhone(sc.nextLine()); 

		System.out.println("--Credit Card Details--");
		System.out.print("Enter Card Type(MasterCard/Visa/AmericanExpress):");
		gDetails.setCardType(sc.nextLine());
		System.out.print("Enter 16-digit card number: ");
		gDetails.setCardNumber(sc.nextLine());
		System.out.print("Enter expiry: ");
		gDetails.setExpiry(sc.nextLine());

		System.out.println("--Booking Details--");
		System.out.print("Enter movie name:");
		String movieName = sc.nextLine();
		System.out.print("Enter day: ");
		String day = sc.nextLine();
		System.out.print("Enter Schedule Time: ");
		String time = sc.nextLine();
		System.out.print("Enter quantity: ");
		int quantity = Integer.parseInt(sc.nextLine());

		System.out.println("Confirm\n1. Yes\n2. No");
		if(sc.nextLine().equalsIgnoreCase("No"))
			return true;
		boolean found = false;
		for(MovieSchedule temp : mScheduleList){
			if(temp.getMovieName().equals(movieName) && temp.getScheduleTime().equals(time) && temp.getDay().equals(day)){
				selectedMovie = temp;
				found = true;
				break;
			}
		}// for	
		if(!found){
			System.out.println("Invalid details.");
			return true;
		}
		if(selectedMovie.getAvailability() < quantity){
			System.out.println("**Sorry no seats available for the current Movie!!**");
			return true;
		}
		if(DBTheatreDetails.purchaseTicketViaCC(gDetails, selectedMovie, quantity)){
			System.out.println("Ticket Booked Successfully!!");
			return Invoice.printInvoice(selectedMovie, 0, gDetails.getCardNumber(), theatre, quantity);
		}
		else
			System.out.println("Error while booking ticket");
		return true;
	}

	public static boolean buyTicketUser(String theatre, UserDetails userD, UserCCDetails userCC, Scanner sc)
	{
		ArrayList<MovieSchedule> mScheduleList = new ArrayList<MovieSchedule>(); 
		MovieSchedule selectedMovie = null;
		mScheduleList = DBTheatreDetails.getTheatreSchedule(theatre);

		if(mScheduleList == null){
			System.err.println("**No movies screened in the theatre. Select another theatre.***");
			return true;
		}

		System.out.println("\nEnter following details to purchase ticket:");
		System.out.print("Enter movie name: ");
		String movieName = sc.nextLine();
		System.out.print("Enter day: ");
		String day = sc.nextLine();
		System.out.print("Enter Schedule Time: ");
		String time = sc.nextLine();
		System.out.print("Enter quantity: ");
		int quantity = Integer.parseInt(sc.nextLine());
		
		boolean found = false;
		for(MovieSchedule temp : mScheduleList){
			if(temp.getMovieName().equals(movieName) && temp.getScheduleTime().equals(time) && temp.getDay().equals(day)){
				selectedMovie = temp;
				found = true;
				break;
			}
		}//for
		if(!found){
			System.out.println("Invalid details.");
			return true;
		}
		if(selectedMovie.getAvailability() < quantity){
			System.out.println("**Sorry no seats available for the current Movie!!**");
			return true;
		}
		System.out.print("**Do you want to redeem your membership points?**\n1. Yes\n2. No");
		String option = sc.nextLine();
		while(true){
			if(option.equals("1")){
				if(!processPurchase(true, selectedMovie, quantity, userD, userCC)){
					System.out.println("***Sorry!! Ticket not purchased.***");
					return true;
				}// if
				else{
					System.out.println("\nCredit Points successfully applied!!");
					return Invoice.printInvoice(selectedMovie, userD.getMemberId(), userCC.getCardNumber(), theatre, quantity);
				}
			}// if(option == "1")
			if(option.equals("2"))
				if(!processPurchase(false, selectedMovie, quantity, userD, userCC)){
					System.out.println("***Sorry!! Ticket not purchased.***");
					return true;
				}// if
				else{
					System.out.println("**Congratulations!!. You have earned some credit points for your purchase.**");
					return Invoice.printInvoice(selectedMovie, userD.getMemberId(), userCC.getCardNumber(), theatre, quantity);
				}
			else{
				System.out.println("Invalid option.");
				return true;
			}//else
		}//while
	}// function

	public static boolean processPurchase(boolean redeemPoints, MovieSchedule selectedMovie, int quantity, UserDetails userD, UserCCDetails userCC) {
		// TODO Auto-generated method stub
		FetchData data = new FetchData();
		System.out.println("**Processing purchase**\n");
		Scanner sc=new Scanner(System.in);

		int addCredit = 0;
		String memberStatus = userD.getStatus();
		addCredit = (int)((quantity * selectedMovie.getPrice()) * data.getCreditPurchasePolicy(memberStatus));

		if(redeemPoints){
			if((userD.getCreditPoints())< (selectedMovie.getPrice() * quantity)){
				System.out.println("Insufficient credit points. Do you wanna purchase via credit card.\n1. Yes\n2. No");
				if(sc.nextLine().equals("2"))
					return false;
				else
					return DBTheatreDetails.purchaseTicketViaCC(selectedMovie, quantity, userD, userCC, addCredit);
			}// if
			return DBTheatreDetails.purchaseTicketViaCreditPoints(selectedMovie, quantity, userD, userCC);
		}// if(redeemPoints) 
		return DBTheatreDetails.purchaseTicketViaCC(selectedMovie, quantity, userD, userCC, addCredit);
	}// function
}
