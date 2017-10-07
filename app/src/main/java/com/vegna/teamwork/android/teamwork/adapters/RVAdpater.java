package com.vegna.teamwork.android.teamwork.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.classes.Project;

import java.util.List;

/**
 * Created by alessandro on 05/10/2017.
 */

public class RVAdpater extends RecyclerView.Adapter<RVAdpater.ProjectViewHolder>{
    private List<Project> projects;
    private Context context;


    public RVAdpater(List<Project> projects,Context context){
        this.projects = projects;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i) {
        projectViewHolder.status.setText(projects.get(i).getStatus());
        projectViewHolder.title.setText(projects.get(i).getName());
        projectViewHolder.description.setText(projects.get(i).getDescription());
        //loading image
        if(!projects.get(i).getLogo().isEmpty()){
            Picasso.with(context).load(projects.get(i).getLogo()).error(android.R.drawable.stat_notify_error).placeholder(R.drawable.loading).into(projectViewHolder.logo);
        }
    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cards, viewGroup, false);
        ProjectViewHolder pvh = new ProjectViewHolder(v);
        return pvh;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    class ProjectViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        private TextView status;
        private TextView title;
        private TextView description;
        private ImageView logo;


        ProjectViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            status = (TextView)itemView.findViewById(R.id.status);
            title = (TextView)itemView.findViewById(R.id.title);
            description = (TextView)itemView.findViewById(R.id.description);
            logo = (ImageView)itemView.findViewById(R.id.logo);

            //add ellipsize at the end if the text is to long
            description.setEllipsize(TextUtils.TruncateAt.END);



        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


}