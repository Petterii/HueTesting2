package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;

import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.jmdns.ServiceInfo;

public class FirePushNotes extends AppCompatActivity {
}

