//Code by: Adam Faulkner
//10/09/2016
//class testing, V.1.0

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URLEncoder;


/*Housemember class*/

//"http://housecut-145314.appspot.com/"

public class household_member_class {


		private String current_household;
		//private String user_name;
		private String name;
		private String request = "http://housecut-145314.appspot.com/";	//this might not be needed
		private int ID;
		private String password;
		private String email;
		
		

	//HouseMember constructor
	public household_member_class() {
		
		//TODO : Default constructor for Housemember
		this.name = "null";
		this.email = "null";
		this.ID = 0;
	}
	
	
	//maybe we need this? No? is Register a constructor?
	public void household_member_class(String password, String name, String email) {
		//this.ID = ID;
		//this.name = name;
		//this.current_household = household;
		//-------------------------------------------

			 //Encode POST values to send to HTTP Server
        String enc_pass = null;
        String enc_name = null;
        String enc_email = null;

		//Catch invalid Encoder setting exception
		
        try {
            enc_pass = URLEncoder.encode(password, "UTF-8");
            enc_name = URLEncoder.encode(name, "UTF-8");
            enc_email= URLEncoder.encode(email, "UTF-8");
			
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		
	try {

			//Open a connection (to the server) for POST
	
		URL url = new URL ("http://housecut-145314.appspot.com/register");
		
			//Declare connection object
		HttpURLConnection conn = 
				(HttpURLConnection) url.openConnection();
		
			
			//Register the user
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("username", enc_name);
		conn.setRequestProperty("email", enc_email);
		conn.setRequestProperty("password", enc_pass);
		
			/*If Response code isn't 200, throw exception.*/
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}
		
	
		
		//Opens up an outputstreamwriter for writing to server
		
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());	//retrieve output stream that matches with Server input stream
		out.write("username:" + enc_name);		//what will be written..
		out.write("email:" + enc_email);
		out.write("password:" + enc_pass);
		out.close();	//flush?
		
			/*If HTTP connection fails, throw exception*/

		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : "
				+ conn.getResponseCode());
		}
		
		//To test what the server outputs
		BufferedReader in = new BufferedReader(
				new InputStreamReader((conn.getInputStream())));
				
		String dataString;
		System.out.println("Output from Server .... \n");
		while ((dataString = in.readLine()) != null) {
			System.out.println(dataString);
		}
		
		in.close();
				
		conn.disconnect();
		
	} catch (MalformedURLException e) {
		
		e.printStackTrace();
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	}
	
	/*
	public void changePassword() {
		
		 
			//Open a connection (to the server)
		URL url = new URL ("http://housecut-145314.appspot.com/register");
		
			//Declare connection object
		HttpURLConnection conn = 
				(HttpURLConnection) url.openConnection();
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());		//!!! deal with exceptions..
		}
		
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		//conn.setRequestProperty();
		
		
	}
	
	public void deleteAccount() {
		
		//TODO : Delete account from server
		
	}
	
	
	public logout() {
		
		//TODO : logout from user account
	}
	
	public joinHousehold() {
		
		//TODO : Join a certain household
		
	}
	
	public leaveHousehold() {
		
		//TODO : Leave a certain household
		
	}	
	*/
}
