package com.tuononen.petteri.phuesensor.Helper;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.tuononen.petteri.phuesensor.Activities.SensorActivationActivity;
import com.tuononen.petteri.phuesensor.BridgeUser;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;

import java.util.HashMap;
import java.util.Map;

public class FirebaseFunctions {

    public static void getFirestoreInfo(FirebaseFirestore db){
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("Firestore", document.getId() + " => " + document.getData());

                            }
                        } else {
                            Log.w("Firestore", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public static void getFireStoreToken(final Activity activity, final APIcallback callback){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("Firestore", "getInstanceId failed", task.getException());
                    return;
                }
                // Get new Instance ID token
                String token = task.getResult().getToken();
                callback.ApiRequestResultToken(token);
            }
        });
    }

    public static void addNotifications(FirebaseFirestore db, String token) {
        Log.d("firebase", "addNotifications: COMMENTEDOUT");

        Map<String, Object> note = new HashMap<>();
        note.put("msg", "Sensor activated");
        note.put("token", token);
        MySingleton store = MySingleton.getInstance();
        BridgeUser user = store.getCurrentUser();
        // Add a new document with a generated ID
        db.collection("Users").document(user.getUid()).collection("Notification").document()
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FireStore", "NOTE successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore", "Error writing document", e);
                    }
                });

    }

    public static void addUser(FirebaseFirestore db, BridgeUser currentUser, final APIcallback callback) {
        // Add a new document with a generated ID
        db.collection("Users").document(currentUser.getUid())
                .set(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("FireStore", "NOTE successfully written!");
                        callback.ApiRequestResult("OK");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FireStore", "Error writing document", e);
                    }
                });

    }

    public static void addToDevicetoUser(String token) {
        MySingleton store = MySingleton.getInstance();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("deviceTo",token);



        FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Add a new document with a generated ID
            db.collection("Users").document(store.getCurrentUser().getUid())
                    .update(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("FireStore", "NOTE successfully written!");


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("FireStore", "Error writing document", e);
                        }
                    });

    }

    public static void getToDeviceToken(FirebaseFirestore db, final SensorActivationActivity callback) {
        MySingleton store = MySingleton.getInstance();
        db.collection("Users").document(store.getCurrentUser().getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Log.d("firestore", "onComplete: " + task.getResult());
                String token = (String) task.getResult().get("deviceTo");
                callback.ApiRequestResultToDevice(token);
                int x=1;
                x=5;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });

    }
}

/*
// Get Current time
SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
Date date = new Date(System.currentTimeMillis());
*/