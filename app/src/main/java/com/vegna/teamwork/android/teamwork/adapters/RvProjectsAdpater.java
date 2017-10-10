package com.vegna.teamwork.android.teamwork.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.vegna.teamwork.android.teamwork.fragments.ProjectDescription;
import com.vegna.teamwork.android.teamwork.fragments.ProjectList;
import com.vegna.teamwork.android.teamwork.helpers.Utils;

import java.util.List;

/**
 * Created by alessandro on 05/10/2017.
 */

public class RvProjectsAdpater extends RecyclerView.Adapter<RvProjectsAdpater.ProjectViewHolder>{
    private List<Project> projects;
    private Context context;
    private ProjectList projectListFragment;


    public RvProjectsAdpater(List<Project> projects, Context context, ProjectList projectList){
        this.projects = projects;
        this.context = context;
        this.projectListFragment = projectList;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i) {
        int color = Utils.getColor(projects.get(i).getStatus().toLowerCase(),context);

        projectViewHolder.status.setText(projects.get(i).getStatus());
        projectViewHolder.status.setBackgroundResource(color);
        projectViewHolder.title.setText(projects.get(i).getName());


        String desc = projects.get(i).getDescription();
        //description is optional
        if(desc != null)
            projectViewHolder.description.setText(desc);

        //loading image
        if(!projects.get(i).getLogo().isEmpty()){
            Picasso.with(context).load(projects.get(i).getLogo()).error(android.R.drawable.stat_notify_error).placeholder(R.drawable.loading).into(projectViewHolder.logo);
        }else{
            projectViewHolder.logo.setImageResource(R.drawable.logo);
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

    class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
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

            itemView.setOnClickListener(this);




        }

        @Override
        public void onClick(View v) {
            Fragment fragment = new ProjectDescription();
            Bundle bundle = new Bundle();
            bundle.putSerializable("project",projects.get(getAdapterPosition()));
            fragment.setArguments(bundle);
            Utils.swapFragments(fragment,projectListFragment);
            /*CommsLayer.getComms(v.getContext()).getProject(projects.get(getAdapterPosition()).getId()).then(new DoneCallback() {
                @Override
                public void onDone(Object result) {

                    Fragment fragment = new ProjectDescription();
                    Bundle args = new Bundle();
                    args.putString("data", "This data has sent to FragmentTwo");
                    fragment.setArguments(args);
                    FragmentTransaction transaction =     projectListFragment.getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment, fragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });*/
        }
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }


}