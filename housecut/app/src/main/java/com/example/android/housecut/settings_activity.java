package com.example.android.housecut;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.AsyncListUtil;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
 * Created by nickjohnson on 11/18/16.
 */

public class settings_activity extends AppCompatActivity {
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);


        Button mLeaveHouseholdButton = (Button) findViewById(R.id.leave_household_button);
        Button mRenameHouseholdButton = (Button) findViewById(R.id.rename_household_button);
        Button mChangeUsernameButton = (Button) findViewById(R.id.change_username_button);
        Button mLogoutButton = (Button) findViewById(R.id.logout_button);

        mChangeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeUsername();
            }
        });

        mLeaveHouseholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeaveHousehold();
            }
        });

        mRenameHouseholdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RenameHousehold();
            }
        });

        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logout();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings_icon:
                intent = new Intent(settings_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(settings_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(settings_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(settings_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void ChangeUsername() {
        Intent intent = new Intent(settings_activity.this, change_username_activity.class);
        startActivity(intent);
    }

    public void RenameHousehold() {
        final LinearLayout renameHouseholdView = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        renameHouseholdView.setLayoutParams(lp);
        renameHouseholdView.setOrientation(LinearLayout.VERTICAL);

        final TextView nameLabel = new TextView(this);
        nameLabel.setText("Household name:");
        final EditText nameView = new EditText(this);
        HouseCutApp app = ((HouseCutApp)this.getApplicationContext());
        Household household = app.getHousehold();
        nameView.setHint(household.getName());

        final TextView errorView = new TextView(this);

        renameHouseholdView.addView(nameLabel);
        renameHouseholdView.addView(nameView);
        renameHouseholdView.addView(errorView);

        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Rename your Household")
                .setView(renameHouseholdView)
                .setPositiveButton("Rename Household", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .create();

        d.show();

        d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                name = nameView.getText().toString();
                if (name == null || name.isEmpty()) {
                    errorView.setText("Enter a new Household name.");
                    return;
                }


                RenameHouseholdRunner renameHouseholdRunner =
                        new RenameHouseholdRunner(getApplicationContext(), d,
                        name, errorView);

                renameHouseholdRunner.execute();
            }
        });
    }

    class RenameHouseholdRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        final private AlertDialog d;
        private String name;
        private TextView errorView;

        public RenameHouseholdRunner(Context ctx, AlertDialog d, String name, TextView errorView){
            this.ctx = ctx;
            this.d = d;
            this.name = name;
            this.errorView = errorView;
        }

        @Override
        protected String doInBackground(String... params) {
            return renameHousehold();
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                d.dismiss();
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                Household household = app.getHousehold();
                household.setName(this.name);
            }
            else {

            }
        }

        public String renameHousehold(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/rename");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject json = new JSONObject();
                json.put("token", token);
                json.put("name", this.name);

                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                out.write(json.toString());
                out.close();


                BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

                StringBuffer result = new StringBuffer();
                String line;
                while ((line = in.readLine()) != null) {
                    System.out.println(result);
                    result.append(line);
                }

                JSONObject data = new JSONObject(String.valueOf(result));

                System.out.println(data);

                Boolean success = data.getBoolean("success");

                String responseString;

                if (success) {
                    System.out.println("Rename household success\n");
                    responseString = "success";
                }
                else {
                    System.out.println("Rename household failure\n");
                    String message = data.getString("message");
                    System.out.println(message + "\n");
                    responseString = data.getString("message");
                    final String err = responseString;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            errorView.setText(err);
                        }
                    });

                }

                in.close();
                conn.disconnect();

                return responseString;

            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return "Failure";
        }
    }

    public void LeaveHousehold() {
        Intent intent = new Intent(settings_activity.this, leave_household_activity.class);
        startActivity(intent);
    }

    public void Logout() {
        HouseCutApp app = ((HouseCutApp) this.getApplicationContext());
        app.setUser(null);
        app.setHousehold(null);
        Intent intent = new Intent(settings_activity.this, login_activity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("settings_activity Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
