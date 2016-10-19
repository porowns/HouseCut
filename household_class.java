// Code by Kenneth McManus
// 10/16/16

import java.util.Vector;

// Household class

public class household {
	Vector<household_member_class> household_vector; 
	string householdName;
	household_member_class currentAdmin;

	// household class constructor
	public household(household_member_class admin, string name) {
		// add the first member (the admin) to the household
		// when the household is created
		household_vector.add(admin);
		currentAdmin = admin;
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
		for(int i = 0; i < household_vector.length(); i++)
		{
			if(household_vector[i].name == member.name)
				household_vector.remove(member);
				
		}
		
		return;
	}
	
	// rename the household
	public void renameHousehold(string newName)
	{
		// make sure the new name uses valid characters: letters and numbers and it not too long
		if( newName.matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789]*" && (newName.length() <= 30))
			householdName = newName;
		   
		return;
	}
		   
	public void deleteHousehold()
        {
		household_vector.clear();
		householdName = "null";
			
		return;
	}

	public void changeAdmin(household_member_class newAdmin)
	{
		currentAdmin = newAdmin;
		return;
	}
}
