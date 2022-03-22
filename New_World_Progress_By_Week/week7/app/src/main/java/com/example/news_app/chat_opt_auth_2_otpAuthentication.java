package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class chat_opt_auth_2_otpAuthentication extends AppCompatActivity {

    //step12:
    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverifyotp;
    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofotpauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_opt_auth2);

        //step13: assign XML id to java variables
        mchangenumber = findViewById(R.id.changenumber);
        mverifyotp = findViewById(R.id.verifyotp);
        mgetotp = findViewById(R.id.getotp);
        mprogressbarofotpauth = findViewById(R.id.progressbarOfotpAuth);

        firebaseAuth = FirebaseAuth.getInstance();

        //step14: if user want to change number , we need to redirect user to main activity again
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_opt_auth_2_otpAuthentication.this,chat_Welcome_Screen_1_MainActivity.class);
                startActivity(intent);
            }
        });

        mverifyotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enteredotp = mgetotp.getText().toString();
                if(enteredotp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter Your OTP First", Toast.LENGTH_SHORT).show();
                }else{
                    mprogressbarofotpauth.setVisibility(view.VISIBLE);
                    String codeRecievedFromFireBase = getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeRecievedFromFireBase,enteredotp);
                    singInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void singInWithPhoneAuthCredential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mprogressbarofotpauth.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Login success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(chat_opt_auth_2_otpAuthentication.this,chat_setProfile_3_SetProfile.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                        mprogressbarofotpauth.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }
}