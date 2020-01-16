package com.tuononen.petteri.phuesensor.Activities;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.Helper.BridgeAPIcalls;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.HttpCallsHelper;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;
import com.tuononen.petteri.phuesensor.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundScanning extends IntentService implements APIcallback {
    public BackgroundScanning() {
        super("ScanningSensors");
        isTimerOn = false;
        isAllTimerOn = false;
    }



    private boolean isTimerOn;
    private boolean isAllTimerOn;

    private Timer timer;
    private MySingleton store;

    private Activity activty;
    private APIcallback callback;

    private final String TAG = "IntentService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        timerOn = true;
        sensors = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            Notification notification = new Notification.Builder(this, "NotificationID")
                    .setContentTitle("Example IntentService")
                    .setContentText("Running...")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build();
            startForeground(1,notification);
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        Log.d(TAG, "INTENT START");
        this.callback = callback;

        store = MySingleton.getInstance();
        if (isAllTimerOn == false) {
            isAllTimerOn = true;
            //testButton.setEnabled(false);

            doAPICall();
            /*
            timer = new Timer(true);
            timer.schedule(new BackgroundScanning.UpdateAllSensor(), 0, 700);
            */
        }else {
            //isAllTimerOn = false;
            // timer.cancel();
        }
    }

    private boolean timerOn;

    private void doAPICall() {
        while(timerOn){
            String result = "";
           // BridgeAPIcalls.apiGetCallSensor(this,
             //       "http://"+store.getBridgeIP().getInternalipaddress()+"/api/"+store.getBridgeIP().getKey()+"/sensors" , this);
            try{
                result = HttpCallsHelper.sendGet("http://"+store.getBridgeIP().getInternalipaddress()+"/api/"+store.getBridgeIP().getKey()+"/sensors");
                handleResult(result);
            }catch (Exception e){
                Log.d(TAG, "ERROR api something: ");
            }
              SystemClock.sleep(1500);
        }
    }

    private void handleResult(String result){
        String hello = result;
        //List<Sensor> sensors = new ArrayList<>();
/*
         sensors = Sensor.mapIterator(this,sensors,result);

         // todo when app opens again
        // adapter.changeList(sensors);
        // adapter.notifyDataSetChanged();

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
  */
        sendSensorsToClient(result);
    }

    private void sendSensorsToClient(String msg){
        Intent intent = new Intent();
        intent.setAction(SENSOR_INFO);
        intent.putExtra("sensorshere",msg);
        sendBroadcast(intent);
    }

    private String Message = "Testing";
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sendBroadcast();
        }
    };

    private void sendBroadcast(){
// If your value is still null, run the runnable again
        if (Message == null){
            handler.postDelayed(runnable, 1000);
        }
        else{
//Here where it is expected to send the broadcast
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.example.intent.action.MESSAGE_PROCESSED");
            broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
            broadcastIntent.putExtra("TAG",Message);
            getApplicationContext().sendBroadcast(broadcastIntent);
        }
    }

    private FirebaseFirestore db;
    private boolean sentTrigger;
    private ArrayList<Sensor> sensors;
    @Override
    public void ApiRequestResult(String result) {
        String hello = result;
        //List<Sensor> sensors = new ArrayList<>();

       // sensors = Sensor.mapIterator(this,sensors,result);
       // adapter.changeList(sensors);
       // adapter.notifyDataSetChanged();

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

    }

    @Override
    public void ApiRequestResultToken(String token) {

    }

    @Override
    public void ApiRequestResultToDevice(String token, String sensorId) {

    }

    public static final String SENSOR_INFO = "SENSOR_INFO";

    class UpdateAllSensor extends TimerTask {
        public void run() {
            Log.d(TAG, "START INTENT SERVICE TIMER");
            Intent intentBroadcast = new Intent(SENSOR_INFO);
            intentBroadcast.putExtra("111", 123);

            getApplicationContext().sendBroadcast(intentBroadcast);
            //  BridgeAPIcalls.apiGetCallSensor(activty,
          //          "http://"+store.getBridgeIP().getInternalipaddress()+"/api/"+store.getBridgeIP().getKey()+"/sensors" , callback);
        }
    }

}
