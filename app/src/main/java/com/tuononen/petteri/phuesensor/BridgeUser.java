package com.tuononen.petteri.phuesensor;

import android.util.Log;

import org.json.JSONObject;

import java.util.Iterator;

public class BridgeUser {
    private String name;
    private String email;
    private String uid;
    private String deviceToken;

    public BridgeUser(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }


    public static void usersJsonIterator(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject whitelistObject = jsonObject.getJSONObject("whitelist");


            Iterator<String> keyIterator = whitelistObject.keys();

            while (keyIterator.hasNext()) {
                String key = keyIterator.next();
                JSONObject l = whitelistObject.getJSONObject(key);

                String userKey;
                String pname;
                try {
                    pname = l.getString("name");
                    userKey = key;
                    // todo specifik user. if blabla this user?
                }catch (Exception e){
                   // Log.d("API", "mapIterator: " + e);
                }
            }
        }catch (Exception e){
        //    Log.d("API", "mapIterator: catch Error" + e);
        }

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
