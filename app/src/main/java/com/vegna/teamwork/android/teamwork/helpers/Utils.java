package com.vegna.teamwork.android.teamwork.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.vegna.teamwork.android.teamwork.R;
import com.vegna.teamwork.android.teamwork.classes.Task;

import java.util.ArrayList;

/**
 * Created by ale on 10/8/17.
 */

public class Utils {

    public static void swapFragments(Fragment newFragment, Fragment oldFragment){
        FragmentTransaction transaction =  oldFragment.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_left,R.anim.enter_from_left, R.anim.exit_to_left);
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addFragment(FragmentTransaction transaction, Fragment newFragment){
        transaction.add(R.id.fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
    }

    public static String generateTasksForParams(ArrayList<Task> tasks){
        String content = "";
        for (Task task : tasks){
            if(!task.getContent().isEmpty() && !task.getFromApi())
                content += task.getContent()+"\n";
        }

        return content;
    }

    public static ProgressDialog createProgressDialog(Context context){
        ProgressDialog progress = new ProgressDialog(context);
        //progress.setTitle(context.getString(R.string.loading));
        progress.setMessage(context.getString(R.string.loading));
        progress.setCancelable(false);

        return progress;
    }


    public static int getColor(String s,Context context) {
        int color;
        switch (s){
            case "active":
                color = R.color.active;
                break;
            case "late":
                color = R.color.late;
                break;
            case "done":
                color = R.color.done;
                break;
            case "current":
                color = R.color.current;
                break;
            case "completed":
                color = R.color.completed;
                break;
            case "archived":
                color = R.color.archived;
                break;
            default:
                color = R.color.active;
        }
        return color;
    }
}
