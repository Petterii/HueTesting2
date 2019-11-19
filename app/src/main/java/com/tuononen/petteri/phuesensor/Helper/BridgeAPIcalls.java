package com.tuononen.petteri.phuesensor.Helper;

import android.app.Activity;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.Editable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.tuononen.petteri.phuesensor.Bridge;
import com.tuononen.petteri.phuesensor.BridgeUser;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BridgeAPIcalls {

    static String TAG = "API";
    static String longsensor;
    public static void apiGetCallSensor(Activity activity, String url, final APIcallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        //url ="http://www.google.com";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                      //   longsensor = response;
                        callback.ApiRequestResult(response);
/*
                        try {

                            JSONObject jsonObject = new JSONObject(longsensor);
                            JSONObject jState = new JSONObject(jsonObject.get("state").toString());
                            boolean pree = (boolean)jState.get("presence");
                           // textView2.setText(""+ pree);

                            Log.d(TAG, "run: sensor: "+ pree);

                        }catch (Exception e){
                            Log.d("API", "run: errrrr" + e);
                        }
*/

                        // textView.setText("Response is: "+ response.substring(0,10));

                        //   ApiRequestResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public static void apiGetCallSensorTest(Activity activity, String url, final APIcallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        //url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //   longsensor = response;
                        callback.ApiRequestResultTest(response);
/*
                        try {

                            JSONObject jsonObject = new JSONObject(longsensor);
                            JSONObject jState = new JSONObject(jsonObject.get("state").toString());
                            boolean pree = (boolean)jState.get("presence");
                           // textView2.setText(""+ pree);

                            Log.d(TAG, "run: sensor: "+ pree);

                        }catch (Exception e){
                            Log.d("API", "run: errrrr" + e);
                        }
*/

                        // textView.setText("Response is: "+ response.substring(0,10));

                        //   ApiRequestResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+ error);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    public static void createUser(final Activity activity, final String userName, final APIcallback callback) throws JSONException {
        final MySingleton store = MySingleton.getInstance();
        RequestQueue queue = Volley.newRequestQueue(activity);

        String url = "http://"+store.getBridgeIP().getInternalipaddress()+"/api";
        JSONObject body1 = new JSONObject();

        body1.put("devicetype","my_hue_app#app "+userName);
        final String requestBody = body1.toString();
        JSONArray b1 = new JSONArray();
        b1.put(body1);

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("json", "onResponse: "+ response);
                JSONArray a1;
                JSONObject a2;
                JSONObject a3;
                try{
                    a1 = new JSONArray(response);
                    a2 = a1.getJSONObject(0);
                    a3 = a2.getJSONObject("error");
                    String description = a3.getString("description");
                    Log.d("json", "onResponse: "+ description);
                    Toast.makeText(activity, ""+description, Toast.LENGTH_SHORT).show();
                    callback.ApiRequestResult("Error");
                }catch (Exception e){
                    try{
                        a1 = new JSONArray(response);
                        a2 = a1.getJSONObject(0);
                        a3 = a2.getJSONObject("success");
                        String key = a3.getString("username");
                        Toast.makeText(activity, "USER CREATED!!", Toast.LENGTH_SHORT).show();
                        store.getBridgeIP().setUserKey(key);
                        callback.ApiRequestResult("Done");
                    }catch (Exception e1){
                        Log.d("json", "onResponse: " +e );
                    }
                    Log.d("json", "onResponse: "+e);
                    Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("json", "onErrorResponse: "+ error);
                Toast.makeText(activity, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
        ) {
            // this is the relevant method


            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        queue.add(postRequest);

    }

    public static void apiSetSensorOnState(Context activity, boolean on, String id) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity);
        MySingleton store = MySingleton.getInstance();
        String ip = store.getBridgeIP().getInternalipaddress();
        String key = store.getBridgeIP().getKey();
        String url = "http://" +ip +"/api/" + key+"/sensors/"+id+"/config";
        //url ="http://www.google.com";

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("on", on);
        } catch (JSONException e) {
            Log.d(TAG, "api" + "+e");
            // handle exception
        }

        final String requestBody = jsonObject.toString();
        // Request a string response from the provided URL.
        JsonArrayRequest putRequest = new JsonArrayRequest(Request.Method.PUT, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("API", "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("API", "onErrorResponse: " +error.toString());
            }
        }){
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };


        // Add the request to the RequestQueue.
        queue.add(putRequest);

    }


    //// cant use jsonOBJECTTrequest because I get array as an response
    /*
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, body1, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String key = GsonHelper.getKey(response.toString());
                if (key.equals("error"))
                    Toast.makeText(activity, "Error. something went wrong", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(activity, "Worked", Toast.LENGTH_SHORT).show();

                //store.getBridgeIP();
                Log.d("Response", "onResponse: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d("Response", "onErrorResponse: "+error.toString());
                String discription = GsonHelper.getError(error.getMessage());
                Toast.makeText(activity, "Fail. Did u hit Bridge Button?", Toast.LENGTH_SHORT).show();
            }
        });
  */
}
