package com.CS425.Db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class DBQueries
{


	public static void movieRecommendations(int memberId)
	{
		ResultSet rs1, rs2;
		String str1 = "select title from movie where now_showing = 1 and genre in (select movie.genre from purchase, orderdetails, movie where purchase.member_id = '" + memberId + "' AND purchase.order_id = orderdetails.order_id AND orderdetails.movie_id = movie.movie_id)";
		String str2 = "select title from movie where now_showing = 1 and rating > (select avg(rating) from movie where now_showing = 1) and rownum <= 5 order by rating desc";
		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			int count = 0;
			System.out.println("Below are some movies recommended for you");
			System.out.println("----------------------------------------------\n");

			while(rs1.next())

			{
				String in = rs1.getString(1);
				System.out.println(in);
				count++;
			}
			if(count == 0)
			{

				//DBConnections.closeDbConnection();
				DBConnections.query = str2;
				rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
				while(rs2.next())
				{
					String in2 = rs2.getString(1);
					System.out.println(in2);

				}

			}

			System.out.println("\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}


	public static void viewProfile(int memberId)
	{
		ResultSet rs;
		String str = "select u.name, u.phone, u.address, u.date_of_birth, u.email, u.gender, m.credit_points, m.member_points, m.status, m.role from userregistration u , membership m where u.member_id = m.member_id  and u.member_id =  " + memberId;
		DBConnections.query = str;
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {


			System.out.println("----------------------------------------------\n");

			while(rs.next())
			{
				String name = rs.getString(1);
				String phone = rs.getString(2);
				String address = rs.getString(3);
				String dob = rs.getString(4);
				String email = rs.getString(5);
				String gender = rs.getString(6);
				int credit_points = rs.getInt(7);
				int member_points = rs.getInt(8);
				String status = rs.getString(9);

				System.out.println("Name : " + name);
				System.out.println("Phone Number : " + phone);
				System.out.println("Address : " + address);
				System.out.println("Date of Birth : " + dob.substring(0, 11));
				System.out.println("E-mail : " + email);
				System.out.println("Gender : " + gender);
				System.out.println("Available credit Points : " + credit_points);
				System.out.println("Total points : " + member_points);
				System.out.println("Membership status : " + status);


			}

			System.out.println("----------------------------------------------\n");

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
	}


	public static void updateProfile(int memberId, int option, String value)
	{

		String str1 = "update userregistration set phone = '" + value +"' where member_id = " + memberId;
		String str2 = "update userregistration set address = '" + value +"' where member_id = " + memberId;

		if(option == 1)
			DBConnections.query = str1;
		if(option ==2)
			DBConnections.query = str2;


		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);

		DBConnections.closeDbConnection();

		if(ret == 1)
		{
			System.out.println("\nProfile updated\n");
			System.out.println("\nYour Account details :");
			viewProfile(memberId);
		}


	}

	public static void createNewMovieReviewThread(int memberId, String movie, String review)
	{
		ResultSet rs;
		int movie_id = 0;
		String str1 = "select movie_id from movie where title = '" + movie +"'";
		String str2 = "insert into review values (seq_review.nextVal, " + memberId + ", 0, '" + review + "', 0)"; 



		DBConnections.query = str1;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);


		//find movie_id
		try {
			while(rs.next())
			{
				movie_id = rs.getInt(1);			
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}


		//update review table
		DBConnections.query = str2;		
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

		//update moviereview table
		String str3 = "insert into moviereview (select " + movie_id + ", max(review_id) from review)";
		DBConnections.query = str3;		
		DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();

		//find status, credit_points and member_points
		String memberShipStatus = null;
		int credit_points =0;
		int member_points = 0;
		String str4 = "select status, credit_points, member_points from membership where member_id = " + memberId;
		DBConnections.query = str4;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			while(rs.next())
			{
				memberShipStatus = rs.getString(1);	
				credit_points = rs.getInt(2);
				member_points = rs.getInt(3);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally
		{
			DBConnections.closeDbConnection();
		}


		//give points on posting a review
		int points = 0;

		if(memberShipStatus.equals("Silver"))
			points = 5;
		if(memberShipStatus.equals("Gold"))
			points = 10;
		if(memberShipStatus.equals("Platinum"))
			points = 20;

		String str5 = "update membership set credit_points = " + (credit_points + points) + ", member_points = " + (member_points + points) + " where member_id = " + memberId;

		DBConnections.query = str5;		
		ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

		System.out.println("Review thread created\n");
	}

	public static void showMovieReviews(String movie)
	{
		ResultSet rs1, rs2, rs3;
		int movie_id = 0;

		String str1 = "select movie_id from movie where title = '" + movie +"'";

		DBConnections.query = str1;	
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);


		//find movie_id
		try {
			while(rs1.next())
			{
				movie_id = rs1.getInt(1);			
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

		//show reviews		
		String str2 = "select r.review_id , u.name , r.like_count , r.review_content from review r, moviereview m ,userregistration u where r.review_id = m.review_id and r.MEMBER_ID = u.MEMBER_ID and m.movie_id = " + movie_id;

		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			System.out.println();
			if(rs2.isBeforeFirst() )
			{
				System.out.println("Below are the reviews posted on this movie by our members: \n");	
				System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{

					int reviewId = rs2.getInt(1);
					System.out.println("Review Number : " +reviewId);
					System.out.println(rs2.getString(4) + "\tBy [" + rs2.getString(2) + "]");
					System.out.println("Likes : " + rs2.getInt(3));


					String str3 = "select u.name, r.reply_content from reviewreply r, userregistration u where r.member_id = u.member_id and r.review_id = " + reviewId;
					DBConnections.query = str3;	
					rs3 = DBConnections.openDbConnectionForSelect(DBConnections.query);
					if(rs3.isBeforeFirst() )
					{
						System.out.println("\t\tReplies:");
						while(rs3.next())
						{
							System.out.println("\t\t" + rs3.getString(2) + "\tBy [" + rs3.getString(1) + "]");

						}

					}

					System.out.println("---------------------------------------------------------------------------------------");
				} 
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}

	public static boolean verifyReviewID(int review_id)
	{
		ResultSet rs;
		String str1 = "select review_id from review where review_id = " + review_id;
		DBConnections.query = str1;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			if(rs.isBeforeFirst())
				return true;
			else return false;

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return false;
	}

	public static void insertMovieReviewsReply(int memberId, int review_id, String review)
	{

		ResultSet rs;
		int movie_id = 0;

		String str2 = "insert into reviewreply values (" + review_id + ", " + memberId + ", '" + review + "', 0)"; 


		//update reviewreply table
		DBConnections.query = str2;		
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();

		if(ret ==1)
			System.out.println("Your reply has been updated!!!");

		//find status, credit_points and member_points
		String memberShipStatus = null;
		int credit_points =0;
		int member_points = 0;
		String str4 = "select status, credit_points, member_points from membership where member_id = " + memberId;
		DBConnections.query = str4;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			while(rs.next())
			{
				memberShipStatus = rs.getString(1);	
				credit_points = rs.getInt(2);
				member_points = rs.getInt(3);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally
		{
			DBConnections.closeDbConnection();
		}

		//give points on posting a review

		int points = 0;
		if(memberShipStatus.equals("Silver"))
			points = 5;
		if(memberShipStatus.equals("Gold"))
			points = 10;
		if(memberShipStatus.equals("Platinum"))
			points = 20;
		String str5 = "update membership set credit_points = " + (credit_points + points) + ", member_points = " + (member_points + points) + " where member_id = " + memberId;

		DBConnections.query = str5;		
		ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

	}


	public static void likeComment(int review_id)
	{

		ResultSet rs;
		int like_count = 0;
		String str1 = "select like_count from review where review_id = " + review_id;
		DBConnections.query = str1;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			while(rs.next())
			{
				like_count = rs.getInt(1);	

			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally
		{
			DBConnections.closeDbConnection();
		}

		String str = "update review set like_count = " + (like_count + 1) + " where review_id = " + review_id;

		DBConnections.query = str;		
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	

		if(ret == 1)
			System.out.println("Thank you for liking the comment.");
		DBConnections.closeDbConnection();

	}

	public static void showTheatreReviews(String theatre)
	{
		ResultSet rs1, rs2, rs3;
		int theatre_id = 0;

		String str1 = "select theatre_id from theatre where name = '" + theatre +"'";

		DBConnections.query = str1;	
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);


		//find movie_id
		try {
			while(rs1.next())
			{
				theatre_id = rs1.getInt(1);			
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

		//show reviews		
		String str2 = "select r.review_id , u.name , r.like_count , r.review_content from review r, theatrereview m ,userregistration u where r.review_id = m.review_id and r.MEMBER_ID = u.MEMBER_ID and m.theatre_id = " + theatre_id;

		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			System.out.println();
			if(rs2.isBeforeFirst() )
			{
				System.out.println("Below are the reviews posted on this theatre by our members: \n");	
				System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{

					int reviewId = rs2.getInt(1);
					System.out.println("Review Number : " +reviewId);
					System.out.println(rs2.getString(4) + "\tBy [" + rs2.getString(2) + "]");
					System.out.println("Likes : " + rs2.getInt(3));


					String str3 = "select u.name, r.reply_content from reviewreply r, userregistration u where r.member_id = u.member_id and r.review_id = " + reviewId;
					DBConnections.query = str3;	
					rs3 = DBConnections.openDbConnectionForSelect(DBConnections.query);
					if(rs3.isBeforeFirst() )
					{
						System.out.println("\t\tReplies:");
						while(rs3.next())
						{
							System.out.println("\t\t" + rs3.getString(2) + "\tBy [" + rs3.getString(1) + "]");

						}

					}

					System.out.println("---------------------------------------------------------------------------------------");
				} 
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}



	public static void createNewTheatreReviewThread(int memberId, String theatre, String review)
	{
		ResultSet rs;
		int theatre_id = 0;
		String str1 = "select theatre_id from theatre where name = '" + theatre +"'";
		String str2 = "insert into review values (seq_review.nextVal, " + memberId + ", 0, '" + review + "', 0)"; 


		DBConnections.query = str1;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);


		//find movie_id
		try {
			while(rs.next())
			{
				theatre_id = rs.getInt(1);			
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}


		//update review table
		DBConnections.query = str2;		
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

		//update moviereview table
		String str3 = "insert into theatrereview (select " + theatre_id + ", max(review_id) from review)";
		DBConnections.query = str3;		
		DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();

		//find status, credit_points and member_points
		String memberShipStatus = null;
		int credit_points =0;
		int member_points = 0;
		String str4 = "select status, credit_points, member_points from membership where member_id = " + memberId;
		DBConnections.query = str4;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			while(rs.next())
			{
				memberShipStatus = rs.getString(1);	
				credit_points = rs.getInt(2);
				member_points = rs.getInt(3);
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally
		{
			DBConnections.closeDbConnection();
		}

		//give points on posting a review
		int points = 0;

		if(memberShipStatus.equals("Silver"))
			points = 5;
		if(memberShipStatus.equals("Gold"))
			points = 10;
		if(memberShipStatus.equals("Platinum"))
			points = 20;

		String str5 = "update membership set credit_points = " + (credit_points + points) + ", member_points = " + (member_points + points) + " where member_id = " + memberId;

		DBConnections.query = str5;		
		ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

		System.out.println("Review thread created\n");
	}


	public static void executeQuery6() {
		// TODO Auto-generated method stub
		ResultSet rs;
		DBConnections.query="select name from theatre where theatre_id "
				+ "in(select theatre_id from screen where screen_id "
				+ "in(select screen_id from schedule where schedule_id "
				+ "in(select schedule_id from (select schedule_id, sum(quantity) as s from orderdetails  "
				+ "group by schedule_id  order by s desc) where rownum=1)))";


		rs=DBConnections.openDbConnectionForSelect(DBConnections.query);
		System.out.println("The following theatre(s) shows maximum number of movies");
		System.out.println("----------------------------------------------------------");
		try {
			while(rs.next())
			{
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DBConnections.closeDbConnection();
		}
	}


	public static void executeQuery7() {
		// TODO Auto-generated method stub
		ResultSet rs;
		DBConnections.query="select name from theatre where theatre_id in"
				+ "(select t.THEATRE_ID from (select count(distinct movie_id) c, THEATRE_ID from screen group by theatre_id) t where "
				+ "t.c = (select max(c) from (select count(distinct movie_id) c, THEATRE_ID from screen group by theatre_id) t2))";

		rs=DBConnections.openDbConnectionForSelect(DBConnections.query);
		System.out.println("The following theatre(s) has maximum number of online sales");
		System.out.println("----------------------------------------------------------");
		try {
			while(rs.next())
			{
				System.out.println(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DBConnections.closeDbConnection();
		}

	}


	public static void executeQuery8(String tname) {
		// TODO Auto-generated method stub
		ResultSet rs;



		DBConnections.query="Select s1.name, s2.description, s2.staff_role, s3.start_time, s3.END_TIME from staffdetails s1 "
				+ "inner join staff_description s2 on s1.description_id=s2.description_id "
				+ "inner join staffschedule s3 on s1.STAFF_ID=s3.STAFF_ID where s3.staff_id in "
				+ "(select staff_id from staffschedule where to_char(day, 'DAY') like '%MONDAY%') and s1.theatre_id =(Select theatre_id from theatre where name='"+tname+"') "
				+ "and s3.day in (select day from staffschedule where to_char(day, 'DAY') like '%MONDAY%')";

		rs=DBConnections.openDbConnectionForSelect(DBConnections.query);
		System.out.println("The following are the employees and their schedule");
		System.out.println("----------------------------------------------------------");
		try {
			while(rs.next())
			{
				System.out.println("Name: "+rs.getString(1));
				System.out.println("Description: "+rs.getString(2));
				System.out.println("Role: "+rs.getString(3));
				System.out.println("Start Time: "+rs.getString(4));
				System.out.println("End Time: "+rs.getString(5));
				System.out.println();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			DBConnections.closeDbConnection();
		}


	}


	public static void viewListOfTables()
	{
		ResultSet rs1;
		String str1 = "select table_name from user_tables";

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {

			System.out.println("Below is the list of tables");
			System.out.println("----------------------------------------------\n");

			while(rs1.next())
			{
				String in = rs1.getString(1);
				System.out.println(in);

			}

			System.out.println("----------------------------------------------\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}

	public static void viewTableDetails(String table_name)
	{
		ResultSet rs1;
		String str1 = "select * from " + table_name;

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {

			System.out.println("Content of table " + table_name);
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");

			ResultSetMetaData rsmd = rs1.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs1.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					if(table_name.toUpperCase().equals("CREDIT_CARD_DETAILS"))
					{
						String columnValue = rs1.getObject(i).toString();
						if(i==2)
						{
							String lastFourDigits = columnValue.substring(12, columnValue.length());
							columnValue = "************" + lastFourDigits;
							System.out.print(rsmd.getColumnName(i) + " :- " + columnValue + "\n"); 
						}

						else
							System.out.print(rsmd.getColumnName(i) + " :- " + columnValue + "\n");   
					}else
					{
						String columnValue = rs1.getObject(i).toString();
						System.out.print(rsmd.getColumnName(i) + " :- " + columnValue + "\n");
					}
				}
				System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");
			}	


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}

	public static boolean validateTableName(String table_name)
	{
		ResultSet rs1;
		String str1 = "select table_name from user_tables";
		ArrayList<String> table_list = new ArrayList<String>();
		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {

			while(rs1.next())
			{
				table_list.add(rs1.getString(1));

			}

			System.out.println("----------------------------------------------\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

		if(table_list.contains(table_name))
			return true;
		else return false;

	}


	public static void viewRegisteredUserDetails()
	{
		ResultSet rs1;
		String str1 = "select member_id, name, email  from userregistration";

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {

			System.out.println("List of registered users");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");
			System.out.printf("%10s%20s%30s%n", "Member Id", "Name", "E-mail address");
			while (rs1.next())
			{
				System.out.printf("%10d", rs1.getInt(1));
				System.out.printf("%20s", rs1.getString(2));
				System.out.printf("%30s%n", rs1.getString(3));


			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}

	public static void viewDetailedRegisteredUserDetails(String member_id)
	{
		ResultSet rs1;
		String str1 = "select u.name, u.phone, u.address, u. date_of_birth, u.email, u.gender, m.credit_points, m.member_points, m.status, m.role from membership m, userregistration u where m.MEMBER_ID = u.MEMBER_ID and m.member_id = " + member_id;

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {


			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");

			while (rs1.next())
			{
				System.out.println("Name : " + rs1.getString(1));
				System.out.println("Phone : " + rs1.getString(2));
				System.out.println("Address : " + rs1.getString(3));
				System.out.println("Date of Birth : " + rs1.getString(4).substring(0, 10));
				System.out.println("E-mail : " + rs1.getString(5));
				System.out.println("Gender : " + rs1.getString(6));
				System.out.println("Available Credit points : " + rs1.getInt(7));
				System.out.println("Total Points : " + rs1.getString(8));
				System.out.println("Membership Status : " + rs1.getString(9));
				System.out.println("User Role : " + rs1.getString(10));



			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}


	public static void listPolicy()
	{
		ResultSet rs1;
		String str1 = "select *  from policies";

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {

			System.out.println("Current policy");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");
			System.out.printf("%20s%20s%20s%40s%n", "Membership Status", "Review Points", "Purchase Points", "Threshold for memberships");
			while (rs1.next())
			{
				System.out.printf("%20s", rs1.getString(1));
				System.out.printf("%20.2f", rs1.getFloat(2));
				System.out.printf("%20.2f", rs1.getFloat(3));
				System.out.printf("%20.2f%n", rs1.getFloat(4));	    		    

			}
			System.out.println("-----------------------------------------------------------------------------------------------------------------------\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}


	public static boolean verifyMembershipStatus(String membership_status)
	{
		ResultSet rs;
		String str1 = "select membership_status from policies where membership_status = '" + membership_status + "'";
		DBConnections.query = str1;	
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			if(rs.isBeforeFirst())
				return true;
			else return false;

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return false;
	}


	public static void updatePolicy(String membership_status, String policy_type, float value)
	{
		ResultSet rs;

		String str = "update policies set " +  policy_type + " = '" + value + "' where membership_status = '" + membership_status + "'";

		DBConnections.query = str;		
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);	
		DBConnections.closeDbConnection();	

		if(ret == 1)
		{
			System.out.println("Policy update\n");
			DBQueries.listPolicy();
		}

	}



	public static void viewStaffSchedule(String email_id)
	{
		ResultSet rs1, rs2;
		String str1 = "select ss.day, ss.start_time, ss.end_time from staffschedule ss, STAFFDETAILS sd where ss.staff_id = sd.staff_id and sd.email = '" + email_id + "'"; 

		DBConnections.query = str1;
		rs1 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			int count = 0;
			System.out.println("\nBelow is your schedule");
			System.out.println("----------------------------------------------\n");
			System.out.printf("%15s%15s%15s%n", "Day", "Start Time", "End Time");
			while(rs1.next())
			{
				System.out.printf("%15s", rs1.getObject(1).toString());
				System.out.printf("%15s", rs1.getString(2));
				System.out.printf("%15s%n", rs1.getString(3));
				count++;
			}
			if(count == 0)
			{			
				System.out.println("Your schedule has not been set");
			}

			System.out.println("\n");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}
	
	
	
	
	public static void executeQuery2()
	{
		ResultSet rs2, rs3;
				
		String str2 = "select r.review_id , u.name , r.like_count , r.review_content from review r,userregistration u where r.MEMBER_ID = u.MEMBER_ID";

		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			System.out.println();
			if(rs2.isBeforeFirst() )
			{
				//System.out.println("Below are the reviews posted on this movie by our members: \n");	
				//System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{

					int reviewId = rs2.getInt(1);
					System.out.println("Review Number : " +reviewId);
					System.out.println(rs2.getString(4) + "\tBy [" + rs2.getString(2) + "]");
					System.out.println("Likes : " + rs2.getInt(3));


					String str3 = "select u.name, r.reply_content from reviewreply r, userregistration u where r.member_id = u.member_id and r.review_id = " + reviewId + " and rownum<=3";
					DBConnections.query = str3;	
					rs3 = DBConnections.openDbConnectionForSelect(DBConnections.query);
					if(rs3.isBeforeFirst() )
					{
						System.out.println("\t\tReplies:");
						while(rs3.next())
						{
							System.out.println("\t\t" + rs3.getString(2) + "\tBy [" + rs3.getString(1) + "]");

						}

					}

					System.out.println("---------------------------------------------------------------------------------------");
				} 
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}
	
	public static void executeQuery1()
	{
		ResultSet rs1, rs2, rs3;
				
		String str2 = "select r.review_id , u.name , r.like_count , r.review_content from review r,userregistration u where r.MEMBER_ID = u.MEMBER_ID";

		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			System.out.println();
			if(rs2.isBeforeFirst() )
			{
				System.out.println("Below are all the reviews posted by our members: \n");	
				//System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{

					int reviewId = rs2.getInt(1);
					System.out.println("Review Number : " +reviewId);
					System.out.println(rs2.getString(4) + "\tBy [" + rs2.getString(2) + "]");
					System.out.println("Likes : " + rs2.getInt(3));
					System.out.println("---------------------------------------------------------------------------------------");
				} 
			}

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		
		
		try {
			System.out.println("Please enter the Review Number to watch three latest comments : ");
			Scanner in = new Scanner(System.in);
			int review_id = Integer.parseInt(in.nextLine());
			String str3 = "select u.name, r.reply_content from reviewreply r, userregistration u where r.member_id = u.member_id and r.review_id = " + review_id + " and rownum<=3";
			DBConnections.query = str3;	
			rs3 = DBConnections.openDbConnectionForSelect(DBConnections.query);
			if(rs3.isBeforeFirst() )
			{
				System.out.println("\t\tReplies:");
				while(rs3.next())
				{
					System.out.println("\t\t" + rs3.getString(2) + "\tBy [" + rs3.getString(1) + "]");

				}

			}
			else 
				System.out.println("No comments has been posted on this review thread\n");

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}

	}
	
	
	public static void executeQuery3()
	{
		ResultSet rs2;
				
		String str2 = "select r.review_id, u.name , r.like_count , r.review_content from review r , userregistration u where r.MEMBER_ID = u.MEMBER_ID and (r.review_id not in (select review_id from reviewreply) or r.review_id in (select review_id from reviewreply group by review_id having count(reply_content) = (select min(count(reply_content)) from reviewreply group by review_id))) and rownum <=1 order by r.review_id desc";
		
		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
				System.out.println("\nBelow is the least popular review thread : ");	
				System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{
	
					int reviewId = rs2.getInt(1);
					System.out.println("Review Number : " +reviewId);
					System.out.println(rs2.getString(4) + "\tBy [" + rs2.getString(2) + "]");
					System.out.println("Likes : " + rs2.getInt(3));
					System.out.println("---------------------------------------------------------------------------------------");
				} 
			

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		

	}
	
	public static void executeQuery5()
	{
		ResultSet rs2;
				
		String str2 = "select u.name, sub.c from (select member_id, count(member_id)c from (select member_id from review union all select member_id from reviewreply) group by member_id having count(member_id) = (select max(count(member_id)) from (select member_id from review union all select member_id from reviewreply) group by member_id))sub, userregistration u where u.MEMBER_ID = sub.member_id" ;
		
		DBConnections.query = str2;	
		rs2 = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
				System.out.println("\nBelow is the registered guest who has contributed most comments : ");	
				System.out.println("---------------------------------------------------------------------------------------");
				while(rs2.next())
				{
	
					
					System.out.println("Name : " + rs2.getString(1));
					System.out.println("Number of comments : " + rs2.getInt(2));
					System.out.println("---------------------------------------------------------------------------------------");
				} 
			

		} catch (SQLException e) {

			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		

	}
	
	public static void updateTable(String query)
	{
				
		ResultSet rs1;
		DBConnections.query = query;
		int ret = DBConnections.openDbConnectionForUpdate(DBConnections.query);

		DBConnections.closeDbConnection();

		if(ret == 1)
		{
			System.out.println("Query executed Successfully\n");
		}

	}
}

