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

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class CommsLayer {

    private static final String API_TOKEN = "twp_TEbBXGCnvl2HfvXWfkLUlzx92e3T";
    private static final String BASE_URL = "https://yat.teamwork.com";
    private static final String GET_PROJECTS = "/projects.json";
    private static final String USERNAME = "yat@triplespin.com";
    private static final String PASSWORD = "yatyatyat27";

    private static CommsLayer commsLayer;
    private Context context;
    private RequestQueue queue;
    private String clientId, serverUrl, clientSecret, accessToken, basicToken;


    public static CommsLayer getComms() {
        if (commsLayer == null) {
            commsLayer = new CommsLayer();
            commsLayer.init();
        }

        return commsLayer;
    }

    private void init() {
        basicToken = createBasicToken();
    }

    private String createBasicToken() {
        String basicToken = USERNAME + ":" + PASSWORD;
        return Base64.encodeToString(basicToken.getBytes(), Base64.NO_WRAP);
    }

    public CustomPromise makeRequestForPathWithAuth(final String endPoint, final JSONObject params, String accessToken) {
        return makeRequestForPath(endPoint, params, accessToken);
    }

    public CustomPromise makeRequestForPath(final String endPoint, final JSONObject params) {
        return makeRequestForPath(endPoint, params, null);
    }

    public CustomPromise makeRequestForPath(final String endPoint, final JSONObject params, final String accessToken) {
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
                            if (response.has("HttpStatusCode") && response.getInt("HttpStatusCode") != 200) {
                                reject(new CustomException("We are having difficulty processing your request. Please try again later."));
                            } else {
                                resolve(response);
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
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                queue.add(jsonRequest);
            }
        };
    }


    public CustomPromise getProgects() {
        return new CustomPromise() {
            @Override
            public void execute() {

                makeRequestForPath(GET_PROJECTS, null).then(new DoneCallback() {
                    @Override
                    public void onDone(Object result) {
                        resolve(null);
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
