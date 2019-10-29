package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SingleLineAdapter;
import com.tuononen.petteri.phuesensor.R;

public class ListSensorsActivity extends AppCompatActivity {

    private MySingleton store;
    ListView listView;
    private SensorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_sensors);
        store = MySingleton.getInstance();

    }

    private void initAdapter(){

        listView = findViewById(R.id.devices_list);
        adapter = new SensorListAdapter(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //store.setCurrentBridgeDevice(SingleLineAdapter.devices.get(position));

              // todo get sensors?
            }
        });
    }

}
