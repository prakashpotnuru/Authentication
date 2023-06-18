package com.suneel.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    EditText email, password;
    Button login, register;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email1);
        password = findViewById(R.id.password1);
        login = findViewById(R.id.button1);
        register = findViewById(R.id.button2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginuser();

            }
        });

    }

    private void loginuser() {

        String eml, pd;
        eml = email.getText().toString();
        pd = password.getText().toString();
        if(TextUtils.isEmpty(eml)|| TextUtils.isEmpty(pd)){
            Toast.makeText(getApplicationContext(),
                    "Please enter details!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mAuth.signInWithEmailAndPassword(eml,pd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "Login successful!!",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent i = new Intent(Login.this, Home.class);
                    startActivity(i);
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Login failed!!",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }
}