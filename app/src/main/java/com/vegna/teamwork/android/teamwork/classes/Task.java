package com.vegna.teamwork.android.teamwork.classes;

import java.io.Serializable;

/**
 * Created by ale on 10/8/17.
 */

public class Task implements Serializable {

    private int projectid;
    private String name;
    private String description;

    public int getProjectid() {
        return projectid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
