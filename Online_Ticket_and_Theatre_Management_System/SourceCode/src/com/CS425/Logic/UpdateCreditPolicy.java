package com.CS425.Logic;

import java.util.Scanner;

import com.CS425.Db.DBQueries;

public class UpdateCreditPolicy {

	public void updateCreditPolicy()
	{
		DBQueries.listPolicy();
		Scanner input = new Scanner(System.in);
		while(true)
		{
			System.out.println("Do you want to update values in policy ? type yes or No : ");
			String in = input.nextLine();
			if(in.toUpperCase().equals("YES"))
			{
				
				int option = 0;
				String membership_status;
				String policy_type;
				float new_policy_value;
					System.out.println("Please select the value to be updated: ");
					System.out.println("1 : Review Points");
					System.out.println("2 : Purchase Points");
					System.out.println("3 : Threshold for memberships");
					System.out.println("4 : Previous Screen");
					option = Integer.parseInt(input.nextLine());
					switch(option)
					{
						case 1 :
						{
							policy_type = "review_points";
							System.out.println("Enter membership status for which value needs to be updated");
							membership_status = input.nextLine();
							if(DBQueries.verifyMembershipStatus(membership_status))
								{
								System.out.println("Enter new policy value : ");
								new_policy_value = Float.parseFloat(input.nextLine());
								DBQueries.updatePolicy(membership_status, policy_type, new_policy_value);
								}
							else
								System.out.println("Please enter valid membership status");
							break;
						}
						
						case 2:
						{
							policy_type = "purchase_points";
							System.out.println("Enter membership status for which value needs to be updated");
							membership_status = input.nextLine();
							if(DBQueries.verifyMembershipStatus(membership_status))
							{
							System.out.println("Enter new policy value : ");
							new_policy_value = Float.parseFloat(input.nextLine());
							DBQueries.updatePolicy(membership_status, policy_type, new_policy_value);
							}
						else
							System.out.println("Please enter valid membership status");
							
							break;
						}
							
						case 3:
						{
							policy_type = "threshold";
							System.out.println("Enter membership status for which value needs to be updated");
							membership_status = input.nextLine();
							if(DBQueries.verifyMembershipStatus(membership_status))
							{
							System.out.println("Enter new policy value : ");
							new_policy_value = Float.parseFloat(input.nextLine());
							DBQueries.updatePolicy(membership_status, policy_type, new_policy_value);
							}
						else
							System.out.println("Please enter valid membership status");
							break;
						}
						
						case 4:
							return;
						default:
							System.out.println("Please enter correct option");
					}
				
			}
			else if (in.toUpperCase().equals("NO"))
				return;
			else
				System.out.println("You have entered wrong option");
		}
		
	}
}
