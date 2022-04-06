package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class chat_chatActivity_4_chatActivity extends AppCompatActivity {

    ImageView backButtonOfChatScreen;
    androidx.appcompat.widget.Toolbar mtoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_chat4);

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
                Toast.makeText(getApplicationContext(),"Setting is clicked",Toast.LENGTH_SHORT).show();
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
}