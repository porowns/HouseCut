package com.example.android.housecut;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by ckm13 on 11/27/2016.
 */

public class Household {
    private String name;
    private String id;
    private HashMap<String, String> roommates;

    public Household() {
        this.roommates = new HashMap<>(100);
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public void printRoommates() {
        Set set = roommates.entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry rm = (Map.Entry)i.next();
            System.out.print("Key: " + rm.getKey().toString() + " Value: " + rm.getValue().toString());
        }
    }

    public String getRoommateNameFromId(String id) {
        System.out.println("getting name from id: " + id);
        String name = roommates.get(id);
        System.out.println(name);
        if (name != null) {
            System.out.println("name was: " + name);
            return name;
        }
        System.out.println("Name was null....");
        return "";
    }

    public String getRoommateIdFromName(String name) {
        Set set = roommates.entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry rm = (Map.Entry)i.next();
            if (rm.getValue().toString().equals(name)) {
                return rm.getKey().toString();
            }
        }

        return "";
    }


    public void setName(String name) { this.name = name; }
    public void setId(String id) { this.id = id; }
    public void addRoommate(String name, String id) {
        roommates.put(id, name);
    }
    public void removeRoommateById(String id) {
        roommates.remove(id);
    }
    public void removeRoommateByName(String name) {
        roommates.values().remove(name);
    }
}

