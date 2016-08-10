package com.CS425.Logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.CS425.Db.DBMovieDetails;
import com.CS425.bean.MovieSchedule;
import com.CS425.bean.TheatreSchedule;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class MovieDetailSchedule {

	Scanner sc=new Scanner(System.in);

	public static void viewMovieSchedule(String movie, UserCCDetails userC, UserDetails userD){

		String name;
		String time;
		int quantity;
		int scheduleId;
		String day;
		ArrayList<TheatreSchedule> theatreSchedule=new ArrayList<TheatreSchedule>();
		Scanner sc=new Scanner(System.in);
		theatreSchedule=DBMovieDetails.displayMovieSchedule(movie);	
		TheatreSchedule ts = null;
		Iterator<TheatreSchedule> theatreItr=theatreSchedule.iterator();
		System.out.println("\n**The movie is running in the following theaters**");
		while(theatreItr.hasNext())
		{
			ts=theatreItr.next();
			System.out.println("--------------------------------------------------------");
			System.out.println("Theatre Name: "+ts.getTheatreName());
			//System.out.println("Availability: "+ts.getAvailability());
			System.out.println("Screen Number: "+ts.getScreenNumber());
			System.out.println("Price: "+ts.getPrice());
			System.out.println("Schedule: "+ts.getScheduleTime());
			System.out.println("Day: "+ts.getDay());
			System.out.println("Capacity: "+ts.getSeatsAvailable());
			System.out.println("Total Seats Available: "+ts.getAvailable());



		}
		System.out.println("--------------------------------------------------------------");
		System.out.print("\nEnter the theatre Name: ");
		name=sc.nextLine();
		System.out.print("Enter time: ");
		time=sc.nextLine();
		System.out.print("Enter quantity: ");
		quantity=Integer.parseInt(sc.nextLine());
		System.out.print("Enter the day: ");
		day=sc.nextLine();


		for(TheatreSchedule temp: theatreSchedule)
		{
			if (temp.getTheatreName().equals(name) && temp.getScheduleTime().equals(time) && temp.getDay().equals(day))
			{
				if(temp.getAvailability() < quantity){
					System.out.println("**Sorry no seats available for the current Movie!!**");
					break;
				}
				MovieDetailSchedule.purchaseTicket(movie, quantity, day, userC, userD, temp );
				break;
			}
		}//forEach
	}

	private static void purchaseTicket(String movie, int quantity, String day, UserCCDetails userC, UserDetails userD, TheatreSchedule temp ) {
		// TODO Auto-generated method stub
		System.out.println("");
		//To be dContinued
		Scanner sc=new Scanner(System.in); 
		
		System.out.println("Do you want to redeem your credit points\n1. Yes\n2. No");
		int choice=sc.nextInt();

		if(choice==2)
		{
			DBMovieDetails.purchaseTicket(movie,quantity,day,userC,userD,temp);

			//	DBMovieDetails.invoice(movie,quantity,day,userC,userD,temp);


		}
		else
		{
			if((userD.getCreditPoints()* 0.01)> (temp.getPrice()*quantity))
			{
				DBMovieDetails.purchaseTicketViaCreditPoints(movie,quantity,day,userC,userD,temp);
			}
			else
			{
				System.out.println("You have insufficient credit points to buy the tickets");
			}

			//MovieInvoice.printInvoice(movie,quantity,day,userC,userD,temp);



		}
		MovieSchedule selectedMovie=new MovieSchedule(movie, temp.getScreenNumber(), temp.getScheduleTime(), temp.getAvailability(), temp.getPrice(), day, temp.getScheduleId());			
		Invoice.printInvoice(selectedMovie, userD.getMemberId(), userC.getCardNumber(), temp.getTheatreName(), quantity);
	}

	public void viewMovieScheduleGuest(String movie) {
		// TODO Auto-generated method stub
		//System.out.println("Guest under construction");
		String name;
		String time;
		int quantity;
		int scheduleId;
		String day;
		ArrayList<TheatreSchedule> theatreSchedule=new ArrayList<TheatreSchedule>();
		Scanner sc=new Scanner(System.in);
		theatreSchedule=DBMovieDetails.displayMovieSchedule(movie);	
		TheatreSchedule ts;
		Iterator<TheatreSchedule> theatreItr=theatreSchedule.iterator();
		System.out.println("\n**The movie is running in the following theaters**");
		while(theatreItr.hasNext())
		{
			ts=theatreItr.next();
			System.out.println("--------------------------------------------------------");
			System.out.println("Theatre Name: "+ts.getTheatreName());
			//System.out.println("Availability: "+ts.getAvailability());
			System.out.println("Screen Number: "+ts.getScreenNumber());
			System.out.println("Price: "+ts.getPrice());
			System.out.println("Schedule: "+ts.getScheduleTime());
			System.out.println("Day: "+ts.getDay());
			System.out.println("Capacity: "+ts.getSeatsAvailable());
			System.out.println("Total Seats Available: "+ts.getAvailable());
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("\n**Booking Details**");
		System.out.print("Enter the theatre Name: ");
		name=sc.nextLine();
		System.out.print("Enter time: ");
		time=sc.nextLine();
		System.out.print("Enter quantity: ");
		quantity=Integer.parseInt(sc.nextLine());
		System.out.print("Enter the day: ");
		day=sc.nextLine();


		for(TheatreSchedule temp: theatreSchedule)
		{
			if (temp.getTheatreName().equals(name) && temp.getScheduleTime().equals(time) && temp.getDay().equals(day))
			{
				if(temp.getAvailability() < quantity){
					System.out.println("**Sorry no seats available for the current Movie!!**");
					break;
				}
				
				MovieDetailSchedule.purchaseTicketGuest(movie, quantity, day, temp );
			}
			else
				System.out.println("Invalid Details. Please Enter again!!");
		}//forEach

		
		

	}

	private static void purchaseTicketGuest(String movie, int quantity, String day, TheatreSchedule temp) {
		// TODO Auto-generated method stub
		
		Scanner sc=new Scanner(System.in);
		System.out.println("**Personal Details**");
		System.out.print("\nEnter your name: ");
		String name=sc.nextLine();
		System.out.print("Enter your email: ");
		String email=sc.nextLine();
		System.out.print( "Enter yout phone number: ");
		String phone=sc.nextLine();
		System.out.println("**Credit Card Details**");
		System.out.print("Enter your 16 Digit Credit Card number: ");
		String cc=sc.nextLine();
		System.out.print("Enter your expiry(MM/YY): ");
		String expiry=sc.nextLine();
		
		
		
		
		
		DBMovieDetails.guestPurchase(movie, quantity, day, temp, phone,cc, email);
		
		MovieSchedule selectedMovie= new MovieSchedule(movie, temp.getScreenNumber(), temp.getScheduleTime(), temp.getAvailability(), temp.getPrice(), temp.getDay(), temp.getScheduleId());
		
		
		Invoice.printInvoice(selectedMovie, 0, cc, temp.getTheatreName(), quantity);		
	}
}
