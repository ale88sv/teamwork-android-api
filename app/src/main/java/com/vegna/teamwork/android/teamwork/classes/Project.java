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
    private ArrayList<Task> tasks;

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

    public void setTasks(ArrayList<Task> tasks){
        if(this.tasks == null){
            this.tasks = new ArrayList<>();
        }
        this.tasks.clear();
        this.tasks.addAll(tasks);
    }

    public ArrayList<Task> getTasks() {
        if(tasks == null){
            tasks = new ArrayList<>();
        }
        return tasks;
    }
}
