package com.tuononen.petteri.phuesensor;

import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseFunctions {
    FirebaseFirestore db;

    public FirebaseFunctions() {
        this.db = FirebaseFirestore.getInstance();
    }
}
