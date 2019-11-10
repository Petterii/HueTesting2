package com.tuononen.petteri.phuesensor.Helper;

import com._8rine.upnpdiscovery.UPnPDevice;
import com.tuononen.petteri.phuesensor.Bridge;
import com.tuononen.petteri.phuesensor.BridgeUser;

public class MySingleton {

    private static MySingleton single_instance = null;

    private UPnPDevice currentBridgeDevice;
    private String currentToken;
    private BridgeUser currentUser;
    private Bridge bridgeIP;

    private MySingleton()
    {

    }

    public static MySingleton getInstance()
    {
        if (single_instance == null)
            single_instance = new MySingleton();
        return single_instance;
    }

        public void setCurrentBridgeDevice(UPnPDevice uPnPDevice) {
        this.currentBridgeDevice = uPnPDevice;
    }

    public UPnPDevice getCurrentBridgeDevice() {
        return currentBridgeDevice;
    }

    public void setCurrentToken(String response) {
        this.currentToken = response;
    }

    public String getCurrentToken() {
        return this.currentToken;
    }



    public BridgeUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(BridgeUser user) {
        this.currentUser = user;
    }

    public void setCurrentBridgeDeviceIP(Bridge bridge) {
        this.bridgeIP = bridge;
    }

    public Bridge getBridgeIP() {
        return bridgeIP;
    }
}
