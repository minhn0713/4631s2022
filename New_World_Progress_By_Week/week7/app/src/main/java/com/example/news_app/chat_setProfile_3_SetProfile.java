package com.example.news_app;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class chat_setProfile_3_SetProfile extends AppCompatActivity {

    //Step 16: set profile
    private CardView mgetuserimage;
    private ImageView mGetUserImageInImageView;
    private static int PICK_IMAGE=123;
    Uri imagePathUri;
    ActivityResultLauncher<String> getContent;

    private EditText mgetusername;

    private android.widget.Button msaveprofile;

    private FirebaseAuth firebaseAuth;
    private String name;

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private String ImageUriAcessToken;

    private FirebaseFirestore firebaseFirestore;

    ProgressBar mProgressBarOfSetProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_set_profile3);

        //step 17: get Instant of firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        //Step 18: this storage save the profile pic of user to firebase
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        //Step19: set up the id
        mgetusername = findViewById(R.id.getusername);
        mgetuserimage = findViewById(R.id.getuserimage);
        mGetUserImageInImageView = findViewById(R.id.getuserImageInImageview);
        msaveprofile = findViewById(R.id.saveProfile);
        mProgressBarOfSetProfile = findViewById(R.id.progressbarOfSetProfile);

        //Step20: set onclick listen when user click on change profile pic
        //          syntax
        //         start activity for result
        getContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //result if result of Uri
                if(result != null) {
                    mGetUserImageInImageView.setImageURI(result);
                    imagePathUri = result; //result will be set in imageUri
                }

            }
        });

        //Step21: set onclick listen to Launch the getContent variable when user want to profile pic
        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Step22: select user's photo gallery successfully
                getContent.launch("image/*");
                //step22.1 : we need to connect our app with firebase, since we already did that, we can proceed


            }
        });

        //Step33:set user profile name
        msaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = mgetusername.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Name is Empty",Toast.LENGTH_SHORT).show();
                }
                else if(imagePathUri == null){ //imagepath equals null is that user didn't select image
                    Toast.makeText(getApplicationContext(),"Image is Empty",Toast.LENGTH_SHORT).show();
                }
                else{ //if everything is find , we will send data to firebase
                    mProgressBarOfSetProfile.setVisibility(View.VISIBLE); //start progress bar
                    sendDataForNewUser();                               //send data
                    mProgressBarOfSetProfile.setVisibility(View.INVISIBLE); //make progress bar visible
                    Intent intent = new Intent(chat_setProfile_3_SetProfile.this, chat_chatActivity_4_chatActivity.class); // move user to chat activity since they already log in
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    //step34 : create function for sendDataForNewUser()

    private void sendDataForNewUser(){
        sendDataToRealTimeDatabase();
    }

    //step35: create function for sending data to real time database
    //        **We need a class when we send data through real time database
    //          * we create userProfile class => go to userProfile class(create variables and constructor )
    private void sendDataToRealTimeDatabase(){

        //before sending data to firebase, we need to get the name from the user
        // we store in name variable
        name = mgetusername.getText().toString().trim();
        //get instance of firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //this will create the object of database reference by Uid
        // we can store data using Uid
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());

        //step 36: create instance of userProfile and pass the value to userProfile class
        chat_userProfile muserprofile = new chat_userProfile(name, firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Toast.makeText(getApplicationContext(),"User Profile Added Successfully", Toast.LENGTH_SHORT).show();

        //Step 38: this fuction will send the image to firebase storage
        sendImagetoStorage();
    }

    //Step 37: create function sendImagetoStorage
    //         The reason why we didn't pass the image inside realtime database for two reasons
    //         1) we don't want to show other users pictures when user open his profile
    //         2) we want to show all users' profile pic when they are in the chat activities
    private void sendImagetoStorage(){

        //step 39:              storage users' profile pic inside folder name Images/store Uid(it is user images)/create child name as Profile pic
        if(imagePathUri != null) {
            StorageReference imageref = firebaseStorage.getReference().child("Images/" + firebaseAuth.getUid().toString());
            //  storageReference.getStorage().getReference().child("Images/" + firebaseAuth.getUid().toString());

            //step 40: image compression => to make user images' size lower so our phone can run faster
            Bitmap bitmap = null;
            try {
                //                                         it will so if the image contain sth or not
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePathUri);
            }catch (IOException e){
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream); //compress to 25
            byte[] data = byteArrayOutputStream.toByteArray();  //store compressed image in in byte array

            //step 41: code if for putting compressed image to storage ; we store in byte size
            UploadTask uploadTask = imageref.putBytes(data);

            imageref.putBytes(data).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Image Uri is uploaded successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Image Uri is NOT uploaded successfully !", Toast.LENGTH_SHORT).show();
                    }
                }
            });

//        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                //Step42 : in this step, we already successfully upload the compressed image
//                //         now, we need to take the uri of that image to store in the cloud firestorage
//                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        ImageUriAcessToken = uri.toString(); //cover uri to string and store at ImageUriAccessToken
//                        Toast.makeText(getApplicationContext(), "URI get success", Toast.LENGTH_SHORT).show();
//                        sendDataToCloudFirestore();
//                    }
//                });
//                Toast.makeText(getApplicationContext(), "Image is Uploaded", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Image is not Uploaded", Toast.LENGTH_SHORT).show();
//            }
//        });
        }

    }

    //Step 43: send the data to cloud fire store
    private void sendDataToCloudFirestore(){
        //create the doc reference of user
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        //create Map
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name",name);
        userdata.put("image", ImageUriAcessToken); //we can retrieve the image using picasso later on
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Data on Cloud FireStore send success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Step 31: this method will help set the image selected by user
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//
//        if(resultCode == RESULT_OK){
//            //step 31: store URI of the image from user phone to app
//            imagePathUri = data.getData();
//            System.out.println(imagePathUri);
//            //step32: set the Image uri to user's profile
//            mGetUserImageInImageView.setImageURI(imagePathUri);
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }


}