package com.suneel.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
   // private static final int RC_SIGN_IN = 100;
    TextView btn_gmail_signin;
    EditText email, password, nummber, name;
    public static FirebaseAuth mAuth;
    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;
    Button btn_register, btn_login;
    String Tag = "SignIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.Email);
        password = findViewById(R.id.pwd);
        nummber = findViewById(R.id.Mobilenumber);
        name = findViewById(R.id.Name);
        btn_register = findViewById(R.id.register);
        btn_login = findViewById(R.id.login);
        btn_gmail_signin = findViewById(R.id.google);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("155784815572-pm61u2hdh195ptqmvmum05e9t6gc41r6.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        btn_gmail_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
//        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//        if (firebaseUser != null) {
//            startActivity(new Intent(MainActivity.this, Home.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        }


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Login.class);
                startActivity(i);
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerNewUser();
            }
        });

    }

    private void signInWithGoogle() {
        Intent i = googleSignInClient.getSignInIntent();
        startActivityForResult(i, 100);
        Log.d(Tag,"error_occured");
    }

    private void registerNewUser() {
        String eml, pd;
        eml = email.getText().toString();
        pd = password.getText().toString();
        if (TextUtils.isEmpty(eml) || TextUtils.isEmpty(pd)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter details!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(eml, pd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Registration successful!",
                            Toast.LENGTH_LONG)
                            .show();
                    Intent i = new Intent(MainActivity.this, Login.class);
                    startActivity(i);
                } else {

                    // Registration failed
                    Toast.makeText(
                            getApplicationContext(),
                            "Registration failed!!"
                                    + " Please try again later",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
//            if (resultCode == 100) {
                Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                if (signInAccountTask.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Google Sign In Successfull", Toast.LENGTH_LONG).show();

                    try {
                        GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                        if (googleSignInAccount != null) {
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                            mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // When task is successful redirect to profile activity display Toast
                                        startActivity(new Intent(MainActivity.this, Home.class));
                                        Log.e(Tag,"Error occured while signIn");
                                        Toast.makeText(getApplicationContext(), "SignIn Successfully", Toast.LENGTH_LONG).show();
                                    } else {
                                        // When task is unsuccessful display Toast
                                        Toast.makeText(getApplicationContext(), "SignIn Failed!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                }

//            }

        }


//    private void navigateToHome() {
//        finish();
//        startActivity(new Intent(MainActivity.this, Home.class));
//    }
    }
}