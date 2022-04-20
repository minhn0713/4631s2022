package com.example.news_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class chat_specificChat_9 extends AppCompatActivity {

    ImageButton backButtonOfSpecificChat;

    EditText mGetMessage;
    ImageButton mSendMessageButton;

    CardView mSendMessageCardView;
    androidx.appcompat.widget.Toolbar mToolbarOfSpecificChat;
    ImageView mImageViewOfSpecificUser;
    TextView mNameOfSpecificUser;

    private String enteredMessage;
    Intent intent;
    String mRecieverName, senderName, mRecieverUId,mSenderUId;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderRoom, recieverRoom;

    RecyclerView mMessagerRecyclerView;

    String currentTime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;

    //for Messages display purpose
    chat_MessageAdapter_12 messagesAdapter;
    ArrayList<chat_Message_11> messagesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_specific_chat9);

        //8.1 create chat room for user
        // we want to use real time database, so we need to create java class "Messages.class"for it

        backButtonOfSpecificChat = findViewById(R.id.backButtonOfSpecificChat);
        backButtonOfSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(chat_specificChat_9.this,chat_chatActivity_4_chatActivity.class);
                startActivity(intent);
            }
        });

        mGetMessage = findViewById(R.id.getMessage);
        mSendMessageCardView = findViewById(R.id.cardViewOfSendMessage);
        mSendMessageButton = findViewById(R.id.imageViewOfSendMessage);
        mToolbarOfSpecificChat = findViewById(R.id.toolbarOfSpecificChat);
        mNameOfSpecificUser = findViewById(R.id.nameOfSpecificUser);
        mImageViewOfSpecificUser = findViewById(R.id.specificImageInImageview);

        //10.1 set id for recylerView
        messagesArrayList = new ArrayList<>();
        mMessagerRecyclerView = findViewById(R.id.recycleViewOfSpecificUser);
        //10.2 customized recyleView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true); //we want the recycleview to display in reverse
        mMessagerRecyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter =new chat_MessageAdapter_12(chat_specificChat_9.this,messagesArrayList);
        mMessagerRecyclerView.setAdapter(messagesAdapter);

        intent = getIntent();
        setSupportActionBar(mToolbarOfSpecificChat);

        mToolbarOfSpecificChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Toolbar is clicked",Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm a");

        //8.2 store Uid to identify the user
        mSenderUId = firebaseAuth.getUid();

        //for reciever, we need to get uid from chat fragment by using Intent from chat_fragment class
        mRecieverUId= getIntent().getStringExtra("receiveUid");
        mRecieverName= getIntent().getStringExtra("name");

        //8.3 create room
        // logic of create chat room is that we can combine users' Uid together to generate unique id
        senderRoom = mSenderUId+mRecieverUId;
        recieverRoom = mRecieverUId+mSenderUId;

        //10.3: store all messages into our arrayList
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");
        messagesAdapter = new chat_MessageAdapter_12(chat_specificChat_9.this, messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear(); // we need to clear previous arrayList
                //display message one by one
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    chat_Message_11 messages = snapshot1.getValue(chat_Message_11.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mNameOfSpecificUser.setText(mRecieverName);

        //8.3.1 load image
        String uri = intent.getStringExtra("imageuri");

        if(uri.isEmpty()){
            Toast.makeText(getApplicationContext(),"null is clicked",Toast.LENGTH_SHORT).show();
        }else {
            Picasso.get().load(uri).into(mImageViewOfSpecificUser);
        }

        //8.4 set up send button for users to send message
        mSendMessageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                enteredMessage = mGetMessage.getText().toString();

                if(enteredMessage.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Enter Message First",Toast.LENGTH_SHORT).show();
                }else{
                    //For timestamp
                    Date date = new Date();
                    currentTime = simpleDateFormat.format(calendar.getTime());
                    //create object of class message
                    chat_Message_11 messages = new chat_Message_11(enteredMessage, firebaseAuth.getUid(),date.getTime(),currentTime);

                    //8.5 store messages in firebase_database
                    firebaseDatabase= FirebaseDatabase.getInstance();
                    //inside our firebaseDatabase we create a section call "chats"
                    // and create room for user to chat, we pass senderRoom , which contain senderUid and receiverUid
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    firebaseDatabase.getReference()
                                            .child("chats")
                                            .child(recieverRoom)
                                            .child("messages")
                                            .push()
                                            .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                }
                    });
                    //set the getMessage as null after send the messages
                    mGetMessage.setText(null);
                }
            }
        });

        //8.6 to show messages on each side of the message screen
        //        //We need an adapter class
    }
    //we need to override both function to make our chatAdapter work
    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
        //we can add online offline here as well
    }

    @Override
    public void onStop() {
        super.onStop();
        if( messagesAdapter!= null) {
            messagesAdapter.notifyDataSetChanged();
        }

    }

}