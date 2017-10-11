package com.vegna.teamwork.android.teamwork.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.fragments.ProjectList;
import com.vegna.teamwork.android.teamwork.helpers.Utils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        //add the Fragment to the stack
        Utils.addFragment(getSupportFragmentManager().beginTransaction(),new ProjectList());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
