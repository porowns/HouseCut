package com.example.android.housecut;

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
import java.lang.String;

//Implemented by Nick and Jose
public class login_activity extends AppCompatActivity {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email1);
        mPasswordView = (EditText) findViewById(R.id.password1);



        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

    }

    public void registerUser() {


        Intent intent = new Intent(login_activity.this, register_activity.class);
        startActivity(intent);
    }
    public void attemptLogin() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        //String line = "";

        AsyncTaskRunner runner = new AsyncTaskRunner();

        //runner.execute(email, password, line);
        runner.execute(email, password);
        //if(line.contains("\"success\":true"))
        {
            Intent intent = new Intent(login_activity.this, main_page_activity.class);
            startActivity(intent);
        }
        //else
        {
            //Intent issue = new Intent(login_activity.this, login_issue_activity.class);
            //startActivity(issue);
        }

    }




}

class AsyncTaskRunner extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        //loginToServer(params[0], params[1], params[2]);
        loginToServer(params[0], params[1]);
        return null;
    }


    //public void loginToServer(String email, String password, String line){
    public void loginToServer(String email, String password){
        //Catch invalid Encoder setting exception

        //System.out.println(email);


        try {

            //Open a connection (to the server) for POST

            URL url = new URL ("http://housecut-145314.appspot.com/login");

            //Declare connection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");


            //Creates JSON string to write to server via POST
            JSONObject json = new JSONObject();
            json.put("email", email);

            json.put("password", password);

            String requestBody = json.toString();

            //Opens up an outputstreamwriter for writing to server
            //retrieve output stream that matches with Server input stream..
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");

            //OR, with JSON....
            out.write(json.toString());

            out.close();	//flush?  .writeBytes?

			/*If HTTP connection fails, throw exception*/


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
            JSONObject data = new JSONObject(String.valueOf(result));


            System.out.println(data);


            in.close();
            conn.disconnect();

        } catch (MalformedURLException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}