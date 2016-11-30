package com.example.android.housecut;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

/**
 * Created by Jose on 11/21/2016.
 */

public class join_household_activity extends AppCompatActivity{

    // UI references.
    private EditText mNameView;
    private EditText mPasswordView;
    private TextView mMessageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_household);

        mNameView = (EditText) findViewById(R.id.householdname);
        mPasswordView = (EditText) findViewById(R.id.householdpassword);
        mMessageView = (TextView)findViewById(R.id.householdloginmessage);

        Button createHousehold =(Button) findViewById(R.id._create_button);
        Button joinHousehold =(Button) findViewById(R.id._join_button);

        joinHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptJoin();
            }
        });


        createHousehold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createHousehold();
            }
        });

        mMessageView.setVisibility(View.GONE);


    }
    public void createHousehold() {
        Intent intent = new Intent(join_household_activity.this, create_household_activity.class);
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();
        intent.putExtra("name", name);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    public void attemptJoin() {
        String name = mNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        mMessageView.setVisibility(View.VISIBLE);
        mMessageView.setText("Loading...");

        AsyncTaskRunner runner = new AsyncTaskRunner(this, mMessageView);

        runner.execute(name, password);
    }

    class AsyncTaskRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        private TextView mMessageView;

        public AsyncTaskRunner(Context ctx, TextView mMessageView) {
            this.ctx = ctx;
            this.mMessageView = mMessageView;
        }

        @Override
        protected String doInBackground(String... params) {
            return joinHousehold(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                Intent intent = new Intent(this.ctx, main_page_activity.class);
                this.ctx.startActivity(intent);
            } else {
                this.mMessageView.setVisibility(View.VISIBLE);
                this.mMessageView.setText(responseString);
            }
        }

        public String joinHousehold(String name, String password) {
            //Catch invalid Encoder setting exception
            try {

                //Open a connection (to the server) for POST

                URL url = new URL("http://10.0.2.2:8080/joinhousehold");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");


                //Creates JSON string to write to server via POST
                JSONObject json = new JSONObject();
                json.put("houseHoldName", name);
                json.put("houseHoldPassword", password);
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
                    System.out.println("Join household success\n");
                    String householdId = data.getString("householdId");
                    user.setHouseholdId(householdId);
                    responseString = "success";


                    System.out.println("Getting roommates");
                    /* get roommates */

                    try {
                        url = new URL("http://10.0.2.2:8080/household/roommates?token=" + token);

                        //Declare connection object
                        conn = (HttpURLConnection) url.openConnection();

                        conn.setRequestMethod("GET");

                        in = new BufferedReader(
                                new InputStreamReader(conn.getInputStream()));

                        result = new StringBuffer();
                        while ((line = in.readLine()) != null) {
                            System.out.println(result);
                            result.append(line);
                        }

                        data = new JSONObject(String.valueOf(result));

                        System.out.println(data);

                        success = data.getBoolean("success");

                        Household household = new Household();

                        if (success) {
                            JSONArray roommates = data.getJSONArray("roommates");
                            for (int i = 0; i < roommates.length(); i++) {
                                JSONObject roommateJSON = roommates.getJSONObject(i);
                                String roommateName = roommateJSON.getString("displayName");
                                String roommateId = roommateJSON.getString("id");
                                household.addRoommate(roommateName, roommateId);
                            }

                            in.close();
                            conn.disconnect();

                       /* get household name */
                            try {
                                url = new URL("http://10.0.2.2:8080/household/name?token=" + token);

                                //Declare connection object
                                conn = (HttpURLConnection) url.openConnection();

                                conn.setRequestMethod("GET");

                                in = new BufferedReader(
                                        new InputStreamReader(conn.getInputStream()));

                                result = new StringBuffer();
                                while ((line = in.readLine()) != null) {
                                    System.out.println(result);
                                    result.append(line);
                                }

                                data = new JSONObject(String.valueOf(result));

                                System.out.println(data);

                                success = data.getBoolean("success");

                                if (success) {
                                    String householdName = data.getString("name");
                                    household.setName(householdName);

                                    ((HouseCutApp) this.ctx.getApplicationContext()).setHousehold(household);
                                    responseString = "success";
                                } else {
                                    String message = data.getString("message");
                                    System.out.println(message + "\n");
                                    responseString = data.getString("message");
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();

                            } catch (IOException e) {

                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String message = data.getString("message");
                            System.out.println(message + "\n");
                            responseString = data.getString("message");
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
