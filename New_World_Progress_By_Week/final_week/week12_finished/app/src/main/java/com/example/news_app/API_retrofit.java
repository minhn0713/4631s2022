package com.example.news_app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface API_retrofit {

    //1. Making get request
    //we need to specify the modal class which is the data that we will receive from the get request

    @GET
    Call<News_Modal> getAllNews(@Url String url);

    @GET
    Call<News_Modal> getNewsByCategory(@Url String url);
}
