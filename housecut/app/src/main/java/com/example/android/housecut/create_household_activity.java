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


    }



}

