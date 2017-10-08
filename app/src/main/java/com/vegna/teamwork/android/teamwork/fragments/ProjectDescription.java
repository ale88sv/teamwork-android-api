package com.vegna.teamwork.android.teamwork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.adapters.RvTasksAdpater;
import com.vegna.teamwork.android.teamwork.classes.Project;
import com.vegna.teamwork.android.teamwork.classes.Task;
import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;

import org.jdeferred.DoneCallback;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProjectDescription extends Fragment {

    private CommsLayer commsLayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RvTasksAdpater adapter;
    private Context context;
    private RecyclerView rv;
    private Project project;
    private TextView tv;
    private TextView status;
    private TextView title;
    private TextView description;
    private ImageView logo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_project_description, container, false);
        project = (Project) getArguments().getSerializable("project");

        status = (TextView)v.findViewById(R.id.status);
        title = (TextView)v.findViewById(R.id.title);
        description = (TextView)v.findViewById(R.id.description);
        //remove the max-lines from the previous layout
        description.setMaxLines(Integer.MAX_VALUE);

        logo = (ImageView)v.findViewById(R.id.logo);

        context =  v.getContext();
        commsLayer = CommsLayer.getComms(context);

        rv = (RecyclerView)v.findViewById(R.id.rv_tasks);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };


        rv.setLayoutManager(llm);

        adapter = new RvTasksAdpater(project.getTasks(),context);
        rv.setAdapter(adapter);

        CommsLayer.getComms(context).getProjectTasks(project.getId()).then(new DoneCallback() {
            @Override
            public void onDone(Object result) {
                if(result != null){
                    ArrayList<Task> tasks= (ArrayList<Task>) result;
                    project.setTasks(tasks);
                    adapter.notifyDataSetChanged();
                }else{
                    Log.e("error","error");
                }
            }
        });

        updateDetails();

        return v;
    }

    private void updateDetails() {

        status.setText(project.getStatus());
        title.setText(project.getName());

        String desc = project.getDescription();
        //description is optional
        if(desc != null)
            description.setText(desc);



        //loading image
        if(!project.getLogo().isEmpty()){
            Picasso.with(context).load(project.getLogo()).error(android.R.drawable.stat_notify_error).placeholder(R.drawable.loading).into(logo);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
