package com.tuononen.petteri.phuesensor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tuononen.petteri.phuesensor.Helper.BridgeAPIcalls;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;

public class BridgeUserActivity extends AppCompatActivity implements APIcallback {

    EditText userName;
    Button createUserButton;
    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_user);
        initText();
        initButtons();
    }

    private void initButtons() {
        createUserButton = findViewById(R.id.bridge_createButton);
        doneButton = findViewById(R.id.bridge_btn_done);

        createUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createUserButton.setEnabled(false);
                try {
                    BridgeAPIcalls.createUser(BridgeUserActivity.this,userName.getText().toString(),BridgeUserActivity.this);
                }catch (Exception e){
                    Log.d("Bridge", "onClick: "+e);
                }
            }
        });
    }

    private void initText() {
        userName = findViewById(R.id.bridge_user);
    }

    @Override
    public void ApiRequestResult(String result) {

        if (result.equals("Done"))
        {
            // save key and ip in sharedprefferences
            MySingleton store = MySingleton.getInstance();
            SharedPreferences.Editor editor = getSharedPreferences("MYPREFFS", Context.MODE_PRIVATE).edit();

            editor.putString("bridgeip", store.getBridgeIP().getInternalipaddress());
            editor.putString("bridgekey", store.getBridgeIP().getKey());
            editor.commit();

            // todo do I need to do anything after Bridge user is created?
            startActivity(new Intent(this,MainMenuActivity.class));
        }

    }

    @Override
    public void ApiRequestResultTest(String response) {

    }

    @Override
    public void ApiRequestResultToken(String token) {

    }
}
