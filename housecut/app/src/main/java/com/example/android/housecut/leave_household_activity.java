package com.example.android.housecut;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by nickjohnson on 11/29/16.
 */

public class leave_household_activity extends AppCompatActivity{

    Button yButton;
    Button nButton;
    TextView loadingText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_household);

        yButton = (Button) findViewById( R.id.leave_yes_button );
        nButton = (Button) findViewById( R.id.leave_no_button );
        loadingText = (TextView) findViewById( R.id.loading_leave );

        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnToSettings();
            }
        });

        yButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveHousehold();
            }
        });

        loadingText.setVisibility( View.GONE );

    }


    public void returnToSettings(){
        Intent intent = new Intent(leave_household_activity.this, settings_activity.class);
        startActivity(intent);
    }

    public void leaveHousehold(){
        AsyncTaskRunner runner = new AsyncTaskRunner(this, loadingText);
        runner.execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings_icon:
                intent = new Intent(leave_household_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(leave_household_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(leave_household_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(leave_household_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    class AsyncTaskRunner extends AsyncTask<String, Void, String>{
        private Context ctx;
        private TextView mMessageView;

        public AsyncTaskRunner(Context ctx, TextView mMessageView) {
            this.ctx = ctx;
            this.mMessageView = mMessageView;
        }

        @Override
        protected String doInBackground(String... params) {
            return leaveHousehold();
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                Intent intent = new Intent(this.ctx, login_activity.class);
                this.ctx.startActivity(intent);
            } else {
                this.mMessageView.setVisibility(View.VISIBLE);
                this.mMessageView.setText(responseString);
            }
        }

        public String leaveHousehold() {
            //Catch invalid Encoder setting exception
            try {

                //Open a connection (to the server) for POST

                URL url = new URL("http://10.0.2.2:8080/household/roommates");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");


                //Creates JSON string to write to server via POST
                JSONObject json = new JSONObject();
                json.put("operation", "remove");
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                String token = user.getToken();
                json.put("token", token);

                //Opens up an outputstreamwriter for writing to server
                //retrieve output stream that matches with Server input stream..
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

                //OR, with JSON....
                out.write(json.toString());

                out.close();    //flush?  .writeBytes?

			/*If HTTP connection fails, throw exception*/


                //To test what the server outputs AND finish sending request
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));

                //StringBuffer will hold JSON string
                StringBuffer result = new StringBuffer();
                String line;
                System.out.println("Output from Server .... \n");
                while ((line = in.readLine()) != null) {
                    System.out.println(result);
                    result.append(line);
                }

                //JSON string returned by server
                JSONObject data = new JSONObject(String.valueOf(result));

                System.out.println(data);

                Boolean success = data.getBoolean("success");

                String responseString;

                if (success) {
                    System.out.println("Leave household success\n");
                    user.setHouseholdId("0");
                    responseString = "success";

                } else {
                    System.out.println("Join household failure\n");
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

}
