package com.CS425.Logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import com.CS425.Db.FetchData;
import com.CS425.bean.OrderDetails;

public class UserOrderHistory {

	public boolean viewOrderHistory(int memberId){

		FetchData data = new FetchData();
		int option;
		Scanner sc = new Scanner(System.in);
		ArrayList<OrderDetails> orderHistory = new ArrayList<OrderDetails>();
		orderHistory = data.getUserOrderHistory(memberId);
		OrderDetails order;

		System.out.println("\n\t\t-----------------------------Order History----------------------------------");
		if(orderHistory == null)
			System.out.println("*No order history found.*");
		else{
			System.out.println("Order ID\tMovie Name\t\tTheatre Name\t\tTheatre Location\t\tTime\t\tDay\t\tQuantity\tBooked On");
			Iterator<OrderDetails> orderItr = orderHistory.iterator();
			while(orderItr.hasNext()){
				order = orderItr.next();
				System.out.print(order.getOrderId() + "\t\t");
				System.out.print(order.getMovieName() + "\t\t");
				System.out.print(order.getTheatreName() + "\t");
				System.out.print(order.getTheatreLocation() + "\t");
				System.out.print(order.getDay() + "\t");
				System.out.print(order.getTime() + "\t");
				System.out.print(order.getQuantity() + "\t\t");
				System.out.println(order.getTimestamp() + "\n");
			}// While
		}

		System.out.println("\n1. Home");
		System.out.println("2. Logout");
		option = Integer.parseInt(sc.nextLine());

		while(true){
			switch(option){
			case 1:
				return true;
			case 2:
				return false;
			default:
				System.out.println("Select appropriate option.");
			}// Switch
		}// While
	}// Function
}