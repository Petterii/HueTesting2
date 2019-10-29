package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com._8rine.upnpdiscovery.UPnPDevice;
import com._8rine.upnpdiscovery.UPnPDiscovery;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SingleLineAdapter;
import com.tuononen.petteri.phuesensor.R;

import java.util.HashSet;
import java.util.List;

public class ListDevicesActivity extends AppCompatActivity {

    private ListView listView;
    private SingleLineAdapter adapter;
    private MySingleton store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);
        store = MySingleton.getInstance();
        initAdapter();

        searchPNPDevices();

    }
    private List<UPnPDevice> devices;

    public void searchPNPDevices(){
        // devices = new ArrayList<>();
        UPnPDiscovery.discoveryDevices(this, new UPnPDiscovery.OnDiscoveryListener() {
            @Override
            public void OnStart() {
                Log.d("UPnPDiscovery", "Starting discovery");
            }

            @Override
            public void OnFoundNewDevice(UPnPDevice device) {
               // devices.add(device);
                adapter.addItem(device);
                adapter.notifyDataSetChanged();
                String friendlyName = device.getHostAddress();
                Log.d("UPnPDiscovery", "OnFoundNewDevice: "+friendlyName);


                // ... see UPnPDevice description below
            }

            @Override
            public void OnFinish(HashSet<UPnPDevice> devices) {
                Log.d("UPnPDiscovery", "Finish discovery");
                for (UPnPDevice device :
                        devices) {
                    Log.d("UPnPDiscovery", ""+device.getHostAddress().toString());
                }

              //  apiGetCall(bridgeIPGetURL);
            }

            @Override
            public void OnError(Exception e) {
                Log.d("UPnPDiscovery", "Error: " + e.getLocalizedMessage());
            }
        });

    }
    String bridgeIPGetURL = "https://discovery.meethue.com ";
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
                        Log.d("PNP", "onResponse: OK check debug");
                        //textView.setText("Response is: "+ response.substring(0,10));

                        //ApiRequestResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    private void initAdapter(){

        listView = findViewById(R.id.devices_list);
        adapter = new SingleLineAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                store.setCurrentBridgeDevice(SingleLineAdapter.devices.get(position));
                Intent intent = new Intent(ListDevicesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
