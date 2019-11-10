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
    private boolean previousPresence;

    public Sensor(String jsonString) {
        mapIterator(jsonString);
        previousPresence = false;
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


                    Sensor sensor = new Sensor(pname,pressence,key);
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



    private Sensor(String pname, String pressence,String key) {
        this.id = key;
        this.name = pname;
        this.presence = pressence;
        /*
        if (pressence.equals("true") && this.previousPresence != true)
            this.previousPresence = true;
        else if(presence.equals("false") && this.previousPresence != false)
            this.previousPresence = false;
    */
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
}
