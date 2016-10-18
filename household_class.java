// Code by Kenneth McManus
// 10/16/16

import java.util.Vector;

// Household class

public class household {
	Vector<household_member_class> household_vector; 
	string householdName;

	// household class constructor
	public household(household_admin admin, string name) {
		// add the first member (the admin) to the household
		// when the household is created
		household_vector.add(admin);
		householdName = name;
		
	}
	
	// adds a new roommate to the household
	public void addMember (household_member member)
	{
		household_vector.add(member);
		
		return;
	}
	
	// removes a chosen roommate from the household
	public void removeMember( household_member member)
	{
		household_vector.remove(member);
		
		return;
	}
	
	public void renameHousehold(string newName)
	{
		// make sure the new name uses valid characters: letters and numbers
		if( newName.matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789]*")
			householdName = newName;
		   
		return;
	}

}
