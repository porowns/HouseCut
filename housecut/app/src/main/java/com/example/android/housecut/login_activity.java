package com.example.android.housecut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;






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

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                attemptLogin();
                return true;
            }
        });

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
        Intent intent = new Intent(login_activity.this, login_confirmation_activity.class);
        startActivity(intent);
    }



}