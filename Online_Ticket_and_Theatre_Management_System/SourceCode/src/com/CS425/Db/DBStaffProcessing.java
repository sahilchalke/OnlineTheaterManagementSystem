package com.CS425.Db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.CS425.bean.StaffDetails;

public class DBStaffProcessing {

	static ResultSet rs;

	public ArrayList<String> fetchTheatreList() {
		ArrayList<String> allTheaterList = new ArrayList<String>();

		DBConnections.query = "select theatre_id, name from THEATRE";
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);

		try {
			while(rs.next())
				allTheaterList.add(rs.getInt(1) + ": " + rs.getString(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		return allTheaterList;
	}

	public boolean insertStaffToDb(StaffDetails staffD) {

		int success1, success2;
		DBConnections.query = "insert into STAFFDETAILS values (seq_staff.nextval, '" + staffD.getName() + "', '" + staffD.getPhone() + "', '" +
				staffD.getSsn() + "', to_date('" + staffD.getDateOfJoining() + "','MM-DD-YYYY'), " + staffD.getDescId() + ", '" + 
				staffD.getAddress() + "', '" + staffD.getEmail() + "', " + staffD.getTheatreId() + ")";
		success1 = DBConnections.openDbConnectionForUpdate(DBConnections.query);
		DBConnections.closeDbConnection();

		DBConnections.query = "insert into memberlogin values ('" + staffD.getEmail() + "', '" + staffD.getEmail().substring(1, 4) + "')";
		success2 = DBConnections.openDbConnectionForUpdate(DBConnections.query);
		DBConnections.closeDbConnection();

		if(success1 == 0 && success2 == 0)
			return false;
		return true;
	}

	public ArrayList<String> getUnscheduledStaffList(int theaterId, int staffId) {

		ArrayList<String> staffList = new ArrayList<String>();
		if(staffId == 1000)
			DBConnections.query = "select s.staff_id, s.name, t.name from StaffDetails s inner join Theatre t on s.theatre_id = t.theatre_id where s.description_id not in (1010)";
		else	
			DBConnections.query = "select s.staff_id, s.name, t.name from StaffDetails s inner join Theatre t on s.theatre_id = t.theatre_id where s.description_id not in (1010) and s.theatre_id  = " + 
		                       theaterId + "and s.staff_id not in (" + staffId + ")";
		
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			while(rs.next())
				staffList.add(rs.getInt(1) + "\t\t" + rs.getString(2) + "\t\t" + rs.getString(3));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return staffList;
	}

	public ArrayList<String> getScheduledStaffList(int theaterId, int staffId) {

		ArrayList<String> staffList = new ArrayList<String>();
		if(staffId == 1000)
			DBConnections.query = "select s.staff_id, s.name, t.name from StaffDetails s inner join Theatre t on s.theatre_id = t.theatre_id where s.staff_id in (select staff_id from Staffschedule) and "
		            			+ "s.description_id not in (1010)";
		else
			DBConnections.query = "select s.staff_id, s.name, t.name from StaffDetails s inner join Theatre t on s.theatre_id = t.theatre_id where s.staff_id in (select staff_id from Staffschedule) and "
				            	+ "s.description_id not in (1010) and s.theatre_id = " + theaterId + "and s.staff_id not in (" + staffId + ")";
		
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);
		try {
			while(rs.next())
				staffList.add(rs.getInt(1) + "\t\t" + rs.getString(2));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return staffList;
	}

	public boolean addSchedule(int staff_id, String date, String startTime,
			String endTime) {
		String sql1;
		String date_check;
		String start_time_check;
		String end_time_check;
		int staffid_check;
		sql1 = "select staffdetails.staff_id, staffschedule.DAY, staffschedule.start_time, staffschedule.end_time from staffdetails, (select description_id, theatre_id from staffdetails where staff_id = '" + staff_id +"') sub, staffschedule where staffdetails.DESCRIPTION_ID = sub.description_id and staffdetails.THEATRE_ID = sub.theatre_id and staffdetails.staff_id = staffschedule.STAFF_ID ";

		rs = DBConnections.openDbConnectionForSelect(sql1);

		try {
			while(rs.next())
			{
				staffid_check = rs.getInt("staff_id");
				date_check = (rs.getDate("day").toString());
				start_time_check = rs.getString("start_time");
				end_time_check = rs.getString("end_time");

				if(staffid_check != staff_id)
				{

					if(date_check.equals(date))
					{
						if ((Double.parseDouble(startTime) >= Double.parseDouble(start_time_check)	
								&& Double.parseDouble(startTime) < Double.parseDouble(end_time_check)) ||
								(Double.parseDouble(endTime) > Double.parseDouble(start_time_check)	
										&& Double.parseDouble(endTime) <= Double.parseDouble(end_time_check)))
						{
							System.out.printf("Schedule of staff ID %d is clashing with the one you have entered%n",staffid_check);
							DBConnections.closeDbConnection();
							return false;
						}

					}
				}
				else if(staffid_check == staff_id && date_check.equals(date))
				{
					System.out.printf("Schedule of entered staff ID %d already exist for entered date. Please use update option to update the schedule%n",staff_id);
					DBConnections.closeDbConnection();
					return false;
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnections.closeDbConnection();
		String sql;
		sql ="INSERT INTO staffschedule VALUES ('" + staff_id + "', TO_DATE ('" + date + "', 'YYYY-MM-DD'), '"+ startTime + "','" + endTime +"')";
		
		int result = DBConnections.openDbConnectionForUpdate(sql);
		if(result == 1)
		{
			System.out.println("Schedule updated for staff ID:" + staff_id);
			DBConnections.closeDbConnection();
			return true;
		}

		else 
		{
			System.out.println("Schedule not updated");
			DBConnections.closeDbConnection();
			return false;
		}		
	}

	public boolean updateSchedule(int staff_id, String date, String startTime,
			String endTime) {
		
	      String sql1;
	      String sql2;
	      String date_check;
	      String start_time_check;
	      String end_time_check;
	      int staffid_check;
	      sql1 = "select staffdetails.staff_id, staffschedule.DAY, staffschedule.start_time, staffschedule.end_time from staffdetails, (select description_id, theatre_id from staffdetails where staff_id = '" + staff_id +"') sub, staffschedule where staffdetails.DESCRIPTION_ID = sub.description_id and staffdetails.THEATRE_ID = sub.theatre_id and staffdetails.staff_id = staffschedule.STAFF_ID ";
	      
	      sql2 ="UPDATE staffschedule SET start_time = '" + startTime + "', end_time = '" + endTime + "' WHERE staff_id = " + staff_id + " AND day = TO_DATE('" + date + "', 'YYYY-MM-DD')";
	      
	      rs = DBConnections.openDbConnectionForSelect(sql1);
	      
	      try {
			while(rs.next())
			  {
				  staffid_check = rs.getInt("staff_id");
				  date_check = (rs.getDate("day").toString());
				  start_time_check = rs.getString("start_time");
				  end_time_check = rs.getString("end_time");
				  
				  if(staffid_check != staff_id)
				  {
					  
					  if(date_check.equals(date))
					  {
						  if ((Double.parseDouble(startTime) >= Double.parseDouble(start_time_check)	
								  && Double.parseDouble(startTime) < Double.parseDouble(end_time_check)) ||
								  (Double.parseDouble(endTime) > Double.parseDouble(start_time_check)	
								  && Double.parseDouble(endTime) <= Double.parseDouble(end_time_check)))
						  {
							  System.out.printf("Schedule of staff ID %d is clashing with the one you have entered%n",staffid_check);
							  DBConnections.closeDbConnection();
							  return false;
						  }
							  
					  }
				  }
			  }
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      DBConnections.closeDbConnection();
	      int result = DBConnections.openDbConnectionForUpdate(sql2);
	      
	      //Clean-up environment
	      
	      if(result == 1)
	      {
	    	  System.out.println("**Schedule updated for staff ID:" + staff_id + "**");
	    	  DBConnections.closeDbConnection();
	    	  return true;
	      }
	      
	      else
	      {
	    	  System.out.println("**Schedule not updated**");
	    	  DBConnections.closeDbConnection();
	    	  return false;
	      }
	}

	public void getSchedule(int staff_id) {
		
		  String sql;
	      sql ="SELECT staff_id, day, start_time, end_time FROM staffschedule WHERE staff_id =" + staff_id ;
	      rs = DBConnections.openDbConnectionForSelect(sql);
	      System.out.println("Current schedule of staff is : \n");
	      System.out.printf("%10s%25s%15s%15s%n", "Staff ID", "Date", "Start Time", "End TIme");
	      try {
			while(rs.next())
			  {
				  System.out.printf("%10d",rs.getInt("staff_id"));
				  System.out.printf("%25s",(rs.getDate("day").toString()));
				  System.out.printf("%15s",rs.getString("start_time"));
				  System.out.printf("%15s%n",rs.getString("end_time"));
			  }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      System.out.println("\n\n");
	}

	public String getTheatreFromId(int theatreId) {
		
		String name = "";
		DBConnections.query = "select name from THEATRE where theatre_id = " + theatreId;
		rs = DBConnections.openDbConnectionForSelect(DBConnections.query);
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
		return name;
	}
}