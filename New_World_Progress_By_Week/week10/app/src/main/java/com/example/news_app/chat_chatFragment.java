package com.example.news_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class chat_chatFragment extends Fragment {

    //4.5 take the object firebase firestore
    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;

    //4.6 We need to set this ImageView globally so that we can access from every class
    ImageView mimageViewOfUser;

    //4.7 (Important) to fetch data from cloud fireStore we need FirestoreRecyclerAdapter
    // it takes in two paramters, one is firebaseModel class, and NoteViewHolder
    //    we need to install dependencies  "implementation 'com.firebaseui:firebase-ui-firestore:8.0.0' "
    FirestoreRecyclerAdapter<chat_firebaseModel,NoteViewHolder> chatAdapter;

    RecyclerView mRecylerView;
    chat_firebaseModel firebaseModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //4.4 we continue from firebaseModel to fetch data into recycleView
        View v = inflater.inflate(R.layout.chatfragment,container,false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mRecylerView = v.findViewById(R.id.recyleView);

        //4.8 fetch all data (all users' data) from cloud firestore
         Query query = firebaseFirestore.collection("Users");

        //4.8.1 fetch all users, except the owner of account
        //Query query = firebaseFirestore.collection("user").whereNotEqualTo("uid",firebaseAuth.getUid());

        FirestoreRecyclerOptions<chat_firebaseModel> allUserName = new FirestoreRecyclerOptions.Builder<chat_firebaseModel>().setQuery(query,chat_firebaseModel.class).build();

        //4.9 implement chat adapter
        chatAdapter = new FirestoreRecyclerAdapter<chat_firebaseModel, NoteViewHolder>(allUserName) {
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull chat_firebaseModel model) {
                //4.10 this is where we want to set our text on screen; set data on variables
                holder.particularUserName.setText(model.getName());
                String uri = model.getImage();

                Picasso.get().load(uri).into(mimageViewOfUser);
                if(model.getStatus().equals("Online")){
                    holder.statusOfUser.setText(model.getStatus());
                    holder.statusOfUser.setTextColor(Color.GREEN);
                }else{
                    holder.statusOfUser.setText(model.getStatus());
                }

                //4.11 if user clicks on other users to chat , we need to setOnClick listener fo them
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(),"Item is clicked", Toast.LENGTH_SHORT).show();
                        //pass data
                        Intent intent = new Intent(getActivity(),chat_specificChat_9.class);
                        intent.putExtra("name", firebaseModel.getName());
                        intent.putExtra("receiveUid",firebaseModel.getUid() );
                        intent.putExtra("imageuri",firebaseModel.getImage());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                //this will attach chat layout
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_chatviewlayout_7,parent,false);
                return new NoteViewHolder(view); // this will return the new objects and reset the data
            }
        };

        mRecylerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecylerView.setLayoutManager(linearLayoutManager);
        mRecylerView.setAdapter(chatAdapter);
        return v;


    }


    public class NoteViewHolder extends RecyclerView.ViewHolder{

        private TextView particularUserName;
        private TextView statusOfUser;

        public NoteViewHolder(@NonNull View itemView) {

            super(itemView);
            particularUserName = itemView.findViewById(R.id.nameofuser);
            statusOfUser = itemView.findViewById(R.id.statusofuser);
            mimageViewOfUser = itemView.findViewById(R.id.imageviewofuser);

        }
    }

    //we need to override both function to make our chatAdapter work
    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter != null) {
            chatAdapter.stopListening();
        }

    }



}
