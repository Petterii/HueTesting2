package com.tuononen.petteri.phuesensor.Helper;

import com._8rine.upnpdiscovery.UPnPDevice;

public class MySingleton {

    private static MySingleton single_instance = null;

    private String userName;
    private UPnPDevice currentBridgeDevice;
    private String currentToken;

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
}
