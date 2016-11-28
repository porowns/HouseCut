package com.example.android.housecut;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nickjohnson on 11/18/16.
 * Major edit by Chris on 11/26/2016.
 */

public class task_list_activity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);

        AsyncTaskRunner runner = new AsyncTaskRunner(this);

        runner.execute();
    }

    class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        Task[] tasks;

        public AsyncTaskRunner(Context ctx){
            this.ctx = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            return getTasklist();
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                ArrayAdapter<Task> adapter = new ArrayAdapter<Task>(task_list_activity.this,
                        R.layout.tasklist_listview, this.tasks);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);

            }
            else {

            }
        }

        public String getTasklist(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                Household household = app.getHousehold();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/tasklist?token=" + token);

                System.out.println("GETting from url: " + url.toString());

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
                    System.out.println("Get tasklist success\n");
                    JSONArray tasklist = data.getJSONArray("tasklist");
                    System.out.println("Tasklist:\n---------");
                    final LinearLayout container = (LinearLayout) findViewById(R.id.container_layout);
                    tasks = new Task[tasklist.length()];
                    household.printRoommates();
                    for (int i = 0; i < tasklist.length(); i++) {
                        JSONObject taskJSON = tasklist.getJSONObject(i);
                        String name = taskJSON.getString("name");
                        String type = taskJSON.getString("type");
                        String currentlyAssignedId = taskJSON.getString("currentlyAssigned");
                        String currentlyAssignedName = household.getRoommateNameFromId(currentlyAssignedId);
                        System.out.println("Task " + i);
                        System.out.println(name);
                        System.out.println(type);
                        System.out.println(currentlyAssignedId);
                        System.out.println(currentlyAssignedName);
                        System.out.println("----");
                        tasks[i] = new Task(name, type, currentlyAssignedId, currentlyAssignedName);
                    }
                    responseString = "success";
                }
                else {
                    System.out.println("Login failure\n");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_icon:
                Intent intent = new Intent(task_list_activity.this, settings_activity.class);
                startActivity(intent);
                return true;

            case R.id.grocery_list_icon:
                Intent intent2 = new Intent(task_list_activity.this, grocery_list_activity.class);
                startActivity(intent2);
                return true;

            case R.id.task_list_icon:
                Intent intent3 = new Intent(task_list_activity.this, task_list_activity.class);
                startActivity(intent3);
                return true;

            case R.id.home_page_icon:
                Intent intent4 = new Intent(task_list_activity.this, main_page_activity.class);
                startActivity(intent4);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
