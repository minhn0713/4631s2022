package com.example.news_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;

//Step33: Since we have created the interface in CategoryAdapter_RV Class
//        we need implement the interface in MainActivity.java file
public class MainActivity extends AppCompatActivity implements CategoryAdapter_RV.CategoryClickInterface{
    //77bab70f038d411583882e4711138d78

    //Step29: create variable for recycleview
    private RecyclerView newsRV;     //For horizontal RV
    private RecyclerView categoryRV; //For category RV
    private ProgressBar loadPB;
    private int flag;


    //Step31: create arrayList for categoryRVModal class
    private ArrayList<newsArticle> newsArticleArrayList;
    private ArrayList<CategoryModal_RV> categoryModalRVArrayList;
    //Step32: create variable for adapter
    private CategoryAdapter_RV categoryAdapterRV;
    private NewsAdapter_RV newsAdapterRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Step30: initialize variable
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadPB = findViewById(R.id.idPBLoading);
        //Step35: initialize arrayList
        newsArticleArrayList = new ArrayList<>();
        categoryModalRVArrayList = new ArrayList<>();
        flag =0;
        //Step36: pass the arrayList into Adapter
        newsAdapterRV = new NewsAdapter_RV(newsArticleArrayList,this);
        //                                                                             pass the onCategoryClick interface
        categoryAdapterRV = new CategoryAdapter_RV(categoryModalRVArrayList,this,this::onCategoryClick);
        //Step37: set the adapter to RV
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsAdapterRV);
        categoryRV.setAdapter(categoryAdapterRV);
        //Step40: call the getCategories method
        getCategories();
        //Step59: call the getNews method and pass "All" news and notifyDataSetChange is call for data modification
        getNews("All");
        newsAdapterRV.notifyDataSetChanged();
        //Step60: go to NewsActivity_detailPage

    }

    //Step38: Create method for getting the data for our category
    //        dak picture and imageURL for each category
    private void getCategories(){

        categoryModalRVArrayList.add(new CategoryModal_RV("All","https://media.istockphoto.com/photos/television-streaming-multimedia-wall-concept-picture-id1287677376?b=1&k=20&m=1287677376&s=170667a&w=0&h=wvu0lKn4WbfHtKId83KzrHvGmBP7zn7ZwGEWmU99HWE="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Technology","https://media.istockphoto.com/photos/abstract-science-circle-global-network-connection-in-hands-on-sunset-picture-id672310452?b=1&k=20&m=672310452&s=170667a&w=0&h=IxGtS_G2_-b4bbpn_9llft2UslYFJMfT-cPZzdzy8Mk="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Science","https://media.istockphoto.com/photos/female-chemist-at-work-in-laboratory-picture-id637785818?b=1&k=20&m=637785818&s=170667a&w=0&h=BFOAFUMdVxMWc-a3w-m_00djwLalBINEHGNHToflXMM="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Sports","https://media.istockphoto.com/photos/various-sport-equipments-on-grass-picture-id949190736?b=1&k=20&m=949190736&s=170667a&w=0&h=f3ofVqhbmg2XSVOa3dqmvGtHc4VLA_rtbboRGaC8eNo="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Business","https://media.istockphoto.com/photos/businessman-receive-counseling-from-colleague-it-is-a-picture-of-the-picture-id1271491361?b=1&k=20&m=1271491361&s=170667a&w=0&h=h3GU6-TmUtsqOfqoVpgYpxt3rO-Rx66kWLCOpymW5wI="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Entertainment","https://media.istockphoto.com/photos/the-best-fans-a-band-could-want-picture-id502131959?b=1&k=20&m=502131959&s=170667a&w=0&h=8efBzeqguArASWGYfWhK5EdgNIJvZm-nvZ42i9DsS1A="));
        categoryModalRVArrayList.add(new CategoryModal_RV("Health","https://media.istockphoto.com/photos/exercising-and-healthy-food-raibow-colored-fruits-vegetables-and-picture-id1276373624?b=1&k=20&m=1276373624&s=170667a&w=0&h=Zq8zG8hV2xmgILYtEzgES-w4Glu7vgWYhq9HttgF5ew="));
        //Step39: after link the imageUrl with adapter, we need to notify the adapter that the data inside the arrayList has been channged
        categoryAdapterRV.notifyDataSetChanged();
    }

    //Step41: Create getting news method
    private void getNews(String category){
        //Step42: display progressbar while waiting for the news to load
        loadPB.setVisibility(View.VISIBLE); //display progress bar
        //Step43: clear the articleArrayList because we need to load data based on the category which user clicked
        newsArticleArrayList.clear();
        //Step44: create url which linked by category and all
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=us&category="+category+"&apiKey=77bab70f038d411583882e4711138d78";
        String url = "https://newsapi.org/v2/top-headlines?country=us&excludeDomain=stackoverflow.com&sortBy=publishedAt&language=en&apikey=77bab70f038d411583882e4711138d78";
        //Step45: create base Url, which is the domain name
        String Base_URL = "https://newsapi.org/";
        //Step46: create the retrofit
        //addConverterFactory : it will create the data into Gson format
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //Step47: create API_retrofit API class
        API_retrofit APIRetrofit = retrofit.create(API_retrofit.class);
        //Step50: create Call newsModal using Call
        Call<News_Modal> call;
        //Step51: check if the category is "all news" or "specific news"
        if(category.equals("All")){
            call = APIRetrofit.getAllNews(url);
        }else{
            call = APIRetrofit.getNewsByCategory(categoryURL);
        }

        //Step52: we need enqueue the method by passing the call back
        call.enqueue(new Callback<News_Modal>() {
            //onResponse will be call if there is a successful response
            @Override
            public void onResponse(Call<News_Modal> call, Response<News_Modal> response) {
                //Step53: we will create the News_Modal class and pass the response
                // The response.body() contain all news modal that we need,
                // so, we pass into variable type News_Modal
                News_Modal newsModal = response.body();
                //Step54: set the Visibility as gone since we already have the data to diplay
                loadPB.setVisibility(View.GONE);
                //Step55: Get all articles in newsModal class and stored it in an array
                ArrayList<newsArticle> articles = newsModal.getArticles();
                //Step56: Paste all articles to newsArticleArrayList, which we created in the variable above
                for(int i=0; i<articles.size();i++){
                    newsArticleArrayList.add(new newsArticle(articles.get(i).getTitle()
                                                      ,articles.get(i).getDescription()
                                                      ,articles.get(i).getUrlToImage()
                                                      ,articles.get(i).getUrl()
                                                      ,articles.get(i).getContent()
                    ));
                }
                //Step57: notify the adapter
                newsAdapterRV.notifyDataSetChanged();

            }
            //onFailure will be call if there is error incurred
            @Override
            public void onFailure(Call<News_Modal> call, Throwable t) {
                //display Toast mgs if error exist
                Toast.makeText(MainActivity.this,"Fail to get news",Toast.LENGTH_LONG).show();
            }
        });


    }


    //Step34: we will get the position where the category is clicked
    //        from that we will get the category from arrayList
    //        Before start implement the onCategoryClick, we need to initialize the arrayList in the onCreate method
    @Override
    public void onCategoryClick(int position) {
        //Step58: call getNews method when user clicked
        // we passed category which getting categoryModalRVArrayList.get position and get category
        String category = categoryModalRVArrayList.get(position).getCategory();
        getNews(category);

    }
}