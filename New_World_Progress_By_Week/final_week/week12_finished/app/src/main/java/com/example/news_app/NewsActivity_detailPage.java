package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class NewsActivity_detailPage extends AppCompatActivity {

    //Step14: getting data inside NewsActivity_detailPage
    //Step15: create variable
    String title,desc,content,imageURL,url;

    //Step61: after set up the activity_news_detail.xml for layout
    //        we will create new variables to linked to that xml for output purpose
    private TextView titleTV, subDescTV, contentTV;
    private ImageView newsTV;
    private Button readNewsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        //Step16: getting Data into the NewsActivity_detailPage by getStringExtra and fill in their names
        //                                      key is from NewsAdapter_RV class, which we put putExtra
        title = getIntent().getStringExtra("title");
        desc  = getIntent().getStringExtra("desc");
        content = getIntent().getStringExtra("content");
        imageURL = getIntent().getStringExtra("image");
        url = getIntent().getStringExtra("url");

        //step17: go to => work on the categoryRVAdapter

        //Step62:initialize the variable
        titleTV   = findViewById(R.id.idTVTitle);
        subDescTV = findViewById(R.id.idTVSubDes);
        contentTV = findViewById(R.id.idTVContent);
        newsTV    = findViewById(R.id.idIVNews);
        readNewsBtn= findViewById(R.id.idBtnReadNews);

        //Step63: set the text from the variable
        titleTV.setText(title);
        subDescTV.setText(desc);
        contentTV.setText(content);
        Picasso.get().load(imageURL).into(newsTV);

        //Step64: setOnclickListener
        readNewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Step65: create intent
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url)); //pass the url to set the data
                startActivity(i);          //start the activity
            }
        });
    }
}