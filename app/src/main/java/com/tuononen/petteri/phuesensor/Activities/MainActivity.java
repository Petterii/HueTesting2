package com.tuononen.petteri.phuesensor;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements APIcallback {


    final static String TAG = "API";

    TextView textView;
    TextView textView2;
// https://www.meethue.com/api/nupnp // maybe in debugger
    String bridgeIPGetURL = "https://discovery.meethue.com ";
    final String TESTAPI = "http://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        textView = findViewById(R.id.labelSensor);
        textView2 = findViewById(R.id.lightLabel);


      //  searchPNPDevices();
       // apiGetCall(TESTAPI);
      // textPost();
    }

    private void textPost() {
        try{
            jsonApiCall("");
        }catch (Exception e){
            Log.d(TAG, "jsonAPI: "+e);
        }
    }

    public void apiGetCall(String url){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: "+ response.substring(0,10));

                        ApiRequestResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    public void ApiRequestResult(String result) {

    }

    public void onClickNext(View view){

        apiGetCall("http://192.168.0.9/api/e6tTyKEKc-vv5e45yX-mOKXrvM-evyIyIXCq34NZ/sensors");
    }

    public void onClickDevices(View view){
        Intent intent = new Intent(this,ListDevicesActivity.class);
        startActivity(intent);
    }


    public void jsonApiCall(String url) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Number", "124");
        jsonObject.put("devicetype","my_hue_app#hue app");

        final String requestBody=jsonObject.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //now handle the response
                       //textView.setText(response.toString());
                        textView.setText("it Worked");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //handle the error
                Log.d(TAG, "onErrorResponse: error jsonobjectrequest");
                error.printStackTrace();
            }
        }) {    //this is the part, that adds the header to the request

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("Accept", "application/json");
                //params.put("Authorization", "");
               // todo do I send it here or in jsonbody
                //params.put("devicetype","my_hue_app#hue app");
                params.put("Content-Type","application/json");
                return params;
            }
        };
        queue.add(jsonRequest);
    }


    ////////////////////////////////
    //// UPNP devices

}
