package com.tuononen.petteri.phuesensor.Activities;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.tuononen.petteri.phuesensor.Bridge;
import com.tuononen.petteri.phuesensor.BridgeUser;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;

public class MainMenuActivity extends AppCompatActivity implements APIcallback {

/*
    final static String TAG = "API";

    FirebaseFunctions db;
    TextView textView;
    TextView textView2;
    private TextView bridgeCon;
// https://www.meethue.com/api/nupnp // maybe in debugger
    String bridgeIPGetURL = "https://discovery.meethue.com ";
    final String TESTAPI = "http://www.google.com";
*/
    private TextView connectedBrigde;
    private MySingleton store;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        db = FirebaseFirestore.getInstance();
        store = MySingleton.getInstance();
        getPreffs();

        initButtons();
        initTextFields();
        getBrigde();
    }

    private void getPreffs() {
        MySingleton store = MySingleton.getInstance();
        SharedPreferences shared = getSharedPreferences("MYPREFFS", MODE_PRIVATE);

        String ip = shared.getString("bridgeip","");
        String key = shared.getString("bridgekey", "");

        Bridge buser = new Bridge(ip,key);
        store.setCurrentBridgeDeviceIP(buser);

        //editor.putString(Email, e);
    }


    private void getBrigde() {
        if (store.getBridgeIP() != null)
            connectedBrigde.setText(store.getBridgeIP().getInternalipaddress());
        else
            connectedBrigde.setText("NONE");
    }

    private void initTextFields() {
        connectedBrigde = findViewById(R.id.mainmenu_connectedbridge);

        connectedBrigde.setText(store.getBridgeIP().getInternalipaddress());
    }

    private void initButtons() {

        final Button setupButton = (Button) findViewById(R.id.mainmenu_homesetup);

        setupButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this,HomeSetupActivity.class);
                startActivity(intent);
            }
        });

        final Button sensorButton = (Button) findViewById(R.id.mainmenu_homeactivation);

        sensorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this,SensorActivationActivity.class);
                startActivity(intent);
            }
        });

        Button pushNotisButton = (Button) findViewById(R.id.mainmenu_pushtest);

        pushNotisButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                try {

                    PushoverClient client = new PushoverRestClient();
                    client.pushMessage(PushoverMessage.builderWithApiToken("afw1tyat8px44tenjxpjq2odcabq5e")
                            .setUserId("uw8rs6pbw4retdf2kntg9fdnmu83pp")
                            .setMessage("testing!")
                            .build());

                }catch (Exception e){
                    Log.d("PUSH", "pushover : " + e);
                }
                */
               // startActivity(new Intent(MainMenuActivity.this,JmDNSActivity.class));
                setupButton.setEnabled(false);
                sensorButton.setEnabled(false);
                FirebaseFunctions.getFireStoreToken(MainMenuActivity.this,MainMenuActivity.this);
            }
        });

        Button logout = (Button) findViewById(R.id.mainmenu_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainMenuActivity.this,LoginActivity.class));
            }
        });
    }

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public void loginAuth(){




    }

    @Override
    public void ApiRequestResult(String result) {

    }

    @Override
    public void ApiRequestResultTest(String response) {

    }

    @Override
    public void ApiRequestResultToken(String token) {
        store.setCurrentToken(token);
        FirebaseFunctions.addToDevicetoUser(token);
    }

    @Override
    public void ApiRequestResultToDevice(String token, String sensorId) {

    }

    /*
    @Override
    protected void onStart() {
        super.onStart();
        db.collection("Users").document("123").addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                
            }
        });

    }
    */
    /*

    public void getFirestore(View view){

       db.getFirestoreInfo();
    }
    public void putFirestore(View view){

        db.putFirestoreStuff();
    }


    public void onClickDevices(View view){
        Intent intent = new Intent(this, HomeSetupActivity.class);
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

*/
}
