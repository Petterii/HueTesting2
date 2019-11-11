package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.Helper.BridgeAPIcalls;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Helper.SensorListAdapter;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;
import com.tuononen.petteri.phuesensor.Sensor;

import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SensorActivationActivity extends AppCompatActivity implements APIcallback {

    private MySingleton store;
    ListView listView;
    TextView testLabel;

    private FirebaseFirestore db;
    private Timer timer;
    private Button testButton;
    private Button searchBridgeButton;
    private Button activationAllButton;

    private SensorListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensoractivation);
        store = MySingleton.getInstance();
        isTimerOn = false;
        isAllTimerOn = false;
        sentTrigger = false;
        initTextFields();
        initAdapter();
        initButtons();
    }

    private void initTextFields() {
        testLabel = findViewById(R.id.sensors_testLabel);
    }

    private boolean isTimerOn;
    private boolean isAllTimerOn;

    private void initButtons() {
        testButton = (Button) findViewById(R.id.testSensorLoop);
        testButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            sentTrigger = false;
            }
        });
        activationAllButton = (Button) findViewById(R.id.home_activate_sensors);
        activationAllButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isAllTimerOn == false) {
                    isAllTimerOn = true;
                    //testButton.setEnabled(false);
                    timer = new Timer(true);
                    timer.schedule(new UpdateAllSensor(), 0, 700);
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
        List<Sensor> sensors = Sensor.mapIterator(result);
        adapter.changeList(sensors);
        adapter.notifyDataSetChanged();

        db = FirebaseFirestore.getInstance();
        for (Sensor sensor :sensors) {
            if (sensor.getPresence().equals("true") && sensor.isPreviousPresence() != true){
                //FirebaseFunctions.putFirestoreStuff(db,true);
                Log.d("FIRESTORE", "firestore : true ");
                 if (!sentTrigger) {
                    FirebaseFunctions.getToDeviceToken(db,this);
                     //FirebaseFunctions.addNotifications(db, store.getCurrentToken());
                    sentTrigger = true;
                }
                    sensor.setPreviousPresence(true);
            } else if (sensor.getPresence().equals("false") && sensor.isPreviousPresence() != false) {
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
    public void ApiRequestResultToDevice(String token) {
        if (token !=null){
            FirebaseFunctions.addNotifications(db, store.getCurrentToken());
        }
    }

    private void testingSensorCalls(String respons){
        try {
            JSONObject jsonObject = new JSONObject(respons);
            JSONObject jState = new JSONObject(jsonObject.get("state").toString());
            boolean pree = (boolean)jState.get("presence");
            testLabel.setText(""+pree);
        }catch (Exception e){
            Log.d("API", "testingSensorCalls: "+ e);
        }

    }


    class UpdateSensor extends TimerTask {
        public void run() {
            // todo change this to propper call... currently a test on specific sensor
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
}
