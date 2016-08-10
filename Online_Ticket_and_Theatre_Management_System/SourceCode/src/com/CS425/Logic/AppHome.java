package com.CS425.Logic;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.CS425.Db.DBQueries;
import com.CS425.Db.FetchData;
import com.CS425.bean.StaffDetails;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class AppHome {

	final static int GOLD_THRESHOLD = new FetchData().getMembershipThreshold("Silver");
	final static int PLATINUM_THRESHOLD = new FetchData().getMembershipThreshold("Gold");;
	final static String UPGRADE_MSG = "**Congratulations!!! You have just been promoted to ";
	final static String BENIFIT_MSG = "Now enjoy greater credit points when you purchase a ticket or write a review.";

	public static void main(String args[]){
		
		AppHome begin = new AppHome();
		begin.displayAppHomePage();
	}

	public void displayAppHomePage(){

		Scanner sc = new Scanner(System.in);
		boolean breakWhile = false;
		int option;
		String email, pass, movie, theatre;
		FetchData data =  new FetchData();
		UserDetails userD;
		UserCCDetails userCC;

		StaffDetails staffD;
		

		while(!breakWhile){
			System.out.println("*********Welcome to AMC Movies*********\n");
			System.out.println("----------Featured Movies-----------");
			data.appHomeMovieReco();
			System.out.println("------------------------------------\n");
			System.out.println("-----------Menu-------------");
			System.out.println("1. Login\n2. Sign Up\n3. Search Movie\n4. Search Theatre\n5. Queries\n6. Exit");
			System.out.println("----------------------------");
			option = Integer.parseInt(sc.nextLine());

			switch(option){
			case 1:
				while(true){
					System.out.print("Enter email Id: ");
					email = sc.nextLine();
					System.out.print("Enter password: ");
					pass = sc.nextLine();
					UserHome uHome;
					StaffHome sHome;
					if(data.validateUserLogin(email, pass)){
						String authorityType = getAuthorityType(email).toLowerCase();

						switch(authorityType){

							case "non-staff":
								userD = data.getUserDetails(email);
								userCC = data.getUserCCDetails(userD.getMemberId());
								checkMemberShipUpgrade(userD);
								uHome = new UserHome();
								uHome.userHomeMenu(userD, userCC);
								break;
							case "staff":
								String staffType = getStaffType(email);
								staffD = data.getStaffDetails(email);
								if(staffType.equalsIgnoreCase("Owner"))
									OwnerHome.viewOwnerHome(staffD); 
								else if(staffType.equalsIgnoreCase("Manager"))
									ManagerHome.viewManagerHome(staffD);
								else if (staffType.equalsIgnoreCase("WEB ADMIN"))
									WebAdminHome.webAdminHome(staffD);
								else
								{
									userD = data.getUserDetails(email);
									userCC = data.getUserCCDetails(userD.getMemberId());
									checkMemberShipUpgrade(userD);
									sHome = new StaffHome();
									sHome.staffHomeMenu(userD, userCC);
								}
								break;
						}//switch
						break;
					}// if
					else{
						System.out.println("**Username not found or incorrect password.**\n");
					} //else
				} //while
				break;
			case 2:
				System.out.println("#SIGN UP#\n");
				signUpUser(sc, data);
				break;
			case 3:
				while(true){
					System.out.println("Enter Movie name: ");
					movie = sc.nextLine();
					if(data.validateMovie(movie)){
						MovieDetails.viewMovieDetail(movie, null, null);
						break;
					}// if
					else
						System.out.println("**Movie not found.**\n");
				} //while
				break;
			case 4:
				while(true){
					System.out.println("Enter Theatre name: ");
					theatre = sc.nextLine();
					if(data.validateTheatre(theatre)){
						TheatreDetails.viewTheatreDetails(theatre, null, null);
						break;
					}// if
					else
						System.out.println("**Theatre not found.**\n");
				} //while
				break;
			case 5:
				/* Queries result will come here*/
				executeQueries();
				break;
			case 6:
				System.out.println("Good Bye!!!!");
				breakWhile = true;
				sc.close();
				break;
			} // switch
		} // while
	}

	private void executeQueries() {
		// TODO Auto-generated method stub
		Scanner sc=new Scanner(System.in);
		int choice;
		boolean flag = true;
		while(flag){
			System.out.println("\nSelect the choice from below");
			System.out.println("1. Display the 3 most recent discussions/comments from a specific discussion thread");
			System.out.println("2. Display the 3 most recent discussion/comments from all discussion threads");
			System.out.println("3. Display the least popular discussion thread in terms of visits and comments");
			System.out.println("4. Display the registered guest who has contributed most comments");
			System.out.println("5. Display the theatre showing maximum number of movies");
			System.out.println("6. Display theatre with maximum number of online ticket sales");
			System.out.println("7. Display the list of all employees who are on duty on Monday on a specific theatre. "
					+ "Display also their jobs and time table.");
			System.out.println("8. Previous screen");

			choice=Integer.parseInt(sc.nextLine());

			switch(choice)
			{
			case 1:
				DBQueries.executeQuery1();
				break;
			case 2:
				DBQueries.executeQuery2();
				break;	
			case 3:
				DBQueries.executeQuery3();
				break;
			case 4:
				DBQueries.executeQuery5();
				break;	
			case 5:
				DBQueries.executeQuery6();
				break;
				
			case 6:
				DBQueries.executeQuery7();
				break;
			
			case 7:
				System.out.println("Enter the theatre which you want to view the schedule of employees");
				String tname=sc.nextLine();
				DBQueries.executeQuery8(tname);
				break;
				
			case 8:
				flag=false;
				break;
			
				
			}//switch
		}//while
	}

	private String getStaffType(String email) {

		FetchData data = new FetchData();
		return data.getStaffTypeByEmail(email);
	}

	private String getAuthorityType(String email) {
		FetchData data = new FetchData();
		return data.getAuthorityByEmail(email);
	}

	private void checkMemberShipUpgrade(UserDetails userD) {

		int memberPoints = userD.getMemberShipPoints();
		FetchData data = new FetchData();
		if(memberPoints >= GOLD_THRESHOLD && !userD.getStatus().equalsIgnoreCase("gold") 
				&& !userD.getStatus().equalsIgnoreCase("platinum")){
			data.upgradeMembershipStatus(userD.getMemberId(), "GOLD");
			System.out.println(UPGRADE_MSG + "Gold status**");
			System.out.println(BENIFIT_MSG);
		}
		if(memberPoints >= PLATINUM_THRESHOLD && !userD.getStatus().equalsIgnoreCase("platinum")){
			data.upgradeMembershipStatus(userD.getMemberId(), "PLATINUM");
			System.out.println(UPGRADE_MSG + "Platinum status**");
			System.out.println(BENIFIT_MSG);
		}
	}

	private void signUpUser(Scanner sc, FetchData data) {

		boolean dbSuccess = false;
		while(!dbSuccess){
			UserDetails user;
			UserCCDetails userCC;

			System.out.print("Name: ");
			String name=sc.nextLine();

			System.out.print("Phone Number: ");
			String phone=sc.nextLine();

			System.out.print("Address: ");
			String address=sc.nextLine();

			System.out.print("Date of Birth(MM-DD-YYYY): ");
			String dob=sc.nextLine();

			System.out.print("Email: ");
			String email=sc.nextLine();

			System.out.print("Gender (Male/Female): ");
			String gender=sc.nextLine();

			System.out.println("\n** Please enter your credit card details **");

			System.out.print("Credit Card Type (MasterCard/Visa/AmericanExpress): ");
			String cardType = sc.nextLine();

			System.out.print("Card Number: ");
			String cardNumber = sc.nextLine();

			System.out.print("Name on Card: ");
			String nameOnCard = sc.nextLine();

			System.out.print("Expiry: ");
			String expiry = sc.nextLine();

			System.out.print("Password: ");
			String password = sc.nextLine();
			//check if user is staff
			if(data.getStaffTypeByEmail(email) != null)
				user = new UserDetails(name, address, phone, dob, email, gender, 0, 0, "Gold", "Staff");
			else
				user = new UserDetails(name, address, phone, dob, email, gender, 0, 0, "Silver", "Non-Staff");

			userCC = new UserCCDetails(cardType, cardNumber, expiry, nameOnCard);

			if(data.insertUserDetails(user, userCC) && data.insertUserLoginDetails(email, password)){
				System.out.println("User registered succesfully. Redirecting to home page.\n");
				try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				dbSuccess = true;
			}
			else{
				System.out.println("Error while registering user. Please enter details again.");
			}
		}// while
	}// method
}