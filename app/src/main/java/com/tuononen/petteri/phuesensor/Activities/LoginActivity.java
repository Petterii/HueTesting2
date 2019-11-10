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
import com.google.firebase.auth.FirebaseUser;
import com.tuononen.petteri.phuesensor.BridgeUser;
import com.tuononen.petteri.phuesensor.Helper.MySingleton;
import com.tuononen.petteri.phuesensor.R;

public class LoginActivity extends AppCompatActivity {

    private EditText editPsw;
    private EditText editEmail;
    private Button loginButton;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private MySingleton store;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        store = MySingleton.getInstance();
        mfirebaseAuth = FirebaseAuth.getInstance();
        editEmail = findViewById(R.id.login_email);
        editPsw = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mfirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null){
                    BridgeUser user = new BridgeUser(mFirebaseUser.getEmail(),mFirebaseUser.getUid());
                    MySingleton store = MySingleton.getInstance();
                    store.setCurrentUser(user);
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    //store.setCurrentUser(mFirebaseUser.getUid());
                    startActivity(new Intent(LoginActivity.this,MainMenuActivity.class));
                } else{
                    Toast.makeText(LoginActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };

        Button signupButton = findViewById(R.id.login_signup);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String psw = editPsw.getText().toString();
                if (email.isEmpty()){
                    editEmail.setError("Please Enter email");
                    editEmail.requestFocus();
                } else if (psw.isEmpty()){
                    editPsw.setError("Please enter your password");
                    editPsw.requestFocus();
                }else if (email.isEmpty() && psw.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Please enter Password and Email",Toast.LENGTH_LONG).show();
                } else if (!(email.isEmpty() && psw.isEmpty())){
                    mfirebaseAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login error pls login again",Toast.LENGTH_LONG).show();
                            } else {
                                startActivity(new Intent(LoginActivity.this,MainMenuActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(LoginActivity.this,"Error Ocurred",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mfirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
