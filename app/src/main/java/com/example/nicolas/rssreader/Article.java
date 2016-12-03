package com.example.nicolas.rssreader;

import java.net.URL;
import java.util.Date;

/**
 * Created by nicolas on 03/12/16.
 */

public class Article {

    private String title;
    private String description;
    private URL url;
    private URL picture;
    private Date pubDate;

    public Article(String title, String description, URL url, URL picture, Date pubDate){
        this.title = title;
        this.description = description;
        this.url = url;
        this.picture = picture;
        this.pubDate = pubDate;
    }

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

    public URL getPicture() {
        return picture;
    }

    public void setPicture(URL picture) {
        this.picture = picture;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

}
