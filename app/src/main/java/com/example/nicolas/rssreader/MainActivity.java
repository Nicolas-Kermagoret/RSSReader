package com.example.nicolas.rssreader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    GetFeedTask asyncTask;
    boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.checkInternetConnection();

        this.getFeed();

        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                checkInternetConnection();
                getFeed();
                swipeContainer.setRefreshing(false);
            }
        });


        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }

    public void getFeed(){
        if(!this.isConnected){
            Toast toast = Toast.makeText(this.getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT);
            toast.show();
            LoadFeedFromStorageTask loadTask = new LoadFeedFromStorageTask(this);
            loadTask.execute();
        }
        else{
            try {
                URL url = new URL("http://www.lemonde.fr/rss/une.xml"); //RSS feed URL
                this.asyncTask = new GetFeedTask(this);
                this.asyncTask.execute(url); //get articles from rss feed outside the UI Thread
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        this.isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
