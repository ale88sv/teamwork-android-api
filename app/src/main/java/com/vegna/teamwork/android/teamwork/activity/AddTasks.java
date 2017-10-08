package com.vegna.teamwork.android.teamwork.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;
import com.vegna.teamwork.android.teamwork.helpers.Utils;

import org.jdeferred.DoneCallback;

import java.util.ArrayList;

public class AddTasks extends AppCompatActivity {

    private ArrayList<String> tasks;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        context = getApplicationContext();

        tasks = new ArrayList<>();
        tasks.add("task 1");
        tasks.add("task 2");
        tasks.add("task 3");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String content = Utils.generateTasksForParams(tasks);

                //todo remove this
                CommsLayer.getComms(context).addTaskToTasklist(958199,content).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {

                    }
                });

//                AlertDialog.Builder alert = new AlertDialog.Builder(AddTasks.this);
//
//                View viewInflated = LayoutInflater.from(AddTasks.this).inflate(R.layout.input_alert,(ViewGroup) findViewById(android.R.id.content));
//                // Set up the input
//                final EditText edittext = (EditText) viewInflated.findViewById(R.id.task_title);
//                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
//
//
//                alert.setMessage(R.string.task_alert_title);
//                alert.setTitle(R.string.task_alert_desc);
//
//                alert.setView(viewInflated);
//
//
//                alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        //OR
//                        String YouEditTextValue = edittext.getText().toString();
//                    }
//                });
//
//                alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // what ever you want to do with No option.
//                    }
//                });
//
//
//                alert.show();
            }
        });


    }


}
