package com.example.news_app;

public class chat_Message_11 {

    //8.1.1 class for messages

    String message, senderId, currentTime;
    long timeStamp;

    public chat_Message_11(){}

    public chat_Message_11(String message, String senderId, long timeStamp,String currentTime) {
        this.message = message;
        this.senderId = senderId;
        this.currentTime = currentTime;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
