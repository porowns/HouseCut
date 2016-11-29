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
 * Created by nick, jose and adam on 10/18/16.
 * Edited by Chris
 */

public class register_activity extends AppCompatActivity {


    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    private EditText mEmailConfirmView;
    private EditText mPasswordConfirmView;
    private EditText mUsernameView;
    private EditText mEmailView;
    private EditText mPasswordView;
    private TextView mRegisterMessageView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsernameView = (EditText) findViewById(R.id.username);
        mEmailConfirmView = (EditText) findViewById(R.id.emailConfirm);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mRegisterMessageView = (TextView)findViewById(R.id.register_message);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");
        if (email != null && !email.isEmpty()) {
            mEmailView.setText(email);
        }
        if (password != null && !password.isEmpty()) {
            mPasswordView.setText(password);
        }


        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_user_button);


        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterButton();
            }
        });

        mRegisterMessageView.setVisibility(View.GONE);

    }

    public void RegisterButton() {
        String email = mEmailConfirmView.getText().toString();
        String password = mPasswordConfirmView.getText().toString();
        String username = mUsernameView.getText().toString();




        mRegisterMessageView.setVisibility(View.VISIBLE);
        mRegisterMessageView.setText("Loading...");

        if(CheckEmail(email) && CheckPassword(password)) {
            register_activity.AsyncTaskRunner runner = new register_activity.AsyncTaskRunner(this, mRegisterMessageView);
            runner.execute(username, email, password);

        }
        else if (!CheckEmail(email)) {
            mRegisterMessageView.setText("Name fields do not match!");
        }
        else if (!CheckPassword(password)) {
            mRegisterMessageView.setText("Password fields do not match!");
        }


    }



    public boolean CheckEmail(String Email) {
        String emailConfirm = mEmailView.getText().toString();
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

        public AsyncTaskRunner(Context ctx, TextView mMessageView){
            this.ctx = ctx;
            this.mMessageView = mMessageView;
        }

        @Override
        protected String doInBackground(String... params) {
                household_member_class user = new household_member_class(params[0], params[1], params[2]);
                ((HouseCutApp)getApplication()).setUser(user);
                if (user.errorMessage() != null) {
                    return user.errorMessage();
                }
                else {
                    return "Registration Success";
                }
            }




    }
    public void backtoLoginPage() {
        Intent intent = new Intent(register_activity.this, login_activity.class);
        startActivity(intent);
    }


}

