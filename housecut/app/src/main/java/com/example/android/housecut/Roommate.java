package com.example.android.housecut;

/**
 * Created by Jose Fernandes on 11/30/2016.
 */

public class Roommate {
    public String name;

    Roommate(String name ) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
