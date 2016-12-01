package com.example.android.housecut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by nickjohnson on 11/29/16.
 */

public class change_username_activity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username);
        Toolbar optionsToolbar = (Toolbar) findViewById(R.id.options_toolbar);
        setSupportActionBar(optionsToolbar);
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
                intent = new Intent(change_username_activity.this, settings_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.grocery_list_icon:
                intent = new Intent(change_username_activity.this, grocery_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.task_list_icon:
                intent = new Intent(change_username_activity.this, task_list_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            case R.id.home_page_icon:
                intent = new Intent(change_username_activity.this, main_page_activity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
