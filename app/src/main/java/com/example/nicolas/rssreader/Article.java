package com.example.nicolas.rssreader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.net.URL;
import java.util.Date;

/**
 * Created by nicolas on 03/12/16.
 */

public class Article {

    private String title;
    private String description;
    private URL url;
    private Bitmap picture;
    private Date pubDate;

    public Article(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

}
