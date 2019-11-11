package com.tuononen.petteri.phuesensor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tuononen.petteri.phuesensor.BridgeUser;
import com.tuononen.petteri.phuesensor.Helper.FirebaseFunctions;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.Interfaces.APIcallback;
import com.tuononen.petteri.phuesensor.R;

public class SignUpActivity extends AppCompatActivity implements APIcallback {

    EditText editPsw;
    EditText editEmail;
    Button loginButton;
    FirebaseAuth firebaseAuth;
    private MySingleton store;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        store = MySingleton.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.signup_email);
        editPsw = findViewById(R.id.signup_password);

        initButton();
    }

    private void initButton() {




        loginButton = findViewById(R.id.signup_signupbutton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String psw = editPsw.getText().toString();
                if (email.isEmpty()){
                    editEmail.setError("Please Enter email");
                    editEmail.requestFocus();
                } else if (psw.isEmpty()){
                    editPsw.setError("Please enter your password");
                    editPsw.requestFocus();
                }else if (email.isEmpty() && psw.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Please enter Password and Email",Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty() && psw.isEmpty())){
                    firebaseAuth.createUserWithEmailAndPassword(email,psw).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"SignUp Unsuccessful. Please try again",Toast.LENGTH_LONG).show();
                            } else {
                                // get user,Uid and then get DeviceToken
                                String userUid = task.getResult().getUser().getUid();
                                String userEmail = task.getResult().getUser().getEmail();
                                BridgeUser user = new BridgeUser(userEmail,userUid);
                                store.setCurrentUser(user);
                                FirebaseFunctions.getFireStoreToken(SignUpActivity.this,SignUpActivity.this);

                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this,"Error Ocurred",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void ApiRequestResult(String result) {
        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
    }

    @Override
    public void ApiRequestResultTest(String response) {

    }

    @Override
    public void ApiRequestResultToken(String token) {
        store.getCurrentUser().setDeviceToken(token);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseFunctions.addUser(db,store.getCurrentUser(),this);
    }

    @Override
    public void ApiRequestResultToDevice(String token) {
        
    }
}
