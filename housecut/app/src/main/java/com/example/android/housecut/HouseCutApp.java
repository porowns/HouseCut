package com.example.android.housecut;

import android.app.Application;

/**
 * Created by Chris on 11/20/2016.
 */
public class HouseCutApp extends Application {

    private household_member_class user;
    private Household household;

    public household_member_class getUser() {
        return user;
    }

    public Household getHousehold() { return household; }

    public void setUser(household_member_class user) {
        this.user = user;
    }

    public void setHousehold(Household household) {
        this.household = household;
    }
}