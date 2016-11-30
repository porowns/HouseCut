package com.example.android.housecut;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nickjohnson on 11/18/16.
 * edited by jose and chris
 */

public class grocery_list_activity extends AppCompatActivity {
    private Button addItemButton;
    private Button viewingButton;
    private TextView loadingTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);

        loadingTextView= (TextView) findViewById(R.id.grocery_list_loading);
        loadingTextView.setVisibility(View.VISIBLE);

        viewingButton = (Button) findViewById(R.id.grocery_list_viewing);
        viewingButton.setVisibility(View.GONE);

        addItemButton = (Button) findViewById(R.id.add_item_button);
        addItemButton.setVisibility(View.GONE);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                addItemDialog();
            }
        });

        GetGroceryListRunner getGroceryListRunner = new GetGroceryListRunner(this);

        getGroceryListRunner.execute();
    }


        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void addItemDialog() {

            final LinearLayout addItemView = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            addItemView.setLayoutParams(lp);
            addItemView.setOrientation(LinearLayout.VERTICAL);

            final TextView nameLabel = new TextView(this);
            nameLabel.setText("Item name");
            final EditText nameView = new EditText(this);

            /*
            final TextView typeLabel = new TextView(this);
            typeLabel.setText("Task type");
            typeLabel.setLayoutParams(lp);
            final RadioButton[] typeRb = new RadioButton[5];
            RadioGroup typeGroup = new RadioGroup(this);
            typeGroup.setOrientation(RadioGroup.HORIZONTAL);
            for (int i = 0; i < 3; i++) {
                typeRb[i] = new RadioButton(this);
                typeGroup.addView(typeRb[i]);
            }
            typeRb[0].setText("Rotating");
            typeRb[1].setText("Assigned");
            typeRb[2].setText("Voluntary");

            final TextView assignedLabel = new TextView(this);
            final RadioButton[] assignedRb = new RadioButton[5];
            final RadioGroup assignedGroup = new RadioGroup(this);
            assignedGroup.setOrientation(RadioGroup.HORIZONTAL);

            HouseCutApp app = ((HouseCutApp) this.getApplicationContext());
            Household household = app.getHousehold();
            HashMap<String, String> roommates = household.getRoommatesHashMap();
            final HashMap<Integer, String> roommateIds = new HashMap<>();
            int roommateNum = 0;

            Set set = roommates.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry rm = (Map.Entry) i.next();
                assignedRb[roommateNum] = new RadioButton(this);
                assignedRb[roommateNum].setText(rm.getValue().toString());
                assignedGroup.addView(assignedRb[roommateNum]);
                int id = View.generateViewId();
                assignedRb[roommateNum].setId(id);
                roommateIds.put(id, rm.getKey().toString());
                roommateNum++;
            }

            final int totalRoommates = roommateNum;

            typeRb[0].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    assignedLabel.setVisibility(View.VISIBLE);
                    assignedGroup.setVisibility(View.VISIBLE);
                    assignedLabel.setText("Assign to (first):");
                }
            });
            typeRb[1].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    assignedLabel.setVisibility(View.VISIBLE);
                    assignedGroup.setVisibility(View.VISIBLE);
                    assignedLabel.setText("Assign to:");
                }
            });
            typeRb[2].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    assignedLabel.setVisibility(View.GONE);
                    assignedGroup.setVisibility(View.GONE);
                }
            });
                */
            addItemView.addView(nameLabel);
            addItemView.addView(nameView);

            /*
            addItemView.addView(typeLabel);
            addItemView.addView(typeGroup);
            addItemView.addView(assignedLabel);
            addItemView.addView(assignedGroup);

            assignedLabel.setVisibility(View.GONE);
            assignedGroup.setVisibility(View.GONE);
            */
            nameView.setHint("Item name");

            final AlertDialog d = new AlertDialog.Builder(this)
                    .setTitle("Create a new task")
                    .setView(addItemView)
                    .setPositiveButton("Add Item", null)
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
                    if (name.isEmpty()) {
                        return;
                    }

                /* Make POST request */


                    //CreateTaskRunner createTaskRunner = new CreateTaskRunner(getApplicationContext(), d,
                    //        name, type, selectedRoommateId);

                    //createTaskRunner.execute();
                }
            });

        }

        class GetGroceryListRunner extends AsyncTask<String, Void, String> {

            private Context ctx;
            ArrayList<Grocery> groceries;

            public GetGroceryListRunner(Context ctx){
                this.ctx = ctx;
            }

            @Override
            protected String doInBackground(String... params) {
                return getGroceryList();
            }

            @Override
            protected void onPostExecute(String responseString) {
                loadingTextView.setVisibility(View.GONE);
                addItemButton.setVisibility(View.VISIBLE);
                viewingButton.setVisibility(View.VISIBLE);

                if (responseString.equals("success")) {

                    GroceryAdapter adapter = new GroceryAdapter(com.example.android.housecut.grocery_list_activity.this, this.groceries);
                    ListView listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(adapter);

                }
                else {

                }
            }

            public String getGroceryList(){
                try {
                    HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                    household_member_class user = app.getUser();
                    Household household = app.getHousehold();
                    String token = user.getToken();

                    URL url = new URL ("http://10.0.2.2:8080/household/getgrocerylist?token=" + token);

                    //Declare connection object
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("GET");

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
                        System.out.println("Get grocerylist success\n");
                        JSONArray grocerylist = data.getJSONArray("grocerylist");
                        groceries = new ArrayList<>(grocerylist.length());
                        for (int i = 0; i < grocerylist.length(); i++) {
                            JSONObject groceryJSON = grocerylist.getJSONObject(i);
                            String name = groceryJSON.getString("name");
                            groceries.add(new Grocery(name));
                        }
                        responseString = "success";
                    }
                    else {
                        System.out.println("Get grocerylist failure\n");
                        String message = data.getString("message");
                        System.out.println(message + "\n");
                        responseString = data.getString("message");
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


        //TODO: Still works on the Task list URL, needs to be switched over 
        class GroceryListRunner extends AsyncTask<String, Void, String> {

            private Context ctx;
            final private AlertDialog d;
            private String itemName;

            public GroceryListRunner(Context ctx, AlertDialog d, String name){
                this.ctx = ctx;
                this.d = d;
                this.itemName = name;
            }

            @Override
            protected String doInBackground(String... params) {
                return createGroceryItem();
            }

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            protected void onPostExecute(String responseString) {
                if (responseString.equals("success")) {
                    d.dismiss();
                    com.example.android.housecut.grocery_list_activity.this.recreate();
                }
            }

            public String createGroceryItem(){
                try {
                    HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                    household_member_class user = app.getUser();
                    String token = user.getToken();

                    URL url = new URL ("http://10.0.2.2:8080/household/creategrocery");

                    //Declare connection object
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setDoOutput(true);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/json");

                    JSONObject json = new JSONObject();
                    json.put("token", token);
                    json.put("name", this.itemName);

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
                        System.out.println("Create grocery item success\n");
                        responseString = "success";
                    }
                    else {
                        System.out.println("Create grocery failure\n");
                        String message = data.getString("message");
                        System.out.println(message + "\n");
                        responseString = data.getString("message");
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
                intent = new Intent(grocery_list_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(grocery_list_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(grocery_list_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(grocery_list_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
