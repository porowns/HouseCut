//Code by: Adam Faulkner
//10/09/2016
//class testing, V.1.0

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.net.HttpURLConnection
import java.net.MalformedURLException


/*Housemember class*/

//"http://housecut-145314.appspot.com/"

public class HouseMember {

	//HouseMember constructor
	Housemember() {
		
		//TODO : Default constructor for Housemember
		this.name = "null";
		this.email = "null";
		this.ID = 0;
	}
	
	
	//maybe we need this? No? is Register a constructor?
	HouseMember(string password, String name, String email) { throw IOException {
		this.ID = id;
		this.name = name;
		this.current_household = household;
		//-------------------------------------------

			//Encode POST values to send to HTTP Server
		String enc_pass = URLEncoder.encode(password, "UTF-8");
		String enc_name = URLEncoder.encode(name, "UTF-8");
		String enc_email= URLEncoder.encode(email, "UTF-8");
		
		
			//Open a connection (to the server) for POST
	
		URL url = new URL ("http://housecut-145314.appspot.com/register");
		
			//Declare connection object
		HttpURLConnection conn = 
				(HttpURLConnection) url.openConnection();
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
			
			//Register the user
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("username", enc_name);
		conn.setRequestProperty("email", enc_email);
		conn.setRequestProperty("password", enc_password);
		
			/*If HTTP connection fails, throw exception*/
		
		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : "
				+ conn.getResponseCode());
		}
		
		//Opens up an outputstreamwriter for writing to server
		
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());	//retrieve output stream that matches with Server input stream
		out.write("username:" + enc_name);		//what will be written..
		out.write("email:" + enc_email);
		out.write("password:" + enc_pass);
		out.close();
		
		//To test what the server outputs
		BufferedReader in = new BufferedReader(
								new InputStreamReader(
								(conn.getInputStream())));
		String dataString;
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
	
	public void changePassword() {
		
			//Open a connection (to the server)
		URL url = new URL ("http://housecut-145314.appspot.com/register");
		
			//Declare connection object
		HttpURLConnection conn = 
				(HttpURLConnection) url.openConnection();
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());		//!!! deal with exceptions..
			
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty()

		
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
	
	
	
	
	
	
	private:
		String current_household;
		//String user_name;
		String name;
		String request = "http://housecut-145314.appspot.com/";	//this might not be needed
		int ID;
		string password;
		
}
