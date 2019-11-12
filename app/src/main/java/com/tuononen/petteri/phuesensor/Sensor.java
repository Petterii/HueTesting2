package com.tuononen.petteri.phuesensor;

import android.util.Log;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sensor {
    public static List<Sensor> sensors;

    private String presence;
    private String id;
    private String name;
    private ArrayList<String> notificationHistory;
    private boolean previousPresence;
    private boolean on;
    private boolean notifying;
    private boolean sound;


    public Sensor(String jsonString) {
        mapIterator(jsonString);
        previousPresence = false;
        notificationHistory = new ArrayList<>();
    }

    public static List<Sensor> mapIterator(String jsonString){
        sensors = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator<String> keyIterator = jsonObject.keys();

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                JSONObject l = jsonObject.getJSONObject(key);


                String pname;
                //  state = l.getString("state");
                //  JSONObject stateObject = new JSONObject(state);
                try {
                    pname = l.getString("productname");
                    JSONObject state = (JSONObject) l.get("state");
                    String pressence = state.getString("presence");

                    JSONObject config = (JSONObject) l.get("config");
                    boolean on = config.getBoolean("on");

                    Sensor sensor = new Sensor(pname,pressence,key,on);
                    sensors.add(sensor);
                }catch (Exception e){
                    Log.d("API", "mapIterator: " + e);
                }

                int x = 1;
                x = 5;
                // String state = l.getString("state");

            }
        }catch (Exception e){
            Log.d("API", "mapIterator: catch Error" + e);
        }

        return sensors;
    }



    private Sensor(String pname, String pressence,String key, boolean on) {
        this.id = key;
        this.name = pname;
        this.presence = pressence;
        this.on = on;
        this.sound = false;
        this.notifying = false;
    }

    public String getPresence() {
        return presence;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isPreviousPresence() {
        return this.previousPresence;
    }

    public void setPreviousPresence(boolean b) {
        this.previousPresence = b;
    }

    public boolean isNotifying() {
        return notifying;
    }

    public void setNotifying(boolean notifying) {
        this.notifying = notifying;
    }
}
