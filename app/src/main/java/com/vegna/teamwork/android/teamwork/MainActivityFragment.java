package com.vegna.teamwork.android.teamwork;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vegna.teamwork.android.teamwork.adapters.RVAdpater;
import com.vegna.teamwork.android.teamwork.classes.Project;
import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<Project> projects;
    private CommsLayer commsLayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RVAdpater adapter;
    private Context context;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_main, container, false);

        context =  v.getContext();
        commsLayer = CommsLayer.getComms(context);
        projects = new ArrayList<>();



        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);

        rv = (RecyclerView)v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);

        adapter = new RVAdpater(projects,context);
        rv.setAdapter(adapter);
//        rv.setItemAnimator(new ScaleUpAnimator());
//        rv.getItemAnimator().setAddDuration(1000);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProjects();
            }
        });


      //  getProjects();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    private void getProjects(){
        commsLayer.getProjects().then(new DoneCallback() {
            @Override
            public void onDone(Object result) {
                //I need a tmp array in order to not lose the reference to the originals projects
                ArrayList<Project> tmp;
                if(result != null){
                    tmp = (ArrayList<Project>) result;
                    projects.clear();
                    projects.addAll(tmp);
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        }).fail(new FailCallback() {
            @Override
            public void onFail(Object result) {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                projects.clear();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

            }
        });
    }
}
