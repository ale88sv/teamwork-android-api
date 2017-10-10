package com.vegna.teamwork.android.teamwork.classes;

import java.io.Serializable;

/**
 * Created by ale on 10/10/17.
 */

public class Category implements Serializable{

    private String name;
    private String id;
    private String color;

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getColor() {
        return color;
    }
}
