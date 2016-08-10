package com.CS425.Db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnections implements Connections{

	protected static Connection conn = null;
	protected static PreparedStatement stmt = null;
	protected static ResultSet rs;
	protected static String query;

	protected static ResultSet openDbConnectionForSelect(String query){

		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.prepareStatement(query);
			rs = stmt.executeQuery();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return rs;
	}

	protected static int openDbConnectionForUpdate(String query){

		int result = 0;
		try{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
			stmt = conn.prepareStatement(query);
			result = stmt.executeUpdate();
		}catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
		}
		return result;
	}

	protected static void closeDbConnection(){
		try{
			if(rs != null)
				rs.close();
			stmt.close();
			conn.close();
		}catch(Exception se){
			//Handle errors for JDBC
			se.printStackTrace();
		}finally{
			//finally block used to close resources
			try{
				if(stmt!=null)
					stmt.close();
			}catch(SQLException se2){
			}// nothing we can do
			try{
				if(conn!=null)
					conn.close();
			}catch(SQLException se){
				se.printStackTrace();
			}//end finally try
		}//end try
	}
}
