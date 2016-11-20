/* created by Logan Vega and Simon Aizpurua */
/*Initial server & Json handling done by Adam Faulkner & Logan Vega*/

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import org.json.JSONObject;
import org.json.JSONException;

public class household_admin extends household_member_class
{

	public household_admin(household_member_class member)
	{
  		//conversion constructor converts household_member_class to household_admin
		giveAdminPrivileges(member);
	}

	public bool giveAdminPrivileges(household_member_class member)
	{
		//only allowed if role of caller = admin
		try {
			String url = request + "setadmin";
			//Get /household /roommates
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			JSONObject json = new JSONObject();
			json.put("userId", member.getID());
			json.put("setAdmin", true);
			json.put("token", member.getToken());
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
			//admin was given
				return true;
			else {
			//admin was not given
				String message = data.getString("message");
			}
		        } catch (MalformedURLException e) {

                	e.printStackTrace();

            		} catch (IOException e) {

                	e.printStackTrace();
            		} catch (JSONException e) {

                	e.printStackTrace();
     			}
			
		return false;
	}

	public bool revokeAdminPrivileges(household_admin admin)
	{
		//only allowed if role of caller = admin

		try {
			String url = request + "setadmin";
			//Get /household /roommates
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			JSONObject json = new JSONObject();
			json.put("userId", member.getID());
			json.put("setAdmin", false);
			json.put("token", member.getToken());
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
			//admin was taken away
				return true;
			else {
			//admin was not taken away
				String message = data.getString("message");
			}
		        } catch (MalformedURLException e) {

                	e.printStackTrace();

            		} catch (IOException e) {

                	e.printStackTrace();
            		} catch (JSONException e) {

                	e.printStackTrace();
     			}
			
		return false;
	}
}
