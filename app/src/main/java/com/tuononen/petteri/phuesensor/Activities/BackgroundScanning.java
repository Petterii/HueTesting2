package com.tuononen.petteri.phuesensor.Activities;

import android.app.IntentService;
import android.content.Intent;

import androidx.annotation.Nullable;

public class BackgroundScanning extends IntentService {

    public BackgroundScanning() {
        super("ScanningSensors");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
