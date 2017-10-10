package com.vegna.teamwork.android.teamwork.classes;

import java.io.Serializable;

/**
 * Created by ale on 10/8/17.
 */

public class Task implements Serializable {

    private int id;
    private String content;
    private boolean fromAPI;

    //called by commmslayer
    public Task(){
        fromAPI = true;
    }

    public Task(String content) {
        this.content = content;
        fromAPI = false;
    }

    public boolean getFromApi() { return fromAPI; }

    public int getTasklistID() {
        return id;
    }

    public String getContent() {
        return content;
    }

}
