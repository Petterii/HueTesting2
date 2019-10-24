package com.tuononen.petteri.phuesensor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com._8rine.upnpdiscovery.UPnPDevice;
import com._8rine.upnpdiscovery.UPnPDiscovery;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ListDevicesActivity extends AppCompatActivity {

    private ListView listView;
    private SingleLineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_devices);

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
            }

            @Override
            public void OnError(Exception e) {
                Log.d("UPnPDiscovery", "Error: " + e.getLocalizedMessage());
            }
        });

    }
    private void initAdapter(){

        listView = findViewById(R.id.devices_list);
        adapter = new SingleLineAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListDevicesActivity.this, "Clicked Item" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
