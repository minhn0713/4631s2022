package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class chat_Welcome_Screen_1_MainActivity extends AppCompatActivity {

    //step1:create Variable
    EditText mgetphonenumber;
    android.widget.Button msendotp;
    CountryCodePicker mcountrycodepicker;
    String countrycode;
    String phonenumber;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarofmain;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String codesent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_welcome_screen_1);

        //step2 match xml-id to variables
        mcountrycodepicker=findViewById(R.id.countrycodepicker);
        msendotp=findViewById(R.id.sendotpbutton);
        mgetphonenumber=findViewById(R.id.getphonenumber);
        mprogressbarofmain=findViewById(R.id.progressbarofmain);

        //step2.1: check if the user is log in or not
        firebaseAuth = FirebaseAuth.getInstance();
        //step2.2 store country code with + into country code variable
        //          Firebase require "+" plus signe in front of number to send OTP
        countrycode = mcountrycodepicker.getSelectedCountryCodeWithPlus();

        //step2.3; if someone want to change their country, they can change
        //         then we need to store that country code picker in the country code variable
        mcountrycodepicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode = mcountrycodepicker.getSelectedCountryCodeWithPlus();
            }
        });

        //step3: check if user enter the number after click the button, then send OTP
        msendotp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                //step3.1: create variable to store user number
                String number;
                number = mgetphonenumber.getText().toString();
                //If user doesn't input any number
                if(number.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Number",Toast.LENGTH_SHORT).show();
                }else if(number.length() < 10){ //invalid number
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Number", Toast.LENGTH_SHORT).show();
                }else{
                    //Step 4:
                    mprogressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber = countrycode + number;

                    //Step5: core of OTP
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber) //phone number that we sent the OTP
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(chat_Welcome_Screen_1_MainActivity.this)
                            .setCallbacks(mCallbacks)
                            .build();

                    //Step6: Verify Phone Number
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }
            }
        });

        //Step7: send the OTP when the verification is completed
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //how to automatically fetch code here
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
            // Enter OTP by user themselve

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                //This function will handle all code send to user
                // 's' belows represent the code which will be sent to user
                super.onCodeSent(s, forceResendingToken);
                //Step 8: We just need to give user a message when code is sent
                Toast.makeText(getApplicationContext(),"OTP is sent", Toast.LENGTH_SHORT).show();
                mprogressbarofmain.setVisibility(View.INVISIBLE);
                //Step 9: We store the OPT code to in our Codesent variable
                //        The purpose of this is that, we need to verify user input OTP with orginal OTP (in otpAuthentication)
                codesent = s;

                //Step 10: to pass value of OTP to other class, we need to use Intent
                Intent intent = new Intent(chat_Welcome_Screen_1_MainActivity.this, chat_opt_auth_2_otpAuthentication.class);
                intent.putExtra("otp",codesent); //"otp" is a key to pass between class
                startActivity(intent);
            }
        };

    }

    //Step 11: if users already Log in, we don't need to send them OTP for verification
    //         the code below will skip the whole verification process if they are already log in
    //         We just open the chat activity for them
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(chat_Welcome_Screen_1_MainActivity.this,chat_chatActivity_4_chatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}