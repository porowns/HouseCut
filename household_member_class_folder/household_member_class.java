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
        this.current_household = null;
        this.name = null;
        this.email = null;
        this.id = null;
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

        //set Object member data..
        this.setUserInfo(username, email, password);

        try {

          //Open a connection (to the server) for POST

          URL url = new URL ("http://housecut-145314.appspot.com/register");

          //Declare connection object
          HttpURLConnection conn = (HttpURLConnection) url.openConnection();

          conn.setDoOutput(true);
          conn.setRequestMethod("POST");
          conn.setRequestProperty("Content-Type", "application/json");
          conn.setRequestProperty("Accept", "application/json");

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
              this.setErrorMessage(message);
          }

          //Closes everything
          in.close();
          conn.disconnect();

            //return true/false based on server response
          return success;

          } catch (MalformedURLException e) {

              e.printStackTrace();

          } catch (IOException e) {

              e.printStackTrace();
          } catch (JSONException e) {

              e.printStackTrace();
          }
      }

      //calls household_member_class & just passes in the new password, as well as original data
  public void changePassword(String new_pass) {

    /*THIS NEEDS EDITING*/

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

        //To test what the server outputs
        BufferedReader in = new BufferedReader(
                            new InputStreamReader((conn.getInputStream())));

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
          //Set protected member string "errorMessage" to the server error message
            String message = data.getString("message");
            this.setErrorMessage(message);
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

  } //End Function

      //Join a certain household
  public boolean joinHousehold(String h, String p) {

      if (current_household == null) {
        current_household = h;

      //Begin Server call for /joinhousehold
      try {

          //Open a connection (to the server) for POST

        URL url = new URL ("http://housecut-145314.appspot.com/joinhousehold");

          //Declare connection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");


          //Creates JSON string to write to server via POST
        JSONObject json = new JSONObject();
        json.put("token", this.getToken());
        json.put("houseHoldName", h);
        json.put("houseHoldPassword", p);
        String requestBody = json.toString();

        //Opens up an outputstreamwriter for writing to server
            //retrieve output stream that matches with Server input stream..
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

          //Write with JSON....
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
        JSONObject data = new JSONObject(result);
        boolean success = data.getBoolean("success");

          //error checking
        if (success == true)
          System.out.println("Account has been deleted.");
        else {
          String message = data.getString("message");
            //Set protected member string "errorMessage" to the server error message
          this.setErrorMessage(message);
        }

        in.close();
        conn.disconnect();

        //return true/false based on server response
      return success;

      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }

    }
    else {
        this.setErrorMessage("You must leave your current household first.");
        return false;
      }
    } //End Function

    public boolean leaveHousehold() {
      this.leaveHousehold(id);  //pass in default user id
    }

      //user leaves current_household
      //OR, if Admin, can remove/kick a roommate from household
    public boolean leaveHousehold(String userID) {

      if (current_household == null)
        return false;
      else {
        //Begin Server call for /joinhousehold
    try {

          //Open a connection (to the server) for POST
        URL url = new URL ("http://housecut-145314.appspot.com/household/roommates");

          //Declare connection object
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");


          //Creates JSON string to write to server via POST
        JSONObject json = new JSONObject();
        json.put("operation", "remove";
        json.put("userId", userID);
        String requestBody = json.toString();

        //Opens up an outputstreamwriter for writing to server
            //retrieve output stream that matches with Server input stream..
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

          //Write with JSON....
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
        JSONObject data = new JSONObject(result);
        boolean success = data.getBoolean("success");

          //error checking
        if (success == true)
          System.out.println("Account has been deleted.");
        else {
          String message = data.getString("message");
            //Set protected member string "errorMessage" to the server error message
          this.setErrorMessage(message);
        }

        in.close();
        conn.disconnect();

          //return true/false based on server response
        return success;

      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  } //END of Function


	public bool removeHouseholdMember(household_member_class member)
	{
		//written by Logan Vega//
		try {
			String url = request + "remove";
			//Get /household /roommates
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			JSONObject json = new JSONObject();
			json.put("operation", remove);
			json.put("userID", member.getID());
			String requestBody = json.toString();

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

			out.write(requestBody);
			out.close();

			//getResponseCode()!

			BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    conn.getInputStream()));

			StringBuffer result = new StringBuffer();
			String line = "";
			System.out.println("Output from Server .... \n");
			while ((line = in.readLine()) != null) {
				System.out.println(result);
				result.append(line);
			}

			//JSON string returned by server
			JSONObject data = new JSONObject(result);
			Bool success = data.getBoolean("success");

			if (success == true)
			//member was removed
				return true;
			else {
			//member was not removed
				String message = data.getString("message");
			}
		        } catch (MalformedURLException e) {

               		e.printStackTrace();

            		} catch (IOException e) {

                	e.printStackTrace();
          		} catch (JSONException e) {

                	e.printStackTrace();
     			}
			
		//fails if trying to remove the only admin, must appoint a new admin before removing
		return false;
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

    /* Private mutator functions */

    private setUserInfo(String u, String e, String p) {
        this.name = u;
        this.email = e;
        this.password = p;
  	}

  	private setErrorMessage(String m) {
  		  this.errorMessage = m;
  	}

} //End of Class
