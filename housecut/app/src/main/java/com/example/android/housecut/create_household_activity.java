package com.example.android.housecut;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jose on 11/21/2016.
 *
 * Edited by Chris on 11/22/2016.
 */

public class create_household_activity extends AppCompatActivity {

    // UI references.
    private EditText mNameConfirmView;
    private EditText mPasswordConfirmView;
    private EditText mNameView;
    private EditText mPasswordView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_household);

        mNameConfirmView = (EditText) findViewById(R.id.nameConfirm);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);
        mNameView = (EditText) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String password = intent.getStringExtra("password");
        if (name != null && !name.isEmpty()) {
            mNameView.setText(name);
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

    }

    public void CreateHouseholdButton() {
        String name = mNameConfirmView.getText().toString();
        String password = mPasswordConfirmView.getText().toString();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute(name, password);


    }

    public boolean ValidInput(String name, String password) {
        if ((!CheckName(name)) || (!CheckPassword(password)))
            return false;

        else
            return true;
    }

    public boolean CheckName(String Email) {
        String emailConfirm = mNameView.getText().toString();
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
                /* TODO: add create household server call here */
                return "Create Household success";
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
    public void backToLoginPage() {
        Intent intent = new Intent(create_household_activity.this, main_no_household_activity.class);
        System.out.println(create_household_activity.this.CheckName("test"));
        startActivity(intent);
    }


}

