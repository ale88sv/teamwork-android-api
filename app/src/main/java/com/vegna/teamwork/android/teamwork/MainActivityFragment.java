package com.vegna.teamwork.android.teamwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vegna.teamwork.android.teamwork.helpers.CommsLayer;

import org.jdeferred.DoneCallback;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView textView = (TextView) v.findViewById(R.id.text);

        textView.setText("test");


        CommsLayer commsLayer = CommsLayer.getComms(getContext());

        commsLayer.getProjects().then(new DoneCallback() {
            @Override
            public void onDone(Object result) {
                textView.setText(result.toString());

            }
        });


        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
