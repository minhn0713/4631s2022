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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class chat_UpdateProfile_6 extends AppCompatActivity {

    ImageView backButtonOfUpdateProfile;

    private EditText mNewUserName;
    private CardView mNewUserImage;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseDatabase firebaseDatabase;

    private ImageView mGetNewImageInImageView;

    private StorageReference storageReference;

    private String ImageURIaccessToken;
    Uri newImagePathUri;
    ActivityResultLauncher<String> getNewContent;

    private androidx.appcompat.widget.Toolbar mToolbarOfUpdateProfile;
    private android.widget.Button mUpdateProfileButton;
    private ImageButton mBackButtonOfUpdateProfile;

    private FirebaseStorage firebaseStorage;

    ProgressBar mProgressBarOfUpdateProfile;
    private Uri imagePath;


    Intent intent;
    private static int PICK_IMAGE = 123;
    String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_update_profile6);

        mToolbarOfUpdateProfile = findViewById(R.id.toolbarOfUpdateProfile);

        mGetNewImageInImageView = findViewById(R.id.getNewUserImageInImageview);
        mProgressBarOfUpdateProfile = findViewById(R.id.progressBarOfUpdateProfile);
        mNewUserName = findViewById(R.id.getNewUserName);
        mUpdateProfileButton = findViewById(R.id.updateProfileButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
       // db = FirebaseFirestore.getInstance();

        intent = getIntent();
        //extra
        mNewUserImage = findViewById(R.id.getNewUserimage);
        setSupportActionBar(mToolbarOfUpdateProfile);

        //set button
        backButtonOfUpdateProfile = findViewById(R.id.backButtonOfUpdateProfile);
        backButtonOfUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_UpdateProfile_6.this,chat_ProfileActivity_5.class);
                startActivity(intent);
            }
        });


        //6.3 we need to load the prev user name before update the new name
        mNewUserName.setText(intent.getStringExtra("nameOfUser"));

        //6.2) Access the reference of user to make change to their profile
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        mUpdateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //6.4: apply conditions when user tries to update their profile
                //check if the name is empty or not
                newName = mNewUserName.getText().toString();
                //1st condition: if user don't fill out the name .
                if(newName.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Name is Empty", Toast.LENGTH_SHORT).show();
                }// 2nd condition: it means that user selects new image
                else if(imagePath != null){
                    mProgressBarOfUpdateProfile.setVisibility(View.VISIBLE);
                    chat_userProfile mUserProfile = new chat_userProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(mUserProfile); //this will update the details such name or image regarding new update or not

                    //create updateImage to storage method
                    updateImageToStorage();

                    Toast.makeText(getApplicationContext(),"Updated", Toast.LENGTH_SHORT).show();
                    mProgressBarOfUpdateProfile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(chat_UpdateProfile_6.this, chat_chatActivity_4_chatActivity.class);
                    startActivity(intent);
                    finish();

                }//3rd condition: user just wants to update the name but not image, in this the image is equal to Null
                else{
                    //create new user profile
                    mProgressBarOfUpdateProfile.setVisibility(View.VISIBLE);
                    chat_userProfile mUserProfile = new chat_userProfile(newName,firebaseAuth.getUid());
                    databaseReference.setValue(mUserProfile);

                    //call method update the name on Cloud firestore
                    updateNameOnCloudFirestore();
                    Toast.makeText(getApplicationContext(),"Updated name on cloud firestore", Toast.LENGTH_SHORT).show();
                    mProgressBarOfUpdateProfile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(chat_UpdateProfile_6.this,chat_chatActivity_4_chatActivity.class);
                    startActivity(intent);
                    finish();

                }

            }
        });

        //Re-select the image by user
        getNewContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //result if result of Uri
                if(result != null) {
                    mGetNewImageInImageView.setImageURI(result);
                    newImagePathUri = result; //result will be set in imageUri
                }

            }
        });

        mNewUserImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getNewContent.launch("image/*");
            }
        });

        //show prev image for user before allows them to update
        storageReference =firebaseStorage.getReference();
        storageReference.child("Images/" + firebaseAuth.getUid().toString()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURIaccessToken = uri.toString();
                Picasso.get().load(uri).into(mGetNewImageInImageView);

            }
        });
    }

    private void updateNameOnCloudFirestore() {

        //link the path to collection
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());

        //override the existing data by using newName and ImageURIaccessToken
        Map<String, Object> userdata = new HashMap<>();
        userdata.put("name",newName);
        userdata.put("image", ImageURIaccessToken); //we can retrieve the image using picasso later on
        userdata.put("uid",firebaseAuth.getUid());
        userdata.put("status","Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "Profile is updated Successfully", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void updateImageToStorage() {

        //step 39:              storage users' profile pic inside folder name Images/store Uid(it is user images)/create child name as Profile pic
        if(newImagePathUri != null) {
            StorageReference imageref = firebaseStorage.getReference().child("Images/" + firebaseAuth.getUid().toString());
            //  storageReference.getStorage().getReference().child("Images/" + firebaseAuth.getUid().toString());

            //step 40: image compression => to make user images' size lower so our phone can run faster
            Bitmap bitmap = null;
            try {
                //                                         it will so if the image contain sth or not
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), newImagePathUri);
            }catch (IOException e){
                e.printStackTrace();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream); //compress to 25
            byte[] data = byteArrayOutputStream.toByteArray();  //store compressed image in in byte array

            //step 41: code if for putting compressed image to storage ; we store in byte size
            UploadTask uploadTask = imageref.putBytes(data);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Step42 : in this step, we already successfully upload the compressed image
                    //         now, we need to take the uri of that image to store in the cloud firestorage
                    imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageURIaccessToken = uri.toString(); //cover uri to string and store at ImageUriAccessToken
                            Toast.makeText(getApplicationContext(), "URI get success", Toast.LENGTH_SHORT).show();

                            //update name and assessToken
                            updateNameOnCloudFirestore();
                        }
                    });
                    Toast.makeText(getApplicationContext(), "Image is Updated", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Image is Not Updated", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //7.1 update online or offline
    //set offline method when users stop using the app
    @Override
    protected void onStop() {
        super.onStop();
        //7.2 go to the path of data from FirebaseFireStore
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        //7.3 update the value of online status
        documentReference.update("status","Offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Now User is Offline", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //set Online method when users start using the app
    @Override
    protected void onStart() {
        super.onStart();
        //7.4 go to the path of data from FirebaseFireStore
        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        //7.5 update the value of online status
        documentReference.update("status","Online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"Now User is Online", Toast.LENGTH_SHORT).show();
            }
        });
    }

}