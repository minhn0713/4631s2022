package com.example.news_app;

//This class is used to send the data to the recycle view (The news part)
//we needs to extend the RecycleView.Adapter

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//Step1: extends the recycleview.Adapter
//Step2: click (alt + enter )on the ViewHodler and create inner class
public class NewsAdapter_RV extends RecyclerView.Adapter<NewsAdapter_RV.ViewHolder> {

    //Step5: create ArrayList for the article
    private ArrayList<newsArticle> newsArticleArrayList;
    private Context context;

    //Step6: create constructor for this variable
    public NewsAdapter_RV(ArrayList<newsArticle> newsArticleArrayList, Context context) {
        this.newsArticleArrayList = newsArticleArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsAdapter_RV.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Step7: put the news_rv_item: which is cardView into used
        //all syntax
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_rv,parent,false);
        return new NewsAdapter_RV.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter_RV.ViewHolder holder, int position) {
    //Step10: we distribute the data to the viewholder below
        newsArticle newsArticle = newsArticleArrayList.get(position); //get article from data that released
        holder.subTitleTV.setText(newsArticle.getDescription());
        holder.titleTV.setText(newsArticle.getTitle());
        Picasso.get().load(newsArticle.getUrlToImage()).into(holder.newsIV); //"into(holder.newsIV) = To load it into imageView"

        //Step11: create new empty activity named as NewsActivity_detailPage for the clickListener
        //Step12: create setOnClickListener(), when the user click on the readmore, it will show new activity
        //          in other word, we passing the data from current screen to the next screen
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, NewsActivity_detailPage.class);
                i.putExtra("title", newsArticle.getTitle());
                i.putExtra("content", newsArticle.getContent());
                i.putExtra("des", newsArticle.getDescription());
                i.putExtra("image", newsArticle.getUrlToImage());
                i.putExtra("url", newsArticle.getUrl());
                context.startActivity(i); //start the activity
                //step13: go to NewsDetailsActivity
            }
        });

    }

    @Override
    public int getItemCount() {
        //Step8: we count the size of the articles
        return newsArticleArrayList.size();
    }

    //Step3: inner class for ViewHolder extend Recycleview.ViewHolder

    public class ViewHolder extends RecyclerView.ViewHolder{

        //Step8: Those variables are to hold the textView and ImageView to display for news_rv_item.xml
        private TextView titleTV,subTitleTV;
        private ImageView newsIV;

        //Step4: create the constructor for this inner class
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Step9: initialize it with the id for each view
            titleTV = itemView.findViewById(R.id.idTVNewsHeadings);
            subTitleTV = itemView.findViewById(R.id.idTVSubTitle);
            newsIV = itemView.findViewById(R.id.idIVNews);
        }
    }
}


