package com.vegna.teamwork.android.teamwork.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.activity.AddTasks;
import com.vegna.teamwork.android.teamwork.classes.Tasklist;

import java.util.List;


/**
 * Created by alessandro on 05/10/2017.
 */

public class RvTasksListAdpater extends RecyclerView.Adapter<RvTasksListAdpater.TaskViewHolder>{
    private List<Tasklist> tasklists;
    private Context context;

    public RvTasksListAdpater(List<Tasklist> tasklists, Context context){
        this.tasklists = tasklists;
        this.context = context;
     }

    @Override
    public void onBindViewHolder(TaskViewHolder taskViewHolder, int i) {
        taskViewHolder.title.setText(tasklists.get(i).getName());
        String desc = tasklists.get(i).getDescription();
        //description is optional
        if(desc != null && !desc.isEmpty())
            taskViewHolder.description.setText(desc);
        else
        {
            taskViewHolder.description.setTypeface(null, Typeface.ITALIC);
            taskViewHolder.description.setText(context.getString(R.string.no_description));
        }

    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards_task, viewGroup, false);
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
        private TextView description;


        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);

            //cannot use implements View.OnClickListener due to a bug on the nestedview
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AddTasks.class);
                    intent.putExtra("tasklist", tasklists.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }



    }

    @Override
    public int getItemCount() {
        return tasklists.size();
    }





}