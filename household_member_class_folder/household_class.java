package com.housecut.housecut.household_member_class_folder;
// Code by Kenneth McManus
// Debugging by Jose Fernandes
// 10/16/16

import java.util.Vector;
import java.lang.String;

// Household class

public class household_class {
	Vector< household_member_class > household_vector;
	String householdName;
	String currentAdmin;

	// household class constructor
	public household_class(household_member_class admin, String name) {
		// add the first member (the admin) to the household when the household is created
		household_vector.add(admin);
		currentAdmin = household_member_class.name;
		householdName = name;
		
	}
	
	// adds a new roommate to the household
	public void addMember (household_member_class member)
	{
		household_vector.add(member);
		
		return;
	}
	
	// removes a chosen roommate from the household
	public void removeMember( household_member_class member)
	{
		for(int i = 0; i < household_vector.size(); i++)
		{
			if(household_vector[i].name == member.name)
				household_vector.remove(member);
		}
		
		return;
	}
		   
	public void deleteHousehold()
        {
		household_vector.clear();
		householdName = "null";
			
		return;
	}
	
	public void clearHousehold() // except the admin
	{
		for(int i = 0; i < household_vector.size(); i++)
			if(currentAdmin != household_vector[i].name)
				household_vector.remove(household_vector[i]);	
	}

	
	// Mutators
	
	// change the admin role to another person
	public void changeAdmin(string newAdmin)
	{
		bool valid = false;
		for(int i = 0; i < household_vector.size(); i++)
			if(newAdmin == household_vector[i])
				valid = true;
		if (valid)
			currentAdmin = newAdmin;
		return;
	}	   
	
	// rename the household
	public void renameHousehold(string newName)
	{
		// make sure the new name uses valid characters: letters and numbers and it is not too long
		if( newName.matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+={[}]:;"'<,>.?/]*" && (newName.length() <= 30))
			householdName = newName;
		   
		return;
	}
		   
	// Accessors
		   
	// returns the size of the household
	public int size()
	{
		return household_vector.length();
	}
		 
	// returns the admin of the household
	public string admin()
	{
		return currentAdmin;
	}
				    
	public void printHouseholdMembers()
	{
		for(int i = 0; i < household_vector.size(); i++)
		{
			System.out.print(household_vector[i]);
			if(i != household_vector.size() - 1)
				System.out.print(", ")
		}
	}
}
