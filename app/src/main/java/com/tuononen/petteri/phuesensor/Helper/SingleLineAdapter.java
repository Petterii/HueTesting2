package com.tuononen.petteri.phuesensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com._8rine.upnpdiscovery.UPnPDevice;

import java.util.ArrayList;
import java.util.List;

class SingleLineAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    public static List<UPnPDevice> devices;


    public SingleLineAdapter(Context context) {
// todo your Array

        devices = new ArrayList<>();

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return devices.size();
    }

    @Override
    public Object getItem(int position) {
        return devices.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView == null)
            view = inflater.inflate(R.layout.listview_devices, parent, false);
        else
            view = convertView;



        TextView textTodoTitle = view.findViewById(R.id.list_devices_title);
        TextView textfound = view.findViewById(R.id.list_devices_status);
        TextView textIP = view.findViewById(R.id.list_devices_ip);

        textTodoTitle.setText(devices.get(position).getFriendlyName());
        textIP.setText(devices.get(position).getHostAddress());
        textfound.setText("Found1");


        return view;
    }

    public void addItem(UPnPDevice device) {
        boolean exists = false;
        for (UPnPDevice cDevices: devices) {
            if (cDevices.getHostAddress().equals(device.getHostAddress()))
                exists = true;
        }
        if (!exists){
            devices.add(device);
            notifyDataSetChanged();
        }
    }
}
