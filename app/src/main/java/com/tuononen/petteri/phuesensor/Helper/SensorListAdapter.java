package com.tuononen.petteri.phuesensor.Helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;
import com.tuononen.petteri.phuesensor.Sensor;

import java.util.ArrayList;
import java.util.List;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;

public class SensorListAdapter extends BaseAdapter implements APIcallback,APIHistorycallback {

    private LayoutInflater inflater;
    private List<Sensor> sensors;
    private List<NotificationHistoryAdapter> adapterHistory;
    private Context context;
    private List<ListView> listView;

    public SensorListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sensors = new ArrayList<>();
        this.context = context;
    }

    @Override
    public int getCount() {
        return sensors.size();
    }

    @Override
    public Object getItem(int position) {
        return sensors.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (convertView == null)
            view = inflater.inflate(R.layout.listview_sensors, viewGroup, false);
        else
            view = convertView;

        TextView name = view.findViewById(R.id.list_sensors_name);
        TextView nr = view.findViewById(R.id.list_sensor_nr);
        Button ntfBtn = view.findViewById(R.id.scanning_notebtn);
        Button onBtn = view.findViewById(R.id.scanning_onoff);
        Button soundBtn = view.findViewById(R.id.scanning_sndbtn);

        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensors.get(position).setOn();
                BridgeAPIcalls.apiSetSensorOnState(context,sensors.get(position).isOn(),sensors.get(position).getId());
                notifyDataSetChanged();
            }
        });


        if (sensors.get(position).isOn())
            onBtn.setBackgroundColor(Color.RED);
        else
            onBtn.setBackgroundColor(Color.GREEN);

        heightOfCell(view,position,onBtn.getHeight());

        name.setText(sensors.get(position).getName());
        nr.setText(""+sensors.get(position).getPresence());
/*
        if (sensors.get(position).getPresence().equals("true"))
            view.setBackgroundColor(Color.RED);
        else
            view.setBackgroundColor(Color.GREEN);
*/
        if (sensors.get(position).getListView() == null){
            sensors.get(position).setListView(view);
            getHistory(position);
        }
/*
        listView = view.findViewById(R.id.scanning_listhistory);
        adapterHistory.add(new NotificationHistoryAdapter(context));
        listView.get(position).setAdapter(adapterHistory.get(position));
*/

        // adapterHistory.addItems(getHistory());



        return view;
    }

    private void getHistory(int position) {
        FirebaseFunctions.getHistory(this,Integer.parseInt(sensors.get(position).getId()));


    }

    private void heightOfCell(View view,int position,int height) {
        // set height of cell.
        ViewGroup.LayoutParams params = view.getLayoutParams();
        int contentHeight = view.getHeight();
        // Set the height of the Item View
        if (sensors.get(position).isOn())
            params.height = height * 4;
        else
            params.height = (int)(height * 1.4f);
        view.setLayoutParams(params);
    }

    public void addSensor(Sensor sensor){

    }

    public void changeList(List<Sensor> sensors) {
        this.sensors = sensors;

    }

    @Override
    public void ApiRequestResult(String result) {

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

    @Override
    public void historyResult(ArrayList<String> time, int id) {
        for (Sensor s : sensors) {
            if (s.getId().equals(""+id)) {
                s.setHistory(time);
                s.getAdapter().notifyDataSetChanged();
                //adapterHistory.setHistory(time);
            }
        }
        // adapterHistory.notifyDataSetChanged();
    }
}
