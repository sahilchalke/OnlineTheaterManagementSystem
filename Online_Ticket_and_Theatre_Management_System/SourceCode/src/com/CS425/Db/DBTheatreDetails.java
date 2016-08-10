package com.CS425.Db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.CS425.bean.GuestDetails;
import com.CS425.bean.MovieSchedule;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class DBTheatreDetails {

	static ResultSet rs;
	static int result;


	public static String getTheatreDetails(String theatre){
		String address = "";
		DBConnections.query = "select * from theatre where name='"+theatre+"'";
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			while(rs.next())
			{
				address = rs.getString(3);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return address;
	}

	public static ArrayList<MovieSchedule> getTheatreSchedule(String theatre)
	{
		MovieSchedule mSchedule;
		ArrayList<MovieSchedule> mScheduleList = new ArrayList<MovieSchedule>();
		DBConnections.query = "select m1.title,m1.year, m1.genre, m1.description, s1.screen_number, s2.schedule_time, s2.availability, s2.price, s2.day, s2.schedule_id "
				+ "from schedule s2 inner join screen s1 on s1.screen_id=s2.screen_id "
				+ "inner join movie m1 on m1.movie_id=s1.movie_id where s2.screen_id "
				+ "IN (Select screen_id from screen where theatre_id "
				+ "IN (Select theatre_id from Theatre where name='"+theatre+"'))";
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);
		System.out.println("Following movies are being screened: \n");
		try {
			while(rs.next())
			{
				System.out.println("Movie Name: "+rs.getString(1));
				System.out.println("Screen Number: "+rs.getInt(5));
				System.out.println("Schedule Time: "+rs.getString(6));
				System.out.println("Availability: "+rs.getInt(7));
				System.out.println("Price: "+rs.getInt(8));
				System.out.println("Day: "+rs.getString(9) + "\n\n");
				mSchedule = new MovieSchedule(rs.getString(1), rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getInt(8), rs.getString(9), rs.getInt(10));
				mScheduleList.add(mSchedule);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return mScheduleList;
	}

	public static boolean purchaseTicketViaCC(MovieSchedule selectedMovie, int quantity, UserDetails userD, 
			UserCCDetails userCC, int addCredit) {

		int movieId = 0;
		int order_id = 0;

		String query = "select movie_id from Movie where title= '" + selectedMovie.getMovieName() + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				movieId = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		
		query = "insert into OrderDetails values (seq_order.nextval, " + quantity + ", '" + userCC.getCardNumber() + "', " + 
				selectedMovie.getScheduleId() + ", " + movieId + ", sysdate)";
		int result = DBConnections.openDbConnectionForUpdate(query);
		if(result == 0)
			return false;
		DBConnections.closeDbConnection();
		
		query = "select order_id from OrderDetails where card_no = " + userCC.getCardNumber() + " and schedule_id = " + 
				selectedMovie.getScheduleId() + " and rownum = 1 order by order_id desc";

		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while (rs.next()){
				order_id = rs.getInt(1);
				break;
			}// while
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    DBConnections.closeDbConnection();
		
	    query = "insert into Purchase values (" + userD.getMemberId() + ", " + order_id + ")";
		result = DBConnections.openDbConnectionForUpdate(query);
		DBConnections.closeDbConnection();

		query = "update Schedule set availability = availability - " + quantity + " where schedule_id = " + selectedMovie.getScheduleId();
		result = DBConnections.openDbConnectionForUpdate(query);
		DBConnections.closeDbConnection();
		
		query = "update Membership set credit_points = credit_points + " + addCredit + ", member_points = member_points + " + (addCredit * 100) + " where member_id = " + userD.getMemberId();
		result = DBConnections.openDbConnectionForUpdate(query);
		DBConnections.closeDbConnection();
		return true;
	}// function

	public static boolean purchaseTicketViaCreditPoints( MovieSchedule selectedMovie, int quantity, UserDetails userD, UserCCDetails userCC) {

		int movieId = 0;
		int order_id = 0;

		String query = "select movie_id from Movie where title= '" + selectedMovie.getMovieName() + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				movieId = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		query = "insert into OrderDetails values (seq_order.nextval, " + quantity + ", '" + userCC.getCardNumber() + "', " + 
				selectedMovie.getScheduleId() + ", " + movieId + ", sysdate)";
		int result = DBConnections.openDbConnectionForUpdate(query);
		if(result == 0)
			return false;
		else{
			query = "select order_id from OrderDetails where card_no = " + userCC.getCardNumber() + " and schedule_id = " + 
					selectedMovie.getScheduleId() + "order by order_id";
			rs = DBConnections.openDbConnectionForSelect(query);
			
			try {
				while (rs.next()){
					order_id = rs.getInt(1);
					break;
				}// while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBConnections.closeDbConnection();
			}
			query = "insert into Purchase values (" + userD.getMemberId() + ", " + order_id + ")";
			result = DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();

			query = "update Schedule set availability = availability - " + quantity + " where schedule_id = " + selectedMovie.getScheduleId();
			DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();

			int deductCredit = selectedMovie.getPrice() * quantity;
			query = "update membership set credit_points = credit_points - " + deductCredit + ", membership_points = membership_points + " + deductCredit + " where member_id = " + userD.getMemberId();
			DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();
		}// else
		return true;
	}

	public static boolean purchaseTicketViaCC(GuestDetails gDetails, MovieSchedule selectedMovie, int quantity) {
			
		int movieId = 0;
		int order_id = 0;

		String query = "select movie_id from Movie where title= '" + selectedMovie.getMovieName() + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				movieId = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		query = "insert into OrderDetails values (seq_order.nextval, " + quantity + ", '" + gDetails.getCardNumber() + "', " + 
				selectedMovie.getScheduleId() + ", " + movieId + ", sysdate)";
		int result = DBConnections.openDbConnectionForUpdate(query);
		if(result == 0)
			return false;
		else{
			query = "select order_id from OrderDetails where card_no = " + gDetails.getCardNumber() + " and schedule_id = " + 
					selectedMovie.getScheduleId() + "order by order_id";
			rs = DBConnections.openDbConnectionForSelect(query);
			
			try {
				while (rs.next()){
					order_id = rs.getInt(1);
					break;
				}// while
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				DBConnections.closeDbConnection();
			}
			query = "insert into GuestOrder values (" + order_id + ", '" + gDetails.getEmail() + "', '" + 
			          gDetails.getPhone() + "')";
			result = DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();

			query = "update Schedule set availability = availability - " + quantity + " where schedule_id = " + selectedMovie.getScheduleId();
			DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();
		}// else
		return true;
	}
}