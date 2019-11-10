package com.tuononen.petteri.phuesensor.Helper;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONObject;

class GsonHelper {
    public static String getError(String error) {
        String errorDiscription = "error";
        try{

            JSONObject o1 = new JSONObject(error);
            JSONObject o2 = o1.getJSONObject("error");
            errorDiscription = o1.getString("description");

        }catch (Exception e){
            Log.d("gson", "getError: " +e);
        }
        return errorDiscription;
    }

    public static String getKey(String response) {
        String key = "error";
        try{

            JSONObject o1 = new JSONObject(response.toString());
            JSONObject o2 = o1.getJSONObject("success");
            key = o1.getString("username");

        }catch (Exception e){
            Log.d("gson", "getError: " +e);
        }
        return key;
    }
}
