package com.example.android.housecut;

/**
 * Created by Jose Fernandes on 11/30/2016.
 */

public class Roommate {
    public String name;
    public Boolean isAdmin;

    Roommate(String name ) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
    
    public void setAdmin() {
        this.isAdmin = true;   
    }
    
    public void removeAdmin() {
        this.isAdmin = false;
    }
    
    public Boolean isAdmin() {
        return isAdmin;   
    }
}
