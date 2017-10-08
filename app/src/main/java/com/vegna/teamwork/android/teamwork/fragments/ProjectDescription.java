package com.vegna.teamwork.android.teamwork.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.adapters.RVAdpater;
import com.vegna.teamwork.android.teamwork.classes.Project;
import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProjectDescription extends Fragment {

    private ArrayList<Project> projects;
    private CommsLayer commsLayer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RVAdpater adapter;
    private Context context;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_project_description, container, false);

        context =  v.getContext();
        commsLayer = CommsLayer.getComms(context);
        projects = new ArrayList<>();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
