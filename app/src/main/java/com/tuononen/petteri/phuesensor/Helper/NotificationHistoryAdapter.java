package com.tuononen.petteri.phuesensor.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com._8rine.upnpdiscovery.UPnPDevice;
import com.tuononen.petteri.phuesensor.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationHistoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<String> history;


    public NotificationHistoryAdapter(Context context) {
// todo your Array

        history = new ArrayList<>();

        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (history.size() > 3)
            return  3;
        return history.size();
    }

    @Override
    public Object getItem(int position) {
        return history.get(position);
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
            view = inflater.inflate(R.layout.listview_notehistory, parent, false);
        else
            view = convertView;

        TextView textTodoTitle = view.findViewById(R.id.scanninglist_history_string);
        textTodoTitle.setText(history.get(position));

        return view;
    }

    public void addItem(String msg) {
        history.add(msg);
    }

    public void setHistory(ArrayList<String> time) {
        this.history = time;
    }
}
