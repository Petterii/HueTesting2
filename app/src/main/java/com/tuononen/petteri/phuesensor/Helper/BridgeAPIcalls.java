package com.tuononen.petteri.phuesensor.Helper;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;

import org.json.JSONObject;

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

}
