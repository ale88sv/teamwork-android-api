package com.vegna.teamwork.android.teamwork.classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ale on 10/7/17.
 */

public class Project implements Serializable {

    private int id;
    private String name;
    private String description;
    private String logo;
    private String status;
    private ArrayList<Tasklist> tasklists;
    private Category category;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public String getStatus() {
        return status;
    }

    public void setTasklists(ArrayList<Tasklist> tasklists){
        if(this.tasklists == null){
            this.tasklists = new ArrayList<>();
        }
        this.tasklists.clear();
        this.tasklists.addAll(tasklists);
    }

    public ArrayList<Tasklist> getTasklists() {
        if(tasklists == null){
            tasklists = new ArrayList<>();
        }
        return tasklists;
    }

    public Category getCategory() {
        return category;
    }
}
