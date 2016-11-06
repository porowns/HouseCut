package com.example.android.housecut;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.housecut.household_member_class;

/**
 * Created by nick, jose and adam on 10/18/16.
 */

public class register_activity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailConfirmView;
    private EditText mPasswordConfirmView;
    private EditText mUsernameView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (EditText) findViewById(R.id.username);
        mEmailConfirmView = (EditText) findViewById(R.id.emailConfirm);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);



        final String email = mEmailConfirmView.getText().toString();
        final String password = mPasswordConfirmView.getText().toString();
        final String username = mUsernameView.getText().toString();

        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_user_button);


        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.doInBackground(password, username, email);
                Intent intent = new Intent(register_activity.this, register_confirmation_activity.class);
                startActivity(intent);
            }
        });

    }


    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            household_member_class house = new household_member_class(params[0], params[1], params[2]);
            return "All done!";
        }
    }


}

