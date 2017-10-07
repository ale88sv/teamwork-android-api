package com.vegna.teamwork.android.teamwork.helpers;

/**
 * Created by ale on 10/7/17.
 */

import com.android.volley.VolleyError;

public class CustomException extends Exception {
    public String status;
    public String code;
    public String details;

    public CustomException(String status) {
        this.status = status;
        this.code = status;
        this.details = status;
    }

    public CustomException(Exception e) {
        status = "JSONError";
        details = e.getMessage();
        code = status;
    }

    public CustomException(VolleyError e) {
        if (e.toString().equals("com.android.volley.AuthFailureError")) {
            status = "AuthFailureError";
            details = e.getMessage();
            code = status;
        } else {
            status = "HTTPError";
            details = e.getMessage();
            code = status;
        }
    }
}