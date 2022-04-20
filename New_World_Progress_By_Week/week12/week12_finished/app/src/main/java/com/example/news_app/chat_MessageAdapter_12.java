package com.example.news_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

//This class is used for display messages between users when they chat with each other
public class chat_MessageAdapter_12 extends RecyclerView.Adapter {


    Context context;
    ArrayList<chat_Message_11> messagesArrayList; // ArrayList to hold the message

    //9.1 set Item of Sender and receiver as 1 and 2 (easy to identify who send or receive the messages )
    int ITEM_SEND = 1 ;
    int ITEM_RECIEVE = 2;

    public chat_MessageAdapter_12(Context context, ArrayList<chat_Message_11> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //9.4.3 we attach the view holders based on either sender or receiver
        if(viewType == ITEM_SEND){ //if the viewType is the sender
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat_layout,parent,false); // this line of code with bend the text and message time
            return new SenderViewHolder(view);
        }else{ // else for receiver
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat_layout,parent,false);
            return new RecieverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //9.4.4 we set the data
        chat_Message_11 messages = messagesArrayList.get(position);

        if(holder.getClass() == SenderViewHolder.class){

            //Set the text for sender
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getCurrentTime());
        }else{
            //set text for receiver
            RecieverViewHolder viewHolder = (RecieverViewHolder) holder;
            viewHolder.textViewMessage.setText(messages.getMessage());
            viewHolder.timeOfMessage.setText(messages.getCurrentTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        chat_Message_11 messages = messagesArrayList.get(position);
        //9.4.1 we check the position
        // if current Uid equal getUid, it means that that person is a sender
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }else {
            return ITEM_RECIEVE;
        }

    }

    @Override
    public int getItemCount() {
        //9.4.2 return the size of messagesArray
        return messagesArrayList.size();
    }

    //9.3 We need two view holders because we have two layouts to display in the screen
    //9.3.1 holder for sender
    class SenderViewHolder extends RecyclerView.ViewHolder
    {

        TextView textViewMessage;
        TextView timeOfMessage;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.senderMessage);
            timeOfMessage = itemView.findViewById(R.id.timeOfMessage);
        }
    }

    //9.3.2 holder for reciever
    class RecieverViewHolder extends RecyclerView.ViewHolder
    {
        TextView textViewMessage;
        TextView timeOfMessage;

        public RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.senderMessage);
            timeOfMessage = itemView.findViewById(R.id.timeOfMessage);
        }
    }
}
