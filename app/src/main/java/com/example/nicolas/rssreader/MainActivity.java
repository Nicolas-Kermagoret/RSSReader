package com.example.nicolas.rssreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Article> articles;

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
            new GetFeedTask().execute(url); //get articles from rss feed outside the UI Thread
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }


    private class GetFeedTask extends AsyncTask<URL, Integer, String[]> {

        private ArrayList<Article> articles;

        @Override
        protected  String[] doInBackground(URL... urls) {
            this.articles = new ArrayList<Article>();
            InputStream in = null;
            String[] rssFeed = new String[urls.length];
            XMLParser parser = new XMLParser();

            for(int i=0; i<urls.length; i++){ //read URL send to the task and parse them to populate Article list
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) urls[i].openConnection();
                    in = connection.getInputStream();

                    XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = xppf.newPullParser();

                    xpp.setInput(in, "utf-8");
                    this.articles = parser.parse(xpp);
                    Log.d("articles", this.articles.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }

            return rssFeed;

        }

    }

}
