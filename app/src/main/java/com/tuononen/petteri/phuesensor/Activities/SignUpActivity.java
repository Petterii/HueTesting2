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
import com.tuononen.petteri.phuesensor.R;

public class SignUpActivity extends AppCompatActivity {

    EditText editPsw;
    EditText editEmail;
    Button loginButton;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

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
                                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                            }
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this,"Error Ocurred",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
