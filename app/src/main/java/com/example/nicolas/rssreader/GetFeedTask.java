package com.example.nicolas.rssreader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicolas on 05/12/16.
 */

public class GetFeedTask extends AsyncTask<URL, Integer, Void> {

    private ArrayList<Article> articles;
    private ListView mListView;
    private MainActivity activity;
    private ListAdapter adapter;
    private List liste;

    public GetFeedTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected  Void doInBackground(URL... urls) {

        this.getFeed(urls);
        return null;
    }

    @Override
    protected void onPostExecute(Void toto){
        this.mListView = (ListView) this.activity.findViewById(R.id.listView);
        this.mListView.setAdapter(new ArticleListAdapter(this.activity, this.articles));
    }

    public void getFeed(URL... urls){
        this.articles = new ArrayList<Article>();
        InputStream in = null;
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
    }

}