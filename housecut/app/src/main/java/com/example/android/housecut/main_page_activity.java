package com.example.android.housecut;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implemented by Nick and Jose
 */

public class main_page_activity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);

        GetRoommateRunner getRoomies = new GetRoommateRunner(this);

        getRoomies.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_toolbar, menu);
        return true;
    }



    class GetRoommateRunner extends AsyncTask<String, Void, String> {
        private Context ctx;
        ArrayList<Roommate> roommateList;

        public GetRoommateRunner(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            return getRoommateList();
        }

        @Override
        protected void onPostExecute(String responseString) {

            if (responseString.equals("success")) {
                RoommateAdapter adapter = new RoommateAdapter(main_page_activity.this, this.roommateList);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);

            }
            else {

            }
        }

        public String getRoommateList(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                Household household = app.getHousehold();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/roommates?token=" + token);

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuffer result = new StringBuffer();
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(result);
                    result.append(line);
                }

                JSONObject data = new JSONObject(String.valueOf(result));

                System.out.println(data);

                Boolean success = data.getBoolean("success");

                String responseString;

                if (success) {
                    System.out.println("Get roommates success\n");

                    TextView titleView = (TextView) findViewById(R.id.main_page_title);

                    titleView.setText( household.getName() );

                    JSONArray roommatelist = data.getJSONArray("roommates");

                    roommateList = new ArrayList<Roommate>(roommatelist.length());


                    for (int i = 0; i < roommatelist.length(); i++) {
                        JSONObject taskJSON = roommatelist.getJSONObject(i);
                        String name = taskJSON.getString("displayName");

                        roommateList.add(new Roommate(name));
                    }
                    responseString = "success";
                }
                else {
                    System.out.println("Get roommate failure\n");
                    String message = data.getString("message");
                    System.out.println(message + "\n");
                    responseString = data.getString("message");
                }

                in.close();
                conn.disconnect();

                return responseString;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Failure";
        }
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings_icon:
                intent = new Intent(main_page_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(main_page_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(main_page_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(main_page_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }
}