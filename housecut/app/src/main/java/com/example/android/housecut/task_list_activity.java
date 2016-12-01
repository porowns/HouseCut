package com.example.android.housecut;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by Chris on 11/26/2016.
 */

public class task_list_activity extends AppCompatActivity {
    private Button createTaskButton;
    private Button viewingButton;
    private TextView loadingTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);
        loadingTextView= (TextView) findViewById(R.id.task_list_loading);
        loadingTextView.setVisibility(View.VISIBLE);

        viewingButton = (Button) findViewById(R.id.task_list_viewing);
        viewingButton.setVisibility(View.GONE);
        final Context finalCtx = this;
        viewingButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {


                HouseCutApp app = ((HouseCutApp)finalCtx.getApplicationContext());
                household_member_class user = app.getUser();
                final Household household = app.getHousehold();
                HashMap<String, String> roommates = household.getRoommatesHashMap();
                ArrayList<String> roommateNames = new ArrayList<>(100);
                roommateNames.add("Household");
                Set set = roommates.entrySet();
                Iterator i = set.iterator();
                while (i.hasNext()) {
                    Map.Entry rm = (Map.Entry) i.next();
                    roommateNames.add(rm.getValue().toString());
                }
                final CharSequence options[] =
                        roommateNames.toArray(new CharSequence[roommateNames.size()]);

                AlertDialog.Builder builder = new AlertDialog.Builder(finalCtx);
                builder.setTitle("View tasks assigned to...");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("clicked: " + options[which]);
                        String id;
                        if (which == 0) {
                            id = "";
                        }
                        else {
                            id = household.getRoommateIdFromName(options[which].toString());
                        }
                        Intent intent = new Intent(finalCtx, task_list_activity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                    }
                });
                builder.show();
            }
        });

        createTaskButton = (Button) findViewById(R.id.create_task_button);
        createTaskButton.setVisibility(View.GONE);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                createTaskDialog();
            }
        });

        String id = getIntent().getStringExtra("id");
        String name = "Household";
        System.out.println("Id: " + id);
        if (id == null || id.isEmpty()) {
            id = "";
        }
        else {
            HouseCutApp app = ((HouseCutApp) this.getApplicationContext());
            Household household = app.getHousehold();
            name = household.getRoommateNameFromId(id);
        }
        viewingButton.setText("Viewing: " + name);

        GetTasklistRunner getTasklistRunner = new GetTasklistRunner(this, id);

        getTasklistRunner.execute();


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void createTaskDialog() {

        final LinearLayout createTaskView = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        createTaskView.setLayoutParams(lp);
        createTaskView.setOrientation(LinearLayout.VERTICAL);

        final TextView nameLabel = new TextView(this);
        nameLabel.setText("Task name");
        final EditText nameView = new EditText(this);


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
            System.out.println("roommate: " + rm.getValue().toString());
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

        final TextView errorView = new TextView(this);

        createTaskView.addView(nameLabel);
        createTaskView.addView(nameView);
        createTaskView.addView(typeLabel);
        createTaskView.addView(typeGroup);
        createTaskView.addView(assignedLabel);
        createTaskView.addView(assignedGroup);
        createTaskView.addView(errorView);

        assignedLabel.setVisibility(View.GONE);
        assignedGroup.setVisibility(View.GONE);

        nameView.setHint("Task name");

        final AlertDialog d = new AlertDialog.Builder(this)
                .setTitle("Create a new task")
                .setView(createTaskView)
                .setPositiveButton("Create Task", null)
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
                    errorView.setText("Enter a task name.");
                    return;
                }
                String type;
                if (typeRb[0].isChecked()) {
                    type = "Rotating";
                } else if (typeRb[1].isChecked()) {
                    type = "Assigned";
                } else if (typeRb[2].isChecked()) {
                    type = "Voluntary";
                } else {
                    errorView.setText("Select a task type.");
                    return;
                }

                int assignedRbId = assignedGroup.getCheckedRadioButtonId();
                String selectedRoommateId = roommateIds.get(assignedRbId);
                if (selectedRoommateId == null) {
                    if (type.equals("Rotating") || type.equals("Assigned")) {
                        errorView.setText("Assign this task to a user.");
                        return;
                    }
                }

                /* Make POST request */

                CreateTaskRunner createTaskRunner = new CreateTaskRunner(getApplicationContext(), d,
                        name, type, selectedRoommateId, errorView);

                createTaskRunner.execute();
            }
        });

    }

    class GetTasklistRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        private ArrayList<Task> tasks;
        private String id;

        public GetTasklistRunner(Context ctx, String id){
            this.ctx = ctx;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            return getTasklist();
        }

        @Override
        protected void onPostExecute(String responseString) {
            loadingTextView.setVisibility(View.GONE);
            createTaskButton.setVisibility(View.VISIBLE);
            viewingButton.setVisibility(View.VISIBLE);

            if (responseString.equals("success")) {
                TaskAdapter adapter = new TaskAdapter(task_list_activity.this, this.tasks);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);
                final Context finalCtx = this.ctx;
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        final Task selectedTask = tasks.get(position);
                        final String completeTaskString = "Complete task";
                        final String deleteTaskString = "Delete task";
                        CharSequence options[];
                        HouseCutApp app = ((HouseCutApp)finalCtx.getApplicationContext());
                        household_member_class user = app.getUser();
                        if (selectedTask.type.equals("Voluntary") ||
                                selectedTask.currentlyAssignedId.equals(user.getID())) {
                            options = new String[2];
                            options[0] = completeTaskString;
                            options[1] = deleteTaskString;
                        }
                        else {
                            options = new String[1];
                            options[0] = deleteTaskString;
                        }
                        final CharSequence finalOptions[] = options;
                        AlertDialog.Builder builder = new AlertDialog.Builder(finalCtx);
                        builder.setTitle("Task: " + selectedTask.name);
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (finalOptions[which].equals(completeTaskString)) {
                                    CompleteTaskRunner completeTaskRunner =
                                            new CompleteTaskRunner(getApplicationContext(),
                                                    selectedTask.name);

                                    completeTaskRunner.execute();
                                }
                                else if (finalOptions[which].equals(deleteTaskString)){
                                    DeleteTaskRunner deleteTaskRunner =
                                            new DeleteTaskRunner(getApplicationContext(),
                                                    selectedTask.name);

                                    deleteTaskRunner.execute();
                                }
                            }
                        });
                        builder.show();
                    }

                });

            }
            else {

            }
        }

        public String getTasklist(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                Household household = app.getHousehold();
                String token = user.getToken();

                String urlString = "http://10.0.2.2:8080/household/tasklist?token=" + token;
                if (!this.id.isEmpty()) {
                    urlString += "&userId=" + this.id;
                }
                URL url = new URL (urlString);

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
                    System.out.println("Get tasklist success\n");
                    JSONArray tasklist = data.getJSONArray("tasklist");
                    tasks = new ArrayList<Task>(tasklist.length());
                    for (int i = 0; i < tasklist.length(); i++) {
                        JSONObject taskJSON = tasklist.getJSONObject(i);
                        String name = taskJSON.getString("name");
                        String type = taskJSON.getString("type");
                        String currentlyAssignedId = taskJSON.getString("currentlyAssigned");
                        String currentlyAssignedName = household.getRoommateNameFromId(currentlyAssignedId);
                        tasks.add(new Task(name, type, currentlyAssignedId, currentlyAssignedName));
                    }
                    responseString = "success";
                }
                else {
                    System.out.println("Get tasklist failure\n");
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

    class CreateTaskRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        final private AlertDialog d;
        private String taskName;
        private String taskType;
        private String currentlyAssigned;
        private TextView errorView;

        public CreateTaskRunner(Context ctx, AlertDialog d, String name,
                                String type, String assigned, TextView errorView){
            this.ctx = ctx;
            this.d = d;
            this.taskName = name;
            this.taskType = type;
            this.currentlyAssigned = assigned;
            this.errorView = errorView;
        }

        @Override
        protected String doInBackground(String... params) {
            return createTask();
        }

        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                d.dismiss();
                task_list_activity.this.recreate();
            }
        }

        public String createTask(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/createtask");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject json = new JSONObject();
                json.put("token", token);
                json.put("name", this.taskName);
                json.put("type", this.taskType);
                json.put("currentlyAssigned", this.currentlyAssigned);
                json.put("recurring", false);

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
                    System.out.println("Create task success\n");
                    responseString = "success";
                }
                else {
                    System.out.println("Create task failure\n");
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

    class DeleteTaskRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        private String taskName;

        public DeleteTaskRunner(Context ctx, String name){
            this.ctx = ctx;
            this.taskName = name;
        }

        @Override
        protected String doInBackground(String... params) {
            return deleteTask();
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                task_list_activity.this.recreate();
            }
        }

        public String deleteTask(){
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/deletetask");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject json = new JSONObject();
                json.put("token", token);
                json.put("taskName", this.taskName);

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
                    System.out.println("Delete task success\n");
                    responseString = "success";
                }
                else {
                    System.out.println("Delete task failure\n");
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

    class CompleteTaskRunner extends AsyncTask<String, Void, String> {

        private Context ctx;
        private String taskName;

        public CompleteTaskRunner(Context ctx, String name){
            this.ctx = ctx;
            this.taskName = name;
        }

        @Override
        protected String doInBackground(String... params) {
            return completeTask();
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onPostExecute(String responseString) {
            if (responseString.equals("success")) {
                task_list_activity.this.recreate();
            }
        }

        public String completeTask(){
            System.out.println("Complete task running..");
            try {
                HouseCutApp app = ((HouseCutApp)this.ctx.getApplicationContext());
                household_member_class user = app.getUser();
                String token = user.getToken();

                URL url = new URL ("http://10.0.2.2:8080/household/completetask");

                //Declare connection object
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");

                JSONObject json = new JSONObject();
                json.put("token", token);
                json.put("name", this.taskName);

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
                    System.out.println("Complete task success\n");
                    responseString = "success";
                }
                else {
                    System.out.println("Complete task failure\n");
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
                intent = new Intent(task_list_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(task_list_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(task_list_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(task_list_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
