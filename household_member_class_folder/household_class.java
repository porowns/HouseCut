package com.housecut.housecut.household_member_class_folder;
// Code by Kenneth McManus
// Debugging by Jose Fernandes
// 10/16/16

import java.util.Vector;
import java.lang.String;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import org.json.JSONObject;
import org.json.JSONException;

// Household class

public class household_class {
	Vector< household_member_class > household_vector;
	protected String request = "http://10.0.2.2:8080/";
	protected String householdName;
	protected String currentAdmin;

	// write to server
	private JSONObject writeToServer (JSONObject json, String url) 
	{

          JSONObject data = new JSONObject();

          try {
            //Open a connection (to the server) for POST

            URL url = new URL(url);

            //Declare connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            String requestBody = json.toString();

            //Opens up an outputstreamwriter for writing to server
            //retrieve output stream that matches with Server input stream..
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            //Write data to Server..
            out.write(requestBody);
            out.close();

            /* If Response code isn't 200, throw exception. */

            if (conn.getResponseCode() != 200) {
              throw new IOException(conn.getResponseMessage());
            }

            //To test what the server outputs AND finish sending request
            BufferedReader in = new BufferedReader(
                              new InputStreamReader(
                              conn.getInputStream()));

            //StringBuffer will hold JSON string
            StringBuffer result = new StringBuffer();
            String line = "";
            System.out.println("Output from Server .... \n");
            while ((line = in.readLine()) != null) {
               System.out.println(result);
               result.append(line);
            }

            //JSON string returned by server
            data = new JSONObject(result.toString());

            //Closes everything
            in.close();
            conn.disconnect();


            } catch (MalformedURLException e) {
              e.printStackTrace();
            } catch (IOException e) {
              e.printStackTrace();
            } catch (JSONException e) {
              e.printStackTrace();
            }
            //Return JSON from server
            return data;
          }

	
	// household class constructor
	public household_class(household_member_class admin, String name) {
		// add the first member (the admin) to the household when the household is created
		household_vector.add(admin);
		currentAdmin = household_member_class.name;
		householdName = name;
		
		
	}
	
	// adds a new roommate to the household
	public void addMember (household_member_class member)
	{
		boolean added = false;
		for(int i = 0; i < household_vector.size(); i++)
		{
			if(household_vector[i].name.equals( member.name ) ){
				added = true;
			}
		}
		
		if ( !added ) {
			household_vector.add(member);
		}
		return;
	}
	
	// removes a chosen roommate from the household
	public void removeMember( household_member_class member)
	{
		for(int i = 0; i < household_vector.size(); i++)
		{
			if(household_vector[i].name.equals( member.name ) ){
				household_vector.remove(member);
			}
		}
		
		return;
	}
		   
	public void deleteHousehold()
        {
		household_vector.clear();
		householdName = "null";
			
		return;
	}
	
	public void clearHousehold() // except the admin
	{
		for(int i = 0; i < household_vector.size(); i++)
			if(currentAdmin != household_vector[i].name)
				household_vector.remove(household_vector[i]);	
	}

	
	// Mutators
	
	// change the admin role to another person
	public void changeAdmin(String newAdmin)
	{
		boolean valid = false;
		for(int i = 0; i < household_vector.size(); i++)
			if(newAdmin == household_vector[i])
				valid = true;
		if (valid)
			currentAdmin = newAdmin;
		return;
	}	   
	
	// rename the household
	public void renameHousehold(String newName)
	{
		// make sure the new name uses valid characters: letters and numbers and it is not too long
		if( newName.matches("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+={[}]:;\"'<,>.?/]*" && (newName.length() <= 30))
			householdName = newName;
		   
		return;
	}
		   
	// Accessors
		   
	// returns the size of the household
	public int size()
	{
		return household_vector.length();
	}
		 
	// returns the admin of the household
	public String admin()
	{
		return currentAdmin;
	}
				    
	public void printHouseholdMembers()
	{
		for(int i = 0; i < household_vector.size(); i++)
		{
			System.out.print(household_vector[i]);
			if(i != household_vector.size() - 1)
				System.out.print(", ")
		}
	}
	public void printAdmin()
	{
		System.out.print(currentAdmin);
	}
	public void printHouseholdName()
	{
		System.out.print(householdName);
	}   

				    
	
}
