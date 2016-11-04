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
		//only allowed if role of caller = admin
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
		//only allowed if role of caller = admin
		member.role = "admin";
			
		return true;
	}

	public bool revokeAdminPrivileges(household_admin admin)
	{
		//only allowed if role of caller = admin
		admin.role = "member";
		
		return true;
	}	
}
