package com.vegna.teamwork.android.teamwork.classes;

import java.io.Serializable;

/**
 * Created by ale on 10/8/17.
 */

public class Tasklist implements Serializable {

    private int id;
    private String name;
    private String description;

    public int getTasklistID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
