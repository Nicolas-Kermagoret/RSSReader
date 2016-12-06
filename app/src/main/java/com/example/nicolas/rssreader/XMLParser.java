package com.example.nicolas.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nicolas on 03/12/16.
 */

public class XMLParser {

    private ArrayList<Article> articles;


    public ArrayList<Article> parse(XmlPullParser parser){

        this.articles = new ArrayList<>();

        try {

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return this.articles;

    }




    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        int eventType = parser.getEventType();
        Article currentArticle = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("item")){ //every articles are inside <item>
                        currentArticle = new Article();
                    } else if (currentArticle != null){
                        if (name.equals("link")){
                            currentArticle.setUrl(new URL(parser.nextText()));
                        } else if (name.equals("title")){
                            currentArticle.setTitle(parser.nextText());
                        } else if (name.equals("description")){
                            currentArticle.setDescription(parser.nextText());
                        } else if (name.equals("pubDate")){
                            DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm");
                            try {
                                Date date = formatter.parse(parser.nextText());
                                currentArticle.setPubDate(date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }else if (name.equals("enclosure")){

                            URL url = new URL(parser.getAttributeValue(null, "url"));

                            InputStream is = url.openStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            currentArticle.setPicture(bitmap);
                            //currentArticle.setPicture(new URL(parser.getAttributeValue(null, "url")));
                            parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && currentArticle!= null){
                        articles.add(currentArticle);
                    }
            }
            eventType = parser.next();
        }

    }


}
