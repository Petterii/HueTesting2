package com.tuononen.petteri.phuesensor.Activities;

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
import com.tuononen.petteri.phuesensor.Helper.APIcallback;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements APIcallback {


    final static String TAG = "API";
    private MySingleton store;
    FirebaseFunctions db;
    TextView textView;
    TextView textView2;
    private TextView bridgeCon;
// https://www.meethue.com/api/nupnp // maybe in debugger
    String bridgeIPGetURL = "https://discovery.meethue.com ";
    final String TESTAPI = "http://www.google.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        light1 = true;
        db = new FirebaseFunctions();
        store = MySingleton.getInstance();

        textView = findViewById(R.id.labelSensor);
        textView2 = findViewById(R.id.lightLabel);
        bridgeCon = findViewById(R.id.philipHueStatus);

        if (store.getCurrentBridgeDevice() != null)
            bridgeCon.setText("OK. "+store.getCurrentBridgeDevice().getHostAddress());
      //  searchPNPDevices();
       // apiGetCall(TESTAPI);
      // textPost();
    }

    private void textPost(String url) {
        try{
            jsonApiCall(url);
        }catch (Exception e){
            Log.d(TAG, "jsonAPI: "+e);
        }
    }

    public void mapIterator(String jsonString){

        try {

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject jState = new JSONObject(jsonObject.get("state").toString());
            boolean pree = (boolean)jState.get("presence");
            //JSONObject lights = jsonObject.getJSONObject(type1);

            Iterator<String> keyIterator = jsonObject.keys();

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                JSONObject l = jsonObject.getJSONObject(key);

                boolean pres = true;
                String state;

                  //  state = l.getString("state");
                  //  JSONObject stateObject = new JSONObject(state);
                    pres = l.getBoolean("presence");
                    textView2.setText(""+pres);
                    state = "ring";
                    state = "ding";

                // String state = l.getString("state");

            }
        }catch (Exception e){
            Log.d("API", "mapIterator: catch Error" + e);
        }
    }

    public void apiGetCall(String url,final String devices){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url+devices,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mapIterator(response.toString());

                       // textView.setText("Response is: "+ response.substring(0,10));

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

    public void onClickGetLight(View view){
        apiGetCall("http://192.168.0.9/api/e6tTyKEKc-vv5e45yX-mOKXrvM-evyIyIXCq34NZ" , "lights");
    }

    public void onClickGetSensor(View view){
        Timer timer = new Timer(true);
        timer.schedule(new UpdateSensor(), 0,700);




    }

    private void apiGetCallSensor(String s, final String sensors) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        //url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        response1 = response;

                        try {

                            JSONObject jsonObject = new JSONObject(response1);
                            JSONObject jState = new JSONObject(jsonObject.get("state").toString());
                            boolean pree = (boolean)jState.get("presence");
                            textView2.setText(""+ pree);
                            Log.d(TAG, "run: sensor: "+ pree);
                        }catch (Exception e){
                            Log.d("API", "run: errrrr" + e);
                        }

                        // textView.setText("Response is: "+ response.substring(0,10));

                     //   ApiRequestResult(response);
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

    // http://192.168.0.9/api/e6tTyKEKc-vv5e45yX-mOKXrvM-evyIyIXCq34NZ/debug/clip.html
    public void onClickSwitch(View view){
        textPost("http://192.168.0.9/api/e6tTyKEKc-vv5e45yX-mOKXrvM-evyIyIXCq34NZ/lights/6/state");
    }

    public void getFirestore(View view){

       db.getFirestoreInfo();
    }
    public void putFirestore(View view){

        db.putFirestoreStuff();
    }


    public void onClickDevices(View view){
        Intent intent = new Intent(this,ListDevicesActivity.class);
        startActivity(intent);
    }

    private boolean light1;
    public void jsonApiCall(String url) throws JSONException {
        RequestQueue queue = Volley.newRequestQueue(this);
        light1 = !light1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("on", light1);
        // jsonObject.put("devicetype","my_hue_app#hue app");

        final String requestBody=jsonObject.toString();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
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
                Log.d(TAG, "onErrorResponse: error jsonobjectrequest" + error);
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

    private String response1;
    ////////////////////////////////
    //// UPNP devices
    class UpdateSensor extends TimerTask {
        public void run() {
            apiGetCallSensor("http://192.168.0.9/api/e6tTyKEKc-vv5e45yX-mOKXrvM-evyIyIXCq34NZ/sensors/24" , "sensors");
        }
    }
}
