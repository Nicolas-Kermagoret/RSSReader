package com.example.nicolas.rssreader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Format;
import java.text.SimpleDateFormat;
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
            this.article = new Article(getIntent().getStringExtra("title"), getIntent().getStringExtra("description"), new URL(getIntent().getStringExtra("url")), bmp, d, getIntent().getStringExtra("pictureURL"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ImageView img = (ImageView) this.findViewById(R.id.article_picture);
        img.setImageBitmap(bmp);

        TextView title = (TextView) this.findViewById(R.id.article_title);
        title.setText(this.article.getTitle());

        Format formatter = new SimpleDateFormat("EEE, dd MMM HH:mm");
        TextView pubDate= (TextView) this.findViewById(R.id.article_date);
        pubDate.setText(formatter.format(this.article.getPubDate()));

        TextView description= (TextView) this.findViewById(R.id.article_description);
        description.setText(this.article.getDescription());

        Button websiteButton = (Button) this.findViewById(R.id.article_url);

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(ArticleActivity.this.article.getUrl().toString());
                Intent launchBrower = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(launchBrower);
            }
        });

    }

}
