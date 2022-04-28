package com.example.news_app;

//4.1 snapshot the data from cloud firestore to display in the app
public class chat_firebaseModel {

    //4.2 have to make sure the variables are the same for with function sendDataToCloudFirestore()
    //    because we will fetch the data from cloudfirestore
    //4.3 then go to chatFragment

    String name, image,uid,status;

    public chat_firebaseModel(){}

    public chat_firebaseModel(String name, String image, String uid, String status) {
        this.name = name;
        this.image = image;
        this.uid = uid;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
