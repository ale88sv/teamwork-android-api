package com.vegna.teamwork.android.teamwork.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.activity.AddTasks;
import com.vegna.teamwork.android.teamwork.classes.Task;
import com.vegna.teamwork.android.teamwork.fragments.ProjectDescription;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by alessandro on 05/10/2017.
 */

public class RvAddTasksAdpater extends RecyclerView.Adapter<RvAddTasksAdpater.TaskViewHolder>{
    private List<String> tasks;
    private Context context;
    private AddTasks addTasks;

    public RvAddTasksAdpater(List<String> tasks, Context context,AddTasks addTasks){
        this.tasks = tasks;
        this.context = context;
        this.addTasks = addTasks;

    }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.title.setText(tasks.get(i));
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_task_insert, viewGroup, false);
        TaskViewHolder pvh = new TaskViewHolder(v);
        return pvh;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        private TextView title;
        private ImageView imageView;


        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.text);
            imageView = (ImageView) itemView.findViewById(R.id.delete);


            //cannot use implements View.OnClickListener due to a bug on the nestedview
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tasks.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if(tasks.isEmpty())
                        addTasks.showNoTasks();
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }





}