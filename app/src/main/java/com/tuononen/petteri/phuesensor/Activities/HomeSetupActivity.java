package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com._8rine.upnpdiscovery.UPnPDevice;
import com._8rine.upnpdiscovery.UPnPDiscovery;
import com.tuononen.petteri.phuesensor.Bridge;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SingleLineAdapter;
import com.tuononen.petteri.phuesensor.Interfaces.UPNPcallback;
import com.tuononen.petteri.phuesensor.Helper.UPNPsearch;
import com.tuononen.petteri.phuesensor.R;

import java.util.HashSet;

public class HomeSetupActivity extends AppCompatActivity implements UPNPcallback {

    private ListView listView;
    private SingleLineAdapter adapter;
    private MySingleton store;
    private Button manualButton;
    private EditText manualIP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesetup);
        store = MySingleton.getInstance();
        manualIP = findViewById(R.id.home_iptext);
        initAdapter();
        initButton();
    }

    private void initButton() {
        Button searchBridgeButton = (Button) findViewById(R.id.setup_find_bridge);
        searchBridgeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // UPNPsearch.searchPNPDevices(HomeSetupActivity.this, HomeSetupActivity.this);
                searchPNPDevices(HomeSetupActivity.this, HomeSetupActivity.this);
            }
        });
        manualButton = findViewById(R.id.home_manual);
        manualButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                store.setCurrentBridgeDeviceIP(new Bridge(manualIP.getText().toString()));
                startActivity(new Intent(HomeSetupActivity.this,BridgeUserActivity.class));


            }
        });
    }

    private void initAdapter(){
        listView = findViewById(R.id.setup_list_bridges);
        adapter = new SingleLineAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                store.setCurrentBridgeDevice(SingleLineAdapter.devices.get(position));
                Intent intent = new Intent(HomeSetupActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void UPNPgotDevice(UPnPDevice device) {
        adapter.addItem(device);
        adapter.notifyDataSetChanged();
    }

    public void searchPNPDevices(Activity activity, final UPNPcallback callback){

        // devices = new ArrayList<>();
        UPnPDiscovery.discoveryDevices(activity, new UPnPDiscovery.OnDiscoveryListener() {
            @Override
            public void OnStart() {
                Log.d("UPnPDiscovery", "Starting discovery");
            }

            @Override
            public void OnFoundNewDevice(UPnPDevice device) {
                // devices.add(device);
                callback.UPNPgotDevice(device);
            }

            @Override
            public void OnFinish(HashSet<UPnPDevice> devices) {
                Log.d("UPnPDiscovery", "Finish discovery");

            }

            @Override
            public void OnError(Exception e) {
                Log.d("UPnPDiscovery", "Error: " + e.getLocalizedMessage());
            }
        });

    }

}
