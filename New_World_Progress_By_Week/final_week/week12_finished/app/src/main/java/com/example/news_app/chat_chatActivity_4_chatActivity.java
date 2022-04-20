package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class chat_chatActivity_4_chatActivity extends AppCompatActivity {

    ImageView backButtonOfChatScreen;
    androidx.appcompat.widget.Toolbar mtoolbar;

    chat_viewPagerAdapter_10 viewPagerAdapter; //create instance of viewPagerAdapter
    ViewPager2 viewPager2;
    TabLayout tabLayout;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String[] titles = new String[]{"Messages"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chat4);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        viewPager2 = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);
        mtoolbar = findViewById(R.id.toolbar);

        backButtonOfChatScreen = findViewById(R.id.backButtonOfChatScreen);
        backButtonOfChatScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_chatActivity_4_chatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //1.) set up the option button
        // we need to link the id with drawable
        mtoolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);


        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_option);
        mtoolbar.setOverflowIcon(drawable);


        // create fragment to apply to chat Screen
        viewPagerAdapter =new chat_viewPagerAdapter_10(this);
        viewPager2.setAdapter(viewPagerAdapter);

        //show the name on fragment tap layout
        new TabLayoutMediator(tabLayout,viewPager2,((tab, position) -> tab.setText(titles[position]))).attach();


    }
    //2. we need to override onOptionsItemSelected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.profile:
                Intent intent = new Intent(chat_chatActivity_4_chatActivity.this,chat_ProfileActivity_5.class);
                startActivity(intent);
                break;
            case R.id.setting:

                Intent intent1 = new Intent(chat_chatActivity_4_chatActivity.this,MainActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    //3. and also override onCreateOptionsMenu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
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
             //   Toast.makeText(getApplicationContext(),"Now User is Offline", Toast.LENGTH_SHORT).show();
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
              //  Toast.makeText(getApplicationContext(),"Now User is Online", Toast.LENGTH_SHORT).show();
            }
        });
    }


}