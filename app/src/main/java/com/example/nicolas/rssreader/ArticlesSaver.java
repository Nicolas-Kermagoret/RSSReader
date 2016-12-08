package com.example.nicolas.rssreader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by nicolas on 08/12/16.
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

        for (Article article : this.articles){

            String filename = article.getTitle()+".png";
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

        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();

        Gson gson = builder.create();
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(this.activity.getApplicationContext().openFileOutput("articles.json", Context.MODE_PRIVATE));

            String data_beer = gson.toJson(this.articles);
            outputStreamWriter.write(data_beer);
            outputStreamWriter.close();


        } catch (FileNotFoundException e) {
            Log.e("Exception","Open file failed" + e.toString());
        } catch (IOException e) {
            Log.e("Exception","File write failed" + e.toString());
        }

        return null;
    }
}
