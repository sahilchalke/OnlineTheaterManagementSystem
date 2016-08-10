package com.CS425.Db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.CS425.bean.OrderDetails;
import com.CS425.bean.StaffDetails;
import com.CS425.bean.UserCCDetails;
import com.CS425.bean.UserDetails;

public class FetchData {

	ResultSet rs;
	String query;

	public boolean validateUserLogin(String email, String pass) {

		query = "select count(*) from MemberLogin where email = '" + email + "' and password = '" + pass + "'";
		boolean isValidUser = false;
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next())
				if(rs.getInt(1) != 0)
					isValidUser = true;
			//rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return isValidUser;
	}

	public UserDetails getUserDetails(String email) {

		UserDetails userD = null;
		query = "select * from USERREGISTRATION u inner join MEMBERSHIP m on u.member_id = m.member_id where u.email = '" + email + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				userD = new UserDetails(rs.getString(2), rs.getString(3), rs.getString(4), rs.getDate(5).toString(), rs.getString(6), 
						rs.getString(7), rs.getInt(9), rs.getInt(10), rs.getString(11), rs.getString(12));
				userD.setMemberId(rs.getInt(1));
				//rs.close();	
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return userD;
	}

	public UserCCDetails getUserCCDetails(int member_id) {
		
		UserCCDetails userCC = null;
		query = "select * from Credit_Card_Details where member_id = '" + member_id + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				userCC = new UserCCDetails(rs.getString(3), rs.getString(2), rs.getString(4), rs.getString(5));
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBConnections.closeDbConnection();
		}
		return userCC;
	}

	public boolean validateMovie(String movie) {

		query = "select count(*) from Movie where title = '" + movie + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				if(rs.getInt(1) != 0){
					DBConnections.closeDbConnection();
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnections.closeDbConnection();
		}
		return false;
	}

	public boolean validateTheatre(String theatre) {

		query = "select count(*) from Theatre where name = '" + theatre + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				if(rs.getInt(1) != 0){
					DBConnections.closeDbConnection();
					return true;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnections.closeDbConnection();
		}
		return false;
	}

	public boolean insertUserDetails(UserDetails user, UserCCDetails userCC) {

		int memberId = 0;
		query = "insert into UserRegistration values (seq_member.nextval, '" +user.getUserName() + "', '" + user.getPhone() + "', '" +
				user.getAddress() + "', to_date('" + user.getDateOfBirth() + "','MM/DD/YYYY'), '" + user.getEmailId() + "', '" + user.getGender() + "')";
		int result1 = DBConnections.openDbConnectionForUpdate(query);
		if(result1 == 0){
			DBConnections.closeDbConnection();
			return false;
		}
		DBConnections.closeDbConnection();
		
		query = "select member_id from UserRegistration where email = '" + user.getEmailId() + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				memberId = rs.getInt("member_id");
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnections.closeDbConnection();
		}
		
		query = "insert into membership values (" + memberId + ", '" + user.getCreditPoints() + "', '" + user.getMemberShipPoints()
				+ "', '" + user.getStatus() + "', '" + user.getRole() + "')";
		int result2 = DBConnections.openDbConnectionForUpdate(query);
		if(result2 == 0){
			DBConnections.closeDbConnection();
			return false;
		}
		DBConnections.closeDbConnection();
		
		query = "insert into Credit_Card_Details values (" + memberId + ", '" + userCC.getCardNumber() + "', '" + userCC.getCardType()
				+ "', '" + userCC.getExpiry() + "', '" + userCC.getNameOnCard() + "')";
		result2 = DBConnections.openDbConnectionForUpdate(query);
		if(result2 == 0){
			DBConnections.closeDbConnection();
			return false;
		}
		DBConnections.closeDbConnection();
		return true;
	}

	public boolean insertUserLoginDetails(String email, String password) {

		query = "insert into memberlogin values ('" + email + "', '" + password + "')";
		int result = DBConnections.openDbConnectionForUpdate(query);
		if(result != 0){
			DBConnections.closeDbConnection();
			return true;
		}
		DBConnections.closeDbConnection();
		return false;
	}

	public void appHomeMovieReco() {
		
		query = "select title from movie where now_showing = 1 and rating > (select avg(rating) from movie where now_showing = 1) and rownum <= 5 order by rating desc";
		rs = DBConnections.openDbConnectionForSelect(query);
		
		try {
			while(rs.next())
				System.out.println(rs.getString("title"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnections.closeDbConnection();
		}
	}

	public ArrayList<OrderDetails> getUserOrderHistory(int memberId) {
		
		ArrayList<OrderDetails> orderHistory = new ArrayList<OrderDetails>();
		OrderDetails order;
		query = "select o.order_id, m.title, t.name, t.location, s.day, s.schedule_time, o.quantity, o.timestamp from " +
				"Purchase p inner join OrderDetails o on o.order_id = p.order_id inner join " +
				"Schedule s on o.schedule_id = s.schedule_id inner join " +
				"Screen sc on s.screen_id = sc.screen_id inner join " +
				"Theatre t on sc.theatre_id = t.theatre_id inner join " +
				"Movie m on o.movie_id = m.movie_id where p.member_id = " + memberId;
		
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				order = new OrderDetails(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
										 rs.getString(5), rs.getString(6), null, 0, rs.getInt(7), 0, rs.getString(8));
				orderHistory.add(order);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBConnections.closeDbConnection();
		}
		return orderHistory;	
	}

	public int getOrderId(String CCNumber, int scheduleId) {
		
		int order_id = 0;	
		query = "select order_id from OrderDetails where card_no = " + CCNumber + " and schedule_id = " + 
				scheduleId + " and rownum = 1 order by order_id desc";
		
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
		return order_id;
	}

	public String getAuthorityByEmail(String email) {
		
		String authority = null;
		query = "select m.role from USERREGISTRATION u inner join MEMBERSHIP m on m.member_id = u.member_id where u.email = '" + email + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				authority = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		return authority;
	}

	public String getStaffTypeByEmail(String email) {
		
		String staffType = null;
		query = "select staff_role from STAFF_DESCRIPTION where description_id in (select description_id from STAFFDETAILS where email = '" + email + "')";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				staffType = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		return staffType;
	}

	public void upgradeMembershipStatus(int memberId, String status) {
		
		if(status.equalsIgnoreCase("gold"))
			query = "update MEMBERSHIP set status = 'Gold' where member_id = " + memberId;
			
		if(status.equalsIgnoreCase("platinum"))
			query = "update MEMBERSHIP set status = 'Platinum' where member_id = " + memberId;
		
		DBConnections.openDbConnectionForUpdate(query);
		DBConnections.closeDbConnection();
	}

	public StaffDetails getStaffDetails(String email) {
		
		StaffDetails sd = new StaffDetails();
		query = "select * from STAFFDETAILS where email = '" + email + "'";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				sd.setName(rs.getString(2));
				sd.setStaffId(rs.getInt(1));
				sd.setEmail(rs.getString(8));
				sd.setTheatreId(rs.getInt(9));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sd;
	}

	public double getCreditPurchasePolicy(String status) {
		
		double points = 0;
		rs = DBConnections.openDbConnectionForSelect("select purchase_points from POLICIES where MEMBERSHIP_status = '" + status + "'");
		try {
			while(rs.next()){
				points = rs.getDouble(1);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return points;
	}

	public int getMembershipThreshold(String status) {
		
		int threshold = 0;
		rs = DBConnections.openDbConnectionForSelect("select threshold from POLICIES where MEMBERSHIP_status = '" + status + "'");
		try {
			while(rs.next()){
				threshold = rs.getInt(1);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		return threshold;
	}

	public ArrayList<String> getManagers() {
		
		ArrayList<String> mList = new ArrayList<String>();
		query = "select s.staff_id, s.name, t.name from STAFFDETAILS s inner join THEATRE t on s.theatre_id = t.theatre_id where s.description_id = 1011";
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next())
				mList.add("Staff ID: " + rs.getInt(1) + "\nName: " + rs.getString(2) + "\nWorks At: " +  rs.getString(3));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		return mList;
	}

	public boolean setPriviledge(int staffId) {
		
		String name = "";
		query = "select * from priviledeges where staff_id = " + staffId;
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				DBConnections.closeDbConnection();
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		
		query = "select name from staffdetails where staff_id = " + staffId;
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next()){
				name = rs.getString(1);
				break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		
		query = "insert into priviledeges values(" + staffId + " ,'"+ name +"')";
		DBConnections.openDbConnectionForUpdate(query);
		DBConnections.closeDbConnection();
		return true;
	}

	public boolean removePriviledge(int staffId) {
		
		int count = 0;
		query = "select count(*) from priviledeges where staff_id = " + staffId;
		rs = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		if(count > 0){
			query = "delete from priviledeges where staff_id = " + staffId;
			DBConnections.openDbConnectionForUpdate(query);
			DBConnections.closeDbConnection();
			return true;
		}
		else	
			return false;
	}

	public boolean isManagerPriviledged(int staffId) {
		
		int count = 0;
		query = "select count(*) from priviledeges where staff_id = " + staffId;
		rs  = DBConnections.openDbConnectionForSelect(query);
		try {
			while(rs.next())
				count = rs.getInt(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		
		if(count > 0)
			return true;
		return false;
	}
}