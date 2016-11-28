package com.example.android.housecut;

/**
 * Created by ckm13 on 11/27/2016.
 */

public class Task {
    public String name;
    public String type;
    public String currentlyAssignedId;
    public String currentlyAssignedName;

    Task(String name, String type, String currentlyAssignedId, String currentlyAssignedName) {
        this.name = name;
        this.type = type;
        this.currentlyAssignedId = currentlyAssignedId;
        if (currentlyAssignedId.equals("0")) {
            this.currentlyAssignedName = "None";
        }
        else {
            this.currentlyAssignedName = currentlyAssignedName;
        }
    }

    @Override
    public String toString() {
        return "Task: " + this.name + "\nType: " + this.type + "\nAssigned to: " + this.currentlyAssignedName;
    }
}
