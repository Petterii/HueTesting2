package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com._8rine.upnpdiscovery.UPnPDevice;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SingleLineAdapter;
import com.tuononen.petteri.phuesensor.Interfaces.UPNPcallback;
import com.tuononen.petteri.phuesensor.Helper.UPNPsearch;
import com.tuononen.petteri.phuesensor.R;

public class HomeSetupActivity extends AppCompatActivity implements UPNPcallback {

    private ListView listView;
    private SingleLineAdapter adapter;
    private MySingleton store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homesetup);
        store = MySingleton.getInstance();

        initAdapter();
        initButton();
    }

    private void initButton() {
        Button searchBridgeButton = (Button) findViewById(R.id.setup_find_bridge);
        searchBridgeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UPNPsearch.searchPNPDevices(HomeSetupActivity.this, HomeSetupActivity.this);
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
}
