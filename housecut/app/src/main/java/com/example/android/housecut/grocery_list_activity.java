package com.example.android.housecut;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by nickjohnson on 11/18/16.
 */

public class grocery_list_activity extends AppCompatActivity {
    private Button createTaskButton;
    private Button viewingButton;
    private TextView loadingTextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery_list);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);

        loadingTextView= (TextView) findViewById(R.id.task_list_loading);
        loadingTextView.setVisibility(View.VISIBLE);

        viewingButton = (Button) findViewById(R.id.task_list_viewing);
        viewingButton.setVisibility(View.GONE);

        createTaskButton = (Button) findViewById(R.id.create_task_button);
        createTaskButton.setVisibility(View.GONE);
        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View view) {
                
            }
        });
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
