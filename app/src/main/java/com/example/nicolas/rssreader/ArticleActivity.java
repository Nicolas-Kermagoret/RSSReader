package com.example.nicolas.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nicolas on 06/12/16.
 */

public class ArticleActivity extends AppCompatActivity {

    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_activity);

        Bitmap bmp = null;
        String filename = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(filename);
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date d = new Date();
        d.setTime(getIntent().getLongExtra("pubDate", -1));

        try {
            this.article = new Article(getIntent().getStringExtra("title"), getIntent().getStringExtra("description"), new URL(getIntent().getStringExtra("url")), bmp, d);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


    }

}
