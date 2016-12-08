package com.example.nicolas.rssreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by nicolas on 05/12/16.
 * AsyncTask to load the articles from the rss feed and then display them in a ListView
 */

public class GetFeedTask extends AsyncTask<URL, Integer, Void> {

    private ArrayList<Article> articles;
    private ListView mListView;
    private MainActivity activity;

    public GetFeedTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    //Populate article list
    protected  Void doInBackground(URL... urls) {

        this.getFeed(urls);
        return null;
    }

    @Override
    //Fill Listview with the articles from the feed
    protected void onPostExecute(Void toto){
        this.mListView = (ListView) this.activity.findViewById(R.id.listView);
        this.mListView.setAdapter(new ArticleListAdapter(this.activity, this.articles));
        saveArticles();

        //Handle click on an article, go to detailed page of the selected article
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

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

    //Read the url to get the xml from the rss feed and then parse it to populate an Article List
    public void getFeed(URL... urls){
        this.articles = new ArrayList<Article>();
        InputStream in = null;
        XMLParser parser = new XMLParser();

        for(int i=0; i<urls.length; i++){
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[i].openConnection();
                in = connection.getInputStream();

                XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = xppf.newPullParser();

                xpp.setInput(in, "utf-8");
                this.articles = parser.parse(xpp);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

        }
    }

    //save articles on the list from the rss feed to make it available when offline
    public void saveArticles(){

            ArticlesSaver saving = new ArticlesSaver(this.activity, this.articles);
            saving.execute();

    }

}