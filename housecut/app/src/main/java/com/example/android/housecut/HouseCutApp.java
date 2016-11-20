package com.example.android.housecut;

import com.example.android.housecut.household_member_class;
import android.app.Application;

/**
 * Created by ckm13 on 11/20/2016.
 */
public class HouseCutApp extends Application {

    private household_member_class user;

    public household_member_class getUser() {
        return user;
    }

    public void setUser(household_member_class user) {
        this.user = user;
    }
}
