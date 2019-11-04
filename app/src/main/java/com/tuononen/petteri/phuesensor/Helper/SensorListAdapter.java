package com.tuononen.petteri.phuesensor.Helper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tuononen.petteri.phuesensor.R;
import com.tuononen.petteri.phuesensor.Sensor;

import java.util.ArrayList;
import java.util.List;

import static androidx.annotation.InspectableProperty.ValueType.COLOR;

public class SensorListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Sensor> sensors;

    public SensorListAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        sensors = new ArrayList<>();

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
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (convertView == null)
            view = inflater.inflate(R.layout.listview_sensors, viewGroup, false);
        else
            view = convertView;

        TextView name = view.findViewById(R.id.list_sensors_name);
        TextView nr = view.findViewById(R.id.list_sensor_nr);

        name.setText(sensors.get(position).getName());
        nr.setText(""+sensors.get(position).getPresence());

        if (sensors.get(position).getPresence().equals("true"))
            view.setBackgroundColor(Color.RED);
        else
            view.setBackgroundColor(Color.GREEN);



        return view;
    }

    public void addSensor(Sensor sensor){

    }

    public void changeList(List<Sensor> sensors) {
        this.sensors = sensors;

    }
}
