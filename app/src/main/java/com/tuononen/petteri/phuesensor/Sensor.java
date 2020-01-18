package com.tuononen.petteri.phuesensor;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.tuononen.petteri.phuesensor.Helper.NotificationHistoryAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Sensor {
    public static List<Sensor> sensors;

    private boolean presence;
    private String id;
    private String name;
    // private ArrayList<String> notificationHistory;

    private NotificationHistoryAdapter adapter;
    private ListView listView;
    private ArrayList<String> history;
    private boolean previousPresence;
    private boolean on;
    private boolean notifying;
    private boolean sound;

    private Sensor(){};

    private Sensor(Context context, String pname, String pressence, String key, boolean on) {
        this.id = key;
        this.name = pname;
        if (pressence.equals("true"))
            this.presence = true;
        else
            this.presence = false;
        this.on = on;
        this.sound = false;
        this.notifying = false;
        this.previousPresence = false;

        adapter = new NotificationHistoryAdapter(context);
    }

    public void setListView(View view){
        listView = view.findViewById(R.id.scanning_listhistory);
        listView.setAdapter(adapter);
    }

    public Sensor(ArrayList<Sensor> sensors, String jsonString) {
        // mapIterator(sensors,jsonString);
        previousPresence = false;
    }

    public static ArrayList<Sensor> mapIterator(Context context,ArrayList<Sensor> sensors, String jsonString){
        if (sensors == null || sensors.size() == 0) {
            sensors = new ArrayList<>();
            sensors = getFirstTimeSensors(context,sensors, jsonString);
            }
        else{
           sensors = updateSensors(sensors,jsonString);
        }
        return sensors;
    }

    public static ArrayList<Sensor> mapIteratorinForeground(Context context,ArrayList<Sensor> sensors, String jsonString){
        if (sensors == null || sensors.size() == 0) {
            sensors = new ArrayList<>();
            sensors = getFirstTimeSensors(context,sensors, jsonString);
        }
        else{
            sensors = checkPresence(sensors,jsonString);
        }
        return sensors;
    }

    private static ArrayList<Sensor> updateSensors(ArrayList<Sensor> sensors, String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator<String> keyIterator = jsonObject.keys();

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                Sensor sensor = null;
                for (Sensor s : sensors) {
                    if (s.id.equals(key)){
                        sensor = s;
                        break;
                    }
                }
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


                    sensor.update(pname,pressence,on);
                    //sensors.add(sensor);
                }catch (Exception e){
                  //  Log.d("API", "mapIterator: " + e);
                }

                int x = 1;
                x = 5;
                // String state = l.getString("state");

            }
        }catch (Exception e){
           // Log.d("API", "mapIterator: catch Error" + e);
        }

        return sensors;
    }

    private void update(String pname, String pressence, boolean on) {

        this.name = pname;
        if (pressence.equals("true"))
            this.presence = true;
        else
            this.presence = false;

        this.on = on;

    }

    private static ArrayList<Sensor> getFirstTimeSensors(Context context, ArrayList<Sensor> sensors, String jsonString) {
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

                    Sensor sensor = new Sensor(context, pname,pressence,key,on);
                    sensors.add(sensor);
                }catch (Exception e){
             //       Log.d("API", "mapIterator: " + e);
                }

                int x = 1;
                x = 5;
                // String state = l.getString("state");

            }
        }catch (Exception e){
          //  Log.d("API", "mapIterator: catch Error" + e);
        }

        return sensors;
    }

    // bad code... repeating. had other ideas to make it shorter for just presence. but changed my mind to have more options for future storing in firebase with notifications
    public static ArrayList<Sensor> checkPresence(ArrayList<Sensor> sensors, String jsonString) {
        boolean newlist = false;
        if (sensors == null) {
            newlist = true;
            sensors = new ArrayList<>();
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            Iterator<String> keyIterator = jsonObject.keys();

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                Sensor sensor = new Sensor();
                for (Sensor s : sensors) {
                    if (s.id.equals(key)){
                        sensor = s;
                        break;
                    }
                }
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


                    sensor.update(pname,pressence,on);
                    if (newlist)
                        sensors.add(sensor);
                }catch (Exception e){
            //        Log.d("API", "mapIterator: " + e);
                }

            }
        }catch (Exception e){
          //  Log.d("API", "mapIterator: catch Error" + e);
        }

        return sensors;
    }

    public boolean getPresence() {
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

    public boolean isOn() {
        return on;
    }

    public boolean isSound() {
        return sound;
    }

    public void setOn() {
        this.on = !this.on;
    }

    public void setHistory(ArrayList<String> time) {
        this.history = time;
        this.adapter.setHistory(time);
    }

    public ArrayList<String> getHistory() {
        return history;
    }

    public ListView getListView() {
        return this.listView;
    }

    public NotificationHistoryAdapter getAdapter() {
        return this.adapter;
    }
}
