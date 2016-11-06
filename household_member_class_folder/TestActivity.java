package com.example.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.example.testapp.household_member_class;

/* 
   This is a main activity file for Android Studio. It is a preliminary test of the household_member_class.java and the 
   household_member_class constructor (i.e., register function). It is not finished, however the end goal is to test 
   the POST /register method for the REST server, and to check if the function successfully posts a users password, name, and email
   to the server.
*/


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }


    AsyncTaskRunner task = new AsyncTaskRunner();

}

class AsyncTaskRunner extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        household_member_class house = new household_member_class("123", "Adam", "mcfaulky@gmail.com");

        return "All done!";
    }
    /*
   * (non-Javadoc)
   *
   * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
   */

}
