package com.example.android.housecut;
//Code by: Adam Faulkner
//Debugging by: Jose Fernandes
//10/09/2016
//class testing, V.1.0

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


/*House member class*/

//"http://housecut-145314.appspot.com/"

public class household_member_class {


    protected String current_household;
    protected String user_name;
    protected String name;
    protected String request = "http://housecut-145314.appspot.com/";
    protected String id;
    protected String token;
    protected String password;
    protected String email;
    protected String role = "member"; // roles can be member or admin
    protected String errorMessage;



    //Default constructor
    public household_member_class() {

        //Default constructor for Housemember
        this.current_household = "null";
        this.name = "null";
        this.email = "null";
        this.id = "";
    }


    //Constructor that will take in user data and register a user
    public household_member_class(String n, String e, String p) {
        this.name = n;
        this.email = e;
        this.password = p;

        //Call register
        this.register(n, e, p);
    }

    /*Function that will register a house member via REST API requests*/
    public boolean register(String username, String email, String password) {
        //register assumes correct user input

        try {

            //Open a connection (to the server) for POST

            URL url = new URL ("http://housecut-145314.appspot.com/register");

            //Declare connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //If i need to do JSON...
            //	String input = "{\"username\": \"" + enc_name "\""\email\": + enc_email

            //Creates JSON string to write to server via POST
            JSONObject json = new JSONObject();
            json.put("username", username);
            json.put("email", email);
            json.put("password", password);
            String requestBody = json.toString();

            //Opens up an outputstreamwriter for writing to server
            //retrieve output stream that matches with Server input stream..
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            //OR, with JSON....
            out.write(requestBody);

            out.close();

			/*If HTTP connection fails, throw exception*/
            //might ought to be 200
	/*	if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
		}
		*/
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
            JSONObject data = new JSONObject(result.toString());
            boolean success = data.getBoolean("success");

            //error checking
            if (success == true)
                System.out.println("Account has been deleted.");
            else {
                String message = data.getString("message");

                //Set protected member string "errorMessage" to the server error message
                errorMessage = message;
                //return true/false based on server response

            }

            //Closes everything
            in.close();
            conn.disconnect();

            //Returns the condition
            return success;

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {

            e.printStackTrace();
        }
        return false;
    }

    //calls household_member_class & just passes in the new password, as well as original data
    public void changePassword(String new_pass) {

        register(getName(), getEmail(), new_pass);

        password = new_pass;
    }

    /* Uses endpoint /deleteaccount & token */
    public boolean deleteAccount(String token) {

        try {

            //For JSON..
            JSONObject jsonToken = new JSONObject();
            jsonToken.put("token", token);
            String requestBody = jsonToken.toString();

            //Open a connection (to the server) for POST

            URL url = new URL ("http://housecut-145314.appspot.com/deleteaccount");

            //Declare connection object
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();

            //Delete the user
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            //Opens up an outputstreamwriter for writing to server

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            out.write(requestBody);
            out.close();

			/* If Response code isn't 200, throw exception. */

            if (conn.getResponseCode() != 200) {
                throw new IOException(conn.getResponseMessage());
            }

			/* If HTTP connection fails, throw exception.

		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : "
				+ conn.getResponseCode());
		}*/

            //To test what the server outputs
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            (conn.getInputStream())));

            StringBuffer result = new StringBuffer();
            String line = "";
            System.out.println("Output from Server .... \n");
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                result.append(line);
            }

            //JSON string returned by server
            JSONObject data = new JSONObject(result.toString());
            boolean success = data.getBoolean("success");

            //error checking
            if (success == true) {
                System.out.println("Account has been deleted.");
            }
            else {
                String message = data.getString("message");

                //Set protected member string "errorMessage" to the server error message
                errorMessage = message;
                //return true/false based on server response
            }

            in.close();
            conn.disconnect();

            //Once everything has been closed, the result is returned
            return success;
        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (JSONException e) {

            e.printStackTrace();
        }
        return false;

    }


	/*	//log user out of account
	public void logout() {


	}*/


    public void joinHousehold(String h) {

        //Join a certain household

        if (current_household == null)
            current_household = h;
        else
            System.out.println("You must leave your current household first.\n");
        //Might have to call a popup error, or simply not let the user have the option to
        //join Household until they have left their current one
    }

    //user leaves current_household
    public void leaveHousehold() {

        current_household = null;

    }


	/* Simple getter functions */

    //Return name field
    public String getName() {
        return name;
    }

    //Return password field
    public String getPassword() {
        return password;
    }

    //Return email field
    public String getEmail() {
        return email;
    }

    public String getID() {
        return id;
    }

    public String getToken() {
        return token;	//Check
    }

    //Return current_household field
    public String getHousehold() {
        return current_household;
    }

    public String errorMessage() {
        return errorMessage;
    }

}