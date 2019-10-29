package com.tuononen.petteri.phuesensor.Activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tuononen.petteri.phuesensor.R;

class SensorListAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    public SensorListAdapter(Context context) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = null;
        if (convertView == null)
            view = inflater.inflate(R.layout.listview_devices, viewGroup, false);
        else
            view = convertView;

        return view;
    }
}
