/* created by Logan Vega and Simon Aizpurua */

import java.util.*;


public class household_admin extends household_member_class
{

	public household_admin(household_member_class member)
	{
  		//conversion constructor converts household_member_class to household_admin
		giveAdminPrivileges(member);	
	}

	public bool removeHouseholdMember(household_member_class member)
	{
    	//fail if no members would be left in household (done server-side?)
		if (household_vector.size() <= 1)
			return false;
		else {
			household.removeMember(member);
			return true;
		}
		return false; 
	}

	public bool giveAdminPrivileges(household_member_class member)
	{
			member.role = admin;
			household_admin admin = new household_admin(member);
			return true;
     	 //TODO create conversion constructor to convert household_member_class to household_admin
	}

	public bool revokeAdminPrivileges(household_admin admin)
	{
		//TODO create conversion constructor to convert household_admin to household_member_class
		household_member_class member = new household_member_class(admin);
		return true;
	}	
}
