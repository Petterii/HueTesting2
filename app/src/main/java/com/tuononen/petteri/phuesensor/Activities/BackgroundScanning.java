package com.tuononen.petteri.phuesensor.Activities;

import android.app.Activity;
import android.app.IntentService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
    public static final String CHANNEL_ID = "ScanningID";

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
    private PowerManager.WakeLock wakeLock;

    public static void turnOff(){
        shuldStop = true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        shuldStop = false;
        timerOn = true;
        sensors = new ArrayList<>();
        inForeground = false;
        createNotificationChannel();

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ExampleApp:Wakelock");
        wakeLock.acquire();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID, "Example Service Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

            sendNot26something("Scanning...");
        }
        else
            sendNot26something("Scanning...");
            //sendNotification("Scanning Sensors");



    }

    private void sendNot26something(String msg) {
        Log.d(TAG, "sendNot26something: start");
        Intent notificationIntent = new Intent(this,SensorActivationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Sensor Scanning is On")
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1,notification);
        Log.d(TAG, "sendNot26something: done");
    }

    private NotificationManager mNotificationManager;
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SensorActivationActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Philip Hue Sensors")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.ic_android);
        mBuilder.setContentIntent(contentIntent);

        mNotificationManager.notify(1, mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        NotificationManagerCompat notificationManagerc = NotificationManagerCompat.from(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = "sensorInfo";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            notificationManagerc.createNotificationChannel(channel);
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
    private static boolean shuldStop;

    private void doAPICall() {
        while(timerOn && !shuldStop ){


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

        if (shuldStop)
        {
            sendNotification("Scanning Sensors STOPPED!!!");
        }

    }

    private static boolean inForeground;
    // private ArrayList<Sensor> sensors;

    private void handleResult(String result){

        if (inForeground)
            checkIfPressence(result);
        else
            sendSensorsToClient(result);
    }

    private void checkIfPressence(String result) {
        sensors = Sensor.mapIteratorinForeground(this, sensors, result);
        for (Sensor sensor : sensors) {

                sendNot26something("Motion Detected");
                db = FirebaseFirestore.getInstance();
                if (sensor.getPresence() && sensor.isPreviousPresence())
                    continue;
                if (sensor.getPresence() && !sensor.isPreviousPresence()){
                    sensor.setPreviousPresence(true);
                    Log.d("FIRESTORE", "firestore : true ");
                    FirebaseFunctions.getToDeviceToken(db,this, sensor.getId());

                } else if (!sensor.getPresence()&& sensor.isPreviousPresence()) {
                    sensor.setPreviousPresence(false);
                    Log.d("FIRESTORE", "firestore : false ");

                }

        }
    }

    public static void changeToForeground(){
        inForeground = true;
    }
    public static void changeoutOfForeground(){
        inForeground = false;
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
        if (token !=null){
            // todo remove comment to add notifications again
            FirebaseFunctions.addNotifications(db, token,sensorId);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        wakeLock.release();
    }
}
