package com.example.nicolas.rssreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by nicolas on 08/12/16.
 * Class to save articles from the rss feed on the internal storage
 */

public class ArticlesSaver extends AsyncTask<Void, Integer, Void>{

    MainActivity activity;
    ArrayList<Article> articles;

    public ArticlesSaver(MainActivity activity, ArrayList<Article> articles){
        this.activity=activity;
        this.articles=articles;
    }

    @Override
    protected Void doInBackground(Void... params) {

        //Delete previous saved articles
        try {
            File folder = new File(this.activity.getFilesDir().getAbsolutePath());
            FileUtils.deleteDirectory(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Save every picture in a seperate file, the picture name is a part of the picture URL
        for (Article article : this.articles){
            String url = article.getPictureURL();
            String filename = url.substring(url.lastIndexOf("/")+1);
            FileOutputStream stream = null;
            try {
                stream =ArticlesSaver.this.activity.openFileOutput(filename, Context.MODE_PRIVATE);
                article.getPicture().compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

        }

        //GsonBuilder to exclude the bitmap of the article in the json file
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();


        Gson gson = builder.create();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.activity.getApplicationContext().openFileOutput("articles.json", Context.MODE_PRIVATE));

            String data_article = gson.toJson(this.articles);
            outputStreamWriter.write(data_article);
            outputStreamWriter.close();


        } catch (FileNotFoundException e) {
            Log.e("Exception","Open file failed" + e.toString());
        } catch (IOException e) {
            Log.e("Exception","File write failed" + e.toString());
        }

        return null;
    }
}
