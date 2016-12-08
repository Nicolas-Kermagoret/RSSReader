package com.example.nicolas.rssreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

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
        saveArticles();

        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d("Item number",Integer.toString(position));

                Intent intent = new Intent(GetFeedTask.this.activity.getApplicationContext(), (Class)ArticleActivity.class);

                String filename = "bitmap.png";
                FileOutputStream stream = null;
                try {
                    stream =GetFeedTask.this.activity.openFileOutput(filename, Context.MODE_PRIVATE);
                    GetFeedTask.this.articles.get(position).getPicture().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    stream.close();

                    //Pop intent
                    intent.putExtra("image", filename);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                intent.putExtra("title", GetFeedTask.this.articles.get(position).getTitle());
                intent.putExtra("description", GetFeedTask.this.articles.get(position).getDescription());
                intent.putExtra("pubDate", GetFeedTask.this.articles.get(position).getPubDate().getTime());
                intent.putExtra("url", GetFeedTask.this.articles.get(position).getUrl().toString());
                intent.putExtra("pictureURL", GetFeedTask.this.articles.get(position).getPictureURL());

                GetFeedTask.this.activity.startActivity(intent);

            }
        });

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

    public void saveArticles(){

            ArticlesSaver saving = new ArticlesSaver(this.activity, this.articles);
            saving.execute();

    }

}