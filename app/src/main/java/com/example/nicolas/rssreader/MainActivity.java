package com.example.nicolas.rssreader;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ArrayList<Article> articles;
    GetFeedTask asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = new ArrayList<Article>();


    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            URL url = new URL("http://www.lemonde.fr/rss/une.xml"); //RSS fedd URL
            this.asyncTask = new GetFeedTask(this);
            this.asyncTask.execute(url); //get articles from rss feed outside the UI Thread
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

}
