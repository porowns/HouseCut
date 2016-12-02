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
 *
 * Edited by Chris on 11/22/2016.
 * Edited by Nick & Adam on 11/29/2016
 */

public class create_household_activity extends AppCompatActivity {

    // UI references.
    private EditText mHouseholdConfirmNameView;
    private EditText mPasswordConfirmView;
    private EditText mHouseholdNameView;
    private EditText mPasswordView;
    private TextView mCreateHouseholdMessageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);

        mHouseholdConfirmNameView = (EditText) findViewById(R.id.nameConfirm);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);
        mHouseholdNameView = (EditText) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);
        mCreateHouseholdMessageView = (TextView)findViewById(R.id.create_household_message);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String password = intent.getStringExtra("password");
        if (name != null && !name.isEmpty()) {
            mHouseholdNameView.setText(name);
        }
        if (password != null && !password.isEmpty()) {
            mPasswordView.setText(password);
        }


        Button mCreateHouseholdButton = (Button) findViewById(R.id.create_household_button);


        mCreateHouseholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateHouseholdButton();
            }
        });

        mCreateHouseholdMessageView.setVisibility(View.GONE);

    }

    public void CreateHouseholdButton() {
        String name = mHouseholdConfirmNameView.getText().toString();
        String password = mPasswordConfirmView.getText().toString();

        mCreateHouseholdMessageView.setVisibility(View.VISIBLE);
        mCreateHouseholdMessageView.setText("Loading...");

        if(CheckName(name) && CheckPassword(password)) {
            CreateHouseholdRunner runner = new CreateHouseholdRunner(this, mCreateHouseholdMessageView);
            HouseCutApp app = ((HouseCutApp)this.getApplicationContext());
            household_member_class user = app.getUser();
            Household house = new Household();
            house.setName(name);
            house.setId(user.getID());
            app.setHousehold(house);

              //get token
            String token = user.getToken();
            runner.execute(token, name, password);
        }
        else if (!CheckName(name)) {
            mCreateHouseholdMessageView.setText("Household name fields do not match!");
        }
        else if (!CheckPassword(password)) {
            mCreateHouseholdMessageView.setText("Household password fields do not match!");
        }

    }



    public boolean CheckName(String Email) {
        String emailConfirm = mHouseholdNameView.getText().toString();
        if(emailConfirm.equals(Email))
            return true;
        else
            return false;
    }
    public boolean CheckPassword(String Pass) {
        String passConfirm = mPasswordView.getText().toString();
        if(passConfirm.equals(Pass))
            return true;
        else
            return false;
    }


    class CreateHouseholdRunner extends AsyncTask<String, String, String> {

        private Context ctx;
        private TextView mMessageView;

        public CreateHouseholdRunner(Context ctx, TextView mCreateHouseholdMessageView){
            this.ctx = ctx;
            this.mMessageView = mCreateHouseholdMessageView;
        }

        @Override
        protected String doInBackground(String... params) {
          return createHousehold(params[0], params[1], params[2]);
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();

                finish();
                if (user.getHousehold().equals("0")) {
                    Intent intent = new Intent(this.ctx, main_page_activity.class);
                    this.ctx.startActivity(intent);
                    overridePendingTransition(0, 0);
                }
                else {
                    Intent intent = new Intent(this.ctx, main_page_activity.class);
                    this.ctx.startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
            else {
                this.mMessageView.setVisibility(View.VISIBLE);
                this.mMessageView.setText(responseString);
            }
        }

            //This will call /createHousehold via POST request, asks
            //user for token, household name, & household password
        public String createHousehold(String token, String hhName, String hhPass) {

          JSONObject json = new JSONObject();
          boolean success;
          String responseString = "";

          try {
              //Open a connection (to the server) for POST

              URL url = new URL("http://10.0.2.2:8080/createhousehold");

              //Declare connection object
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();

              conn.setDoOutput(true);
              conn.setRequestMethod("POST");
              conn.setRequestProperty("Content-Type", "application/json");
              conn.setRequestProperty("Accept", "application/json");

              json.put("houseHoldName", hhName);
              json.put("houseHoldPassword", hhPass);
              json.put("token", token);

              String requestBody = json.toString();

              //Opens up an outputstreamwriter for writing to server
              //retrieve output stream that matches with Server input stream..
              OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

              //Write data to Server..
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
              JSONObject data = new JSONObject(result.toString());

              //Closes everything
              in.close();
              conn.disconnect();

              //error checking
              success = data.getBoolean("success");

              if (success) {
                  responseString = "success";
                  System.out.println("\nHousehold has been created.");
              }
              else {
                  //an error occurred
                  responseString = data.getString("message");
                  System.out.printf("\n %s", responseString);
              }


          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } catch (JSONException e) {
              e.printStackTrace();
          }

          //Return string from server
            return responseString;
        }

    }

}
