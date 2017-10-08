package com.vegna.teamwork.android.teamwork.helpers;

/**
 * Created by ale on 10/7/17.
 */


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vegna.teamwork.android.teamwork.classes.Project;
import com.vegna.teamwork.android.teamwork.classes.Task;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CommsLayer {

    private static final String API_TOKEN = "twp_TEbBXGCnvl2HfvXWfkLUlzx92e3T";
    private static final String BASE_URL = "https://yat.teamwork.com";
    private static final String GET_PROJECTS = "/projects.json";
    private static final String GET_PROJECT = "/projects/%d.json";
    private static final String GET_PROJECT_TASK_LIST = "/projects/%d/tasklists.json";
    private static final String ADD_TASKS_TO_TASK_LIST = "/tasklists/%d/quickadd.json";
    private static final String USERNAME = "yat@triplespin.com";
    private static final String PASSWORD = "yatyatyat27";

    private static CommsLayer commsLayer;
    private RequestQueue queue;
    private String basicToken;


    public static CommsLayer getComms(Context c) {
        if (commsLayer == null) {
            commsLayer = new CommsLayer();
            commsLayer.init(c);
        }

        return commsLayer;
    }

    private void init(Context c) {
        queue = Volley.newRequestQueue(c);
        basicToken = createBasicToken();
    }

    private String createBasicToken() {
        String basicToken = USERNAME + ":" + PASSWORD;
        return Base64.encodeToString(basicToken.getBytes(), Base64.NO_WRAP);
    }

    public CustomPromise makeRequestForPathWithAuth(final String endPoint, final JSONObject params, String accessToken) {
        return makeRequestForPath(endPoint, params, accessToken);
    }

    private CustomPromise makeRequestForPath(final String endPoint, final JSONObject params) {
        return makeRequestForPath(endPoint, params, null);
    }

    private CustomPromise makeRequestForPath(final String endPoint, final JSONObject params, final String accessToken) {
        return new CustomPromise() {
            @Override
            public void execute() {
                final String path = BASE_URL + endPoint;
                //https://stackoverflow.com/questions/22428343/android-volley-double-post-when-have-slow-request
                System.setProperty("http.keepAlive", "false");
                if (params != null)
                    Log.e("VolleyRequest", "POST - Calling:" + path + " Params:" + params.toString());
                else
                    Log.e("VolleyRequest", "GET  - Calling:" + path);

                JsonObjectRequest jsonRequest = new JsonObjectRequest(path, params, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("HttpStatusCode") && response.getInt("HttpStatusCode") != 200 && response.getInt("HttpStatusCode") != 304) {
                                reject(new CustomException("We are having difficulty processing your request. Please try again later."));
                            } else {
                                if(response.has("data"))
                                    resolve(new JSONObject(response.getString("data")));
                                else
                                    reject(new CustomException("no data"));
                            }

                        } catch (JSONException e) {
                            reject(new CustomException(e));
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 200) {
                            reject(new CustomException(error));
                        } else if (error != null && error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            reject(new CustomException("401"));
                        } else {
                            reject(new CustomException("We are having difficulty processing your request. Please try again later."));
                        }
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");
                        if (accessToken != null) //bearer
                            headers.put("Authorization", "Bearer " + accessToken);
                        else {
                            headers.put("Authorization", "Basic " + basicToken);
                        }

                        return headers;
                    }

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        JSONObject res = new JSONObject();
                        try {
                            String jsonString = new String(response.data, "UTF-8");
                            res.put("data", jsonString);
                            res.put("HttpStatusCode", response.statusCode);
                        } catch (UnsupportedEncodingException e) {
                            return Response.error(new ParseError(e));
                        } catch (JSONException je) {
                            return Response.error(new ParseError(je));
                        }

                        return Response.success(res, HttpHeaderParser.parseCacheHeaders(response));

                    }
                };
                //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions.
                //Volley does retry for you if you have specified the policy.
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonRequest);
            }
        };
    }

    //return ArrayList of projects
    public CustomPromise getProjects() {
        return new CustomPromise() {
            @Override
            public void execute() {

                makeRequestForPath(GET_PROJECTS, null).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {
                        try {

                            if(result == null){
                                resolve(null);
                            }else{

                                JSONObject jsonResult = (JSONObject) result;

                                if(jsonResult.has("STATUS") && jsonResult.get("STATUS").toString().toLowerCase().equals("ok")){
                                    ArrayList<Project> projects = new ArrayList<>();
                                    if(jsonResult.has("projects")){
                                        JSONArray jsonProjects = jsonResult.getJSONArray("projects");
                                        Type t = new TypeToken<ArrayList<Project>>(){}.getType();
                                        projects= new Gson().fromJson(jsonProjects.toString(), t);

                                    }
                                    resolve(projects);
                                }else
                                {
                                    Log.e("getProjectss",result.toString());
                                    resolve(null);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            reject(e);
                        }
                    }
                }).fail(new FailCallback() {
                    @Override
                    public void onFail(Object result) {
                        CustomException e = (CustomException) result;
                        reject(e);
                    }
                });
            }
        };
    }

    //return the Project
    public CustomPromise getProject(final int id) {
        return new CustomPromise() {
            @Override
            public void execute() {

                String path = String.format(GET_PROJECT,id);
                makeRequestForPath(path, null).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {
                        try {

                            if(result == null){
                                resolve(null);
                            }else{

                                JSONObject jsonResult = (JSONObject) result;
                                final Project project;

                                if(jsonResult.has("STATUS") && jsonResult.get("STATUS").toString().toLowerCase().equals("ok")){
                                    if(jsonResult.has("project")){
                                        JSONObject jsonProjects = jsonResult.getJSONObject("project");
                                        Type t = new TypeToken<Project>(){}.getType();
                                        project = new Gson().fromJson(jsonProjects.toString(), t);


                                        getProjectTasks(id).then(new DoneCallback() {
                                            @Override
                                            public void onDone(Object result) {
                                                if(result != null){
                                                    ArrayList<Task> tasks = (ArrayList<Task>) result;
                                                    project.setTasks(tasks);

                                                    resolve(project);

                                                }
                                            }
                                        }).fail(new FailCallback() {
                                            @Override
                                            public void onFail(Object result) {
                                                project.setTasks(new ArrayList<Task>());

                                                resolve(project);
                                            }
                                        });
                                    }


                                }else
                                {
                                    Log.e("getProject",result.toString());
                                    resolve(null);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            reject(e);
                        }
                    }
                }).fail(new FailCallback() {
                    @Override
                    public void onFail(Object result) {
                        CustomException e = (CustomException) result;
                        reject(e);
                    }
                });
            }
        };
    }

    //return the Project
    public CustomPromise getProjectTasks(final int id) {
        return new CustomPromise() {
            @Override
            public void execute() {

                String path = String.format(GET_PROJECT_TASK_LIST,id);

                makeRequestForPath(path, null).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {
                        try {

                            JSONObject jsonResult = (JSONObject) result;

                            if(jsonResult.has("STATUS") && jsonResult.get("STATUS").toString().toLowerCase().equals("ok")){
                                ArrayList<Task> tasks = new ArrayList<>();
                                if(jsonResult.has("tasklists")){
                                    JSONArray jsonProjects = jsonResult.getJSONArray("tasklists");
                                    Type t = new TypeToken<ArrayList<Task>>(){}.getType();
                                    tasks= new Gson().fromJson(jsonProjects.toString(), t);

                                }
                                resolve(tasks);
                            }else
                            {
                                Log.e("getTasks",result.toString());
                                resolve(null);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            reject(e);
                        }
                    }
                }).fail(new FailCallback() {
                    @Override
                    public void onFail(Object result) {
                        CustomException e = (CustomException) result;
                        reject(e);
                    }
                });
            }
        };
    }

    //add list of tasks into a tasklist
    public CustomPromise addTaskToTasklist(final int taskListID,final String content) {
        return new CustomPromise() {
            @Override
            public void execute() {

                String path = String.format(ADD_TASKS_TO_TASK_LIST,taskListID);
                JSONObject params = new JSONObject();

                try {
                    params.put("content",content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                makeRequestForPath(path, params).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {
                        try {

                            JSONObject jsonResult = (JSONObject) result;

                            if(jsonResult.has("STATUS") && jsonResult.get("STATUS").toString().toLowerCase().equals("ok")){
                                resolve(true);
                            }else
                            {
                                Log.e("addTasks",result.toString());
                                resolve(false);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            reject(e);
                        }
                    }
                }).fail(new FailCallback() {
                    @Override
                    public void onFail(Object result) {
                        CustomException e = (CustomException) result;
                        reject(e);
                    }
                });
            }
        };
    }



    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }

        return false;
    }


}
