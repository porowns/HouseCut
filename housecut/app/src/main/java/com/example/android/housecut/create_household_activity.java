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

/**
 * Created by Jose on 11/21/2016.
 *
 * Edited by Chris on 11/22/2016.
 * Edited by Nick on 11/29/2016
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
            AsyncTaskRunner runner = new AsyncTaskRunner(this, mCreateHouseholdMessageView);
            runner.execute(name, password);
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


    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private Context ctx;
        private TextView mMessageView;

        public AsyncTaskRunner(Context ctx, TextView mCreateHouseholdMessageView){
            this.ctx = ctx;
            this.mMessageView = mCreateHouseholdMessageView;
        }

        @Override
        protected String doInBackground(String... params) {

                /* TODO: add create household server call here */
            return ("success");
        }
        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();

                finish();
                if (user.getHousehold().equals("0")) {
                    Intent intent = new Intent(this.ctx, join_household_activity.class);
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
        public boolean createHousehold(String hhName, String hhPass) {

          JSONObject json = new JSONObject();
          boolean success;

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
              json.put("houseHoldPassword");
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

              if (success == true) {
                  System.out.println("\nHousehold has been created.");
              }
              else {
                  //an error occurred
                  String errorMessage = data.getString("message");
                  System.out.print("\n %s", errorMessage);
              }

            return success;

          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } catch (JSONException e) {
              e.printStackTrace();
          }
          //Return JSON from server
          //return data;
        }

    }

}
