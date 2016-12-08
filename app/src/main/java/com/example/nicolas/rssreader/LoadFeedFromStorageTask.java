package com.example.nicolas.rssreader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by nicolas on 08/12/16.
 */

public class LoadFeedFromStorageTask extends AsyncTask<Void, Integer, Void> {

    private ArrayList<Article> articles;
    private ListView mListView;
    private MainActivity activity;
    private ListAdapter adapter;
    private List liste;
    private boolean dataToLoad = true;

    public LoadFeedFromStorageTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected  Void doInBackground(Void... params) {

        this.getFeed();
        return null;
    }

    @Override
    protected void onPostExecute(Void toto){
        if (!dataToLoad){
            TextView noInternet = (TextView) this.activity.findViewById(R.id.no_internet);
            noInternet.setVisibility(View.VISIBLE);
        }
        else{
            this.mListView = (ListView) this.activity.findViewById(R.id.listView);
            this.mListView.setAdapter(new ArticleListAdapter(this.activity, this.articles));

            this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Log.d("Item number",Integer.toString(position));

                    Intent intent = new Intent(LoadFeedFromStorageTask.this.activity.getApplicationContext(), (Class)ArticleActivity.class);

                    String filename = "bitmap.png";
                    FileOutputStream stream = null;
                    try {
                        stream =LoadFeedFromStorageTask.this.activity.openFileOutput(filename, Context.MODE_PRIVATE);
                        LoadFeedFromStorageTask.this.articles.get(position).getPicture().compress(Bitmap.CompressFormat.PNG, 100, stream);
                        stream.close();

                        //Pop intent
                        intent.putExtra("image", filename);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    intent.putExtra("title", LoadFeedFromStorageTask.this.articles.get(position).getTitle());
                    intent.putExtra("description", LoadFeedFromStorageTask.this.articles.get(position).getDescription());
                    intent.putExtra("pubDate", LoadFeedFromStorageTask.this.articles.get(position).getPubDate().getTime());
                    intent.putExtra("url", LoadFeedFromStorageTask.this.articles.get(position).getUrl().toString());

                    LoadFeedFromStorageTask.this.activity.startActivity(intent);

                }
            });
        }
    }

    public void getFeed(){
        this.articles = new ArrayList<Article>();
        String articlesJson = null;

        Log.d("Folder", this.activity.getFilesDir().getAbsolutePath());

        File file = new File(this.activity.getFilesDir().getAbsolutePath()+"/articles.json");

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();

        Gson gson = builder.create();


        try {
            FileInputStream stream = new FileInputStream(file);

            FileChannel fc = stream.getChannel();
            MappedByteBuffer bb = null;
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            articlesJson = Charset.defaultCharset().decode(bb).toString();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Article[] articlesfromJson= gson.fromJson(articlesJson, Article[].class);

        if (articlesfromJson != null){

            for(Article articlejson : articlesfromJson){
                this.articles.add(articlejson);
            }

            File folder = new File(this.activity.getFilesDir().getAbsolutePath());


            for (Article article : this.articles) {
                Bitmap bmp = null;
                String url = article.getPictureURL();
                String filename = url.substring(url.lastIndexOf("/") + 1);

                try {
                    FileInputStream is = this.activity.openFileInput(filename);
                    bmp = BitmapFactory.decodeStream(is);
                    is.close();
                    article.setPicture(bmp);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        else{ this.dataToLoad=false; }

    }


}