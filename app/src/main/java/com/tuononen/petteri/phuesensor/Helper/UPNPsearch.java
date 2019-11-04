package com.tuononen.petteri.phuesensor.Helper;

import android.app.Activity;
import android.util.Log;

import com._8rine.upnpdiscovery.UPnPDevice;
import com._8rine.upnpdiscovery.UPnPDiscovery;
import com.tuononen.petteri.phuesensor.Interfaces.UPNPcallback;

import java.util.HashSet;

public class UPNPsearch {




    public static void searchPNPDevices(Activity activity, final UPNPcallback callback){

        // devices = new ArrayList<>();
        UPnPDiscovery.discoveryDevices(activity, new UPnPDiscovery.OnDiscoveryListener() {
            @Override
            public void OnStart() {
                Log.d("UPnPDiscovery", "Starting discovery");
            }

            @Override
            public void OnFoundNewDevice(UPnPDevice device) {
                // devices.add(device);
                callback.UPNPgotDevice(device);
            }

            @Override
            public void OnFinish(HashSet<UPnPDevice> devices) {
                Log.d("UPnPDiscovery", "Finish discovery");

            }

            @Override
            public void OnError(Exception e) {
                Log.d("UPnPDiscovery", "Error: " + e.getLocalizedMessage());
            }
        });

    }
}
