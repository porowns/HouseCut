package com.example.android.housecut;

/**
 * Created by Jose Fernandes on 11/30/2016.
 */

public class Grocery {
    public String name;

    Grocery(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Grocery Item: " + this.name ;
    }
}