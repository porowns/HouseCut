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


public class HouseMember {
	
	//"http://housecut-145314.appspot.com/"
	
	//function to open connection for POST
	public static String httpGet(String urlStr) throw IOException {
		URL url = new URL (urlStr);
		HttpURLConnection conn = 
			(HttpURLConnection) url.openConnection();
		
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}	
	}
		
	//HouseMember constructor
	Housemember() {
		
		//TODO : Default constructor for Housemember
		
	}
	
	
	//maybe we need this? No? is Register a constructor?
	HouseMember(string password, String name, String email) {
		this.ID = id;
		this.name = name;
		this.current_household = household;
		
		httpGet("http://housecut-145314.appspot.com/register");	//register the user
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("username", name);
		conn.setRequestProperty("email", email);
		conn.setRequestProperty("password", password);
		
		if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			throw new RuntimeException("Failed : HTTP Error code : "
				+ conn.getResponseCode());
		}
		
		/*BufferedReader br = new BufferedReader(new InputStreamReader(
			(conn.getInputStream())));*/
			
		conn.disconnect();
		
	} catch (MalformedURLException e) {
		
		e.printStackTrace();
		
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	
	

	
	public void changePassword() {
		
		httpGet("http://housecut-145314.appspot.com/");
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