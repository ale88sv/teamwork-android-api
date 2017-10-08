package com.vegna.teamwork.android.teamwork.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.vegna.teamwork.android.teamwork.R;

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

    public static String generateTasksForParams(ArrayList<String> tasks){
        String content = "";
        for (String task : tasks){
            if(!task.isEmpty())
                content += task+"\n";
        }

        return content;
    }


}
