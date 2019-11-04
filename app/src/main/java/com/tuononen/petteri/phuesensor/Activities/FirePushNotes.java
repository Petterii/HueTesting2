package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;

public class FirePushNotes extends AppCompatActivity implements APIcallback {


    private MySingleton store;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_push_notes);
        store = MySingleton.getInstance();
        db = FirebaseFirestore.getInstance();
        firestoreSetup();
    }

    private void firestoreSetup() {
        FirebaseFunctions.getFireStoreToken(this,this);
    }

    @Override
    public void ApiRequestResult(String result) {

    }

    @Override
    public void ApiRequestResultTest(String response) {

    }

    @Override
    public void ApiRequestResultToken(String token) {
        store.setCurrentToken(token);
    }
}
