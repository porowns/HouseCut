package com.example.android.housecut;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.housecut.HouseCutApp;

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
    private EditText mEmailView;
    private EditText mPasswordView;
    private ShowPopUpWindow mRegisterPopup;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        mUsernameView = (EditText) findViewById(R.id.username);
        mEmailConfirmView = (EditText) findViewById(R.id.emailConfirm);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_user_button);


        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterButton();
            }
        });

    }

    public void RegisterButton() {
        String email = mEmailConfirmView.getText().toString();
        String password = mPasswordConfirmView.getText().toString();
        String username = mUsernameView.getText().toString();



        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(username, email, password);


    }

    public boolean ValidInput(String email, String password) {
        if ((!CheckEmail(email)) || (!CheckPassword(password)))
            return false;

        else
            return true;


    }

    public boolean CheckEmail(String Email) {
        String emailConfirm = mEmailView.getText().toString();
        if(emailConfirm == Email)
            return true;
        else
            return false;
    }
    public boolean CheckPassword(String Pass) {
        String passConfirm = mPasswordView.getText().toString();
        if(passConfirm == Pass)
            return true;
        else
            return false;
    }


    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            if(ValidInput(params[1], params[2])) {
                household_member_class user = new household_member_class(params[0], params[1], params[2]);
                ((HouseCutApp)getApplication()).setUser(user);
                if (user.errorMessage() != null) {
                    return user.errorMessage();
                }
                else {
                    backtoLoginPage();
                    return "Registration Success";
                }
            }
            else
                return "Invalid Input";
        }

        /*
        @Override
        protected void onPostExecute(String errorMessage){
            ShowPopUpWindow message = null;
            setContentView(register_message) = ;

        }
        */
    }
    public void backtoLoginPage() {
        Intent intent = new Intent(register_activity.this, login_activity.class);
        startActivity(intent);
    }


}

