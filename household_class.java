// Code by Kenneth McManus
// 10/16/16

import java.util.Vector;

// Household class

public class household {
	Vector<household_member_class> household_vector; 

	// household class constructor
	public household(household_admin admin) {
		// add the first member (the admin) to the household
		// when the household is created
		household_vector.add(admin);
	}

}