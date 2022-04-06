package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class chat_ProfileActivity_5 extends AppCompatActivity {

    ImageView backButtonOfProfile;
    EditText mviewusername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    TextView mmovetoupdateprofile;

    FirebaseFirestore firebaseFirestore;

    ImageView mviewuserimageinimageview;

    StorageReference storageReference;

    private String ImageURIaccessToken;

    androidx.appcompat.widget.Toolbar mtoolbarofviewprofile;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_profile5);

        mviewuserimageinimageview = findViewById(R.id.viewUserImageInImageview);
        mviewusername = findViewById(R.id.viewUsername);
        mmovetoupdateprofile = findViewById(R.id.moveToUpdateProfile);
        mtoolbarofviewprofile = findViewById(R.id.toolbarOfViewProfileActivity);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        //3.1 setup action bar
        setSupportActionBar(mtoolbarofviewprofile);


        //3.1.2 set up the back button
        backButtonOfProfile = findViewById(R.id.backButtonOfViewProfile);
        backButtonOfProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_ProfileActivity_5.this, chat_chatActivity_4_chatActivity.class);
                startActivity(intent);
            }
        });

        //3.3 go to storage reference and update the image
        storageReference = firebaseStorage.getReference();
        //3.4 we need to make sure this path is the same as the path we set up for "set profile activity"
        // otherwise it won't work
        // we need asscess token of this image to show as our profile
        storageReference.child("Images/" + firebaseAuth.getUid().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //3.5 store URI in the imageURIaccessToken
                ImageURIaccessToken = uri.toString();

                //3.6 using Picasso to load imageView
                Picasso.get().load(uri).into(mviewuserimageinimageview);
            }
        });

        //3.7 fetch the name from the database
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        //3.8 fetch the data via snapshot
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //3.9 create instance of class "userProfile" because inside firebase we need to use
                //    class
                chat_userProfile muserprofile = snapshot.getValue(chat_userProfile.class);
                mviewusername.setText(muserprofile.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //3.10 if sth is wrong, we just give some toast
                Toast.makeText(getApplicationContext(),"Fail to Fetch username ", Toast.LENGTH_SHORT).show();
            }
        });

        //3.11 set onClick listener
        mmovetoupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_ProfileActivity_5.this,chat_UpdateProfile_6.class);

                //whenever user wants to update profile
                //we need to show their previous profile and name as well
                //so, we fetch their property and pass the data via intent
                intent.putExtra("nameOfUser",mviewusername.getText().toString());


                startActivity(intent);
            }
        });

    }
}