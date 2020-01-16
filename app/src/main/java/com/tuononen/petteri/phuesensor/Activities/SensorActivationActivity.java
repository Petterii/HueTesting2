package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.Helper.BridgeAPIcalls;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SensorListAdapter;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;
import com.tuononen.petteri.phuesensor.Sensor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorActivationActivity extends AppCompatActivity implements APIcallback, MyResultReceiver.Receiver {

    private MySingleton store;
    ListView listView;
    TextView testLabel;

    private FirebaseFirestore db;
    private Timer timer;
    private Button testButton;
    private Button searchBridgeButton;
    private Button activationAllButton;
    private ArrayList<Sensor> sensors;


    private SensorListAdapter adapter;

    private SesnorBackReceiver sensorBackReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensoractivation);
        store = MySingleton.getInstance();
        isTimerOn = false;
        isAllTimerOn = false;
        sentTrigger = false;

        sensor1registerReceiver();

        initTextFields();
        initAdapter();
        initButtons();
    }



    private void initTextFields() {

    }



    private boolean isTimerOn;
    private boolean isAllTimerOn;

    private void initButtons() {

        activationAllButton = (Button) findViewById(R.id.home_activate_sensors);
        activationAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isAllTimerOn == false) {

                    isAllTimerOn = true;
                    //testButton.setEnabled(false);
                    timer = new Timer(true);
                //    timer.schedule(new UpdateAllSensor(), 0, 700);

                    Intent intentService = new Intent(Intent.ACTION_SYNC,null,SensorActivationActivity.this,BackgroundScanning.class);
                    //intentService.setClass(this,);
                    ContextCompat.startForegroundService(SensorActivationActivity.this,intentService);


                }else{

                    isAllTimerOn = false;
                    timer.cancel();

                }
            }
        });

    }

    private void initAdapter(){

        listView = findViewById(R.id.list_sensors);
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


    private boolean sentTrigger;
    @Override
    public void ApiRequestResult(String result) {
        String hello = result;
        sensors = Sensor.mapIterator(this,sensors,result);
        adapter.changeList(sensors);
        adapter.notifyDataSetChanged();

        db = FirebaseFirestore.getInstance();
        for (Sensor sensor :sensors) {
            if (sensor.getPresence() && sensor.isPreviousPresence())
                continue;
            if (sensor.getPresence() && !sensor.isPreviousPresence()){
                sensor.setPreviousPresence(true);
                //FirebaseFunctions.putFirestoreStuff(db,true);
                Log.d("FIRESTORE", "firestore : true ");

                    FirebaseFunctions.getToDeviceToken(db,this, sensor.getId());
                     //FirebaseFunctions.addNotifications(db, store.getCurrentToken());
                    sentTrigger = true;


            } else if (!sensor.getPresence()&& sensor.isPreviousPresence()) {
                //FirebaseFunctions.putFirestoreStuff(db,false);
                sensor.setPreviousPresence(false);
                Log.d("FIRESTORE", "firestore : false ");

            }

        }
    }

    @Override
    public void ApiRequestResultTest(String response) {
        testingSensorCalls(response);
    }

    @Override
    public void ApiRequestResultToken(String token) {

    }

    @Override
    public void ApiRequestResultToDevice(String token,String sensorId) {
        if (token !=null){
            // todo remove comment to add notifications again
            FirebaseFunctions.addNotifications(db, token,sensorId);
        }
    }

    private void testingSensorCalls(String respons){


    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        //// TODO RECEIVE BACKGROUNDSCANNI)NG
        Toast.makeText(SensorActivationActivity.this, "Well update", Toast.LENGTH_SHORT).show();

    }


    class UpdateSensor extends TimerTask {
        public void run() {
            // todo change this to propper call... currently a test on specific sensor
            Toast.makeText(SensorActivationActivity.this, "Well update", Toast.LENGTH_SHORT).show();
            BridgeAPIcalls.apiGetCallSensorTest(SensorActivationActivity.this,
                    "http://"+store.getBridgeIP().getInternalipaddress()+"/api/"+store.getBridgeIP().getKey()+"/sensors/24" , SensorActivationActivity.this);
        }
    }

    class UpdateAllSensor extends TimerTask {
        public void run() {

            BridgeAPIcalls.apiGetCallSensor(SensorActivationActivity.this,
                    "http://"+store.getBridgeIP().getInternalipaddress()+"/api/"+store.getBridgeIP().getKey()+"/sensors" , SensorActivationActivity.this);
        }
    }
    private void sensor1registerReceiver() {
        sensorBackReceiver = new SesnorBackReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BackgroundScanning.SENSOR_INFO);
        registerReceiver(sensorBackReceiver, intentFilter);
    }

    private class SesnorBackReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sensorslongstring = intent.getStringExtra("sensorshere");
            ApiRequestResult(sensorslongstring);
            //Toast.makeText(SensorActivationActivity.this, "Well update", Toast.LENGTH_SHORT).show();

        }
    }
}
