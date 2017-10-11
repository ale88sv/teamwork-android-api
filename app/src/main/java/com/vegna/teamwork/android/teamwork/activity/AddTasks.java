package com.vegna.teamwork.android.teamwork.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.adapters.RvSingleTasksAdpater;
import com.vegna.teamwork.android.teamwork.classes.Task;
import com.vegna.teamwork.android.teamwork.classes.Tasklist;
import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;
import com.vegna.teamwork.android.teamwork.helpers.Utils;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;

public class AddTasks extends AppCompatActivity {

    private ArrayList<Task> tasks;
    private Context context;
    private Tasklist tasklist;
    private RvSingleTasksAdpater adapter;
    private RecyclerView rv;
    //deprecated in Android O
    private ProgressDialog progress;
    private TextView noTasksView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        context = getApplicationContext();
        tasks = new ArrayList<>();

        progress = Utils.createProgressDialog(this);
        progress.show();

        //getting the tasklist from the fragment
        tasklist = (Tasklist) getIntent().getSerializableExtra("tasklist");

        //setting the name of the tasklist
        TextView name = (TextView) findViewById(R.id.tasklist_name);
        name.setText(tasklist.getName());
        //setting the description of the tasklist
        TextView desc = (TextView) findViewById(R.id.tasklist_desc);
        LinearLayout descContainer = (LinearLayout) findViewById(R.id.desc_container);

        if(tasklist.getDescription().isEmpty())
            descContainer.setVisibility(View.GONE);
        else
            desc.setText(tasklist.getDescription());

        rv = (RecyclerView) findViewById(R.id.task_list);
        noTasksView = (TextView) findViewById(R.id.no_tasks);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);


        adapter = new RvSingleTasksAdpater(tasks,getApplicationContext(),this);
        rv.setAdapter(adapter);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //send the tasklist to the APIs
                saveTasks();
            }
        });

        //enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateTasks();

    }

    private void updateTasks(){

        CommsLayer.getComms(context).getTasklistTasks(tasklist.getTasklistID()).then(new DoneCallback() {
            @Override
            public void onDone(Object result) {
                if(result != null){
                    tasks.clear();
                    tasks.addAll((ArrayList<Task>)result);
                    adapter.notifyDataSetChanged();
                    if(tasks.size() > 0)
                        showTasks();
                    else
                        showNoTasks();
                }

                progress.dismiss();

            }
        }).fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                Toast.makeText(context,getString(R.string.something_wrong),Toast.LENGTH_LONG).show();
                progress.dismiss();
            }
        });
    }

    private void saveTasks() {
        showProgressBar();

        final String content = Utils.generateTasksForParams(tasks);

        CommsLayer.getComms(context).addTaskToTasklist(tasklist.getTasklistID(),content).then(new DoneCallback() {
            @Override
            public void onDone(Object result) {

                Toast.makeText(context,getString(R.string.task_added),Toast.LENGTH_SHORT).show();
//                dismissProgressBar();
//                finish();
                updateTasks();

            }
        }).fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                Toast.makeText(context,getString(R.string.something_wrong),Toast.LENGTH_SHORT).show();
                dismissProgressBar();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.create_new:
                showInputDialog();
                return true;
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showInputDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.add_task));
        // I'm using fragment here so I'm using getView() to provide ViewGroup
        // but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(context).inflate(R.layout.input_alert, (ViewGroup) findViewById(android.R.id.content), false);
        // Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.task_title);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);

        // Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                addTask(input.getText().toString());
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }



    private void addTask(String single_task){
        if(!single_task.isEmpty())
        {
            tasks.add(0,new Task(single_task));
            adapter.notifyDataSetChanged();
            showTasks();
            showSaveBtn();
        }
    }


    public void showProgressBar(){
        progress.show();
    }

    public void dismissProgressBar(){
        progress.dismiss();
    }

    public void showNoTasks() {
        noTasksView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.GONE);
    }

    public void showTasks() {
        noTasksView.setVisibility(View.GONE);
        rv.setVisibility(View.VISIBLE);
    }

    public void hideSaveBtn() {
        fab.setVisibility(View.GONE);
    }

    public void showSaveBtn() {
        fab.setVisibility(View.VISIBLE);
    }
}
