package com.vegna.teamwork.android.teamwork.helpers;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.vegna.teamwork.android.teamwork.R;

/**
 * Created by ale on 10/8/17.
 */

public class Utils {

    public static void swapFragments(Fragment newFragment, Fragment oldFragment){
        FragmentTransaction transaction =  oldFragment.getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void addFragment(FragmentTransaction transaction, Fragment newFragment){
        transaction.add(R.id.fragment_container, newFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
