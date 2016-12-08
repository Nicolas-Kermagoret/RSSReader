package com.example.nicolas.rssreader;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * Created by nicolas on 03/12/16.
 */

public class Article implements Serializable {
    @Expose
    private String title;
    @Expose
    private String description;
    @Expose
    private URL url;
    @Expose(serialize = false, deserialize = false)
    private Bitmap picture;
    @Expose
    private Date pubDate;
    @Expose
    private String pictureURL;

    public Article(){

    }

    public Article(String title, String description, URL url, Bitmap picture, Date pubDate, String pictureURL) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.picture = picture;
        this.pubDate = pubDate;
        this.pictureURL = pictureURL;
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

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }


}
