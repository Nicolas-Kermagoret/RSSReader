package com.example.nicolas.rssreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Article> articles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = new ArrayList<Article>();


    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            URL url = new URL("http://www.lemonde.fr/rss/une.xml");
            new GetFeedTask().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }



//        try{
//            URL url = new URL("http://www.ouest-france.fr/rss-en-continu.xml");
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            in = connection.getInputStream();
//            Log.d("coucou", "coucou");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private class GetFeedTask extends AsyncTask<URL, Integer, String[]> {

        private ArrayList<Article> articles;

        @Override
        protected  String[] doInBackground(URL... urls) {
            this.articles = new ArrayList<Article>();
            InputStream in = null;
            String[] rssFeed = new String[urls.length];
            XMLParser parser = new XMLParser();

            for(int i=0; i<urls.length; i++){
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) urls[i].openConnection();
                    in = connection.getInputStream();
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    byte[] buffer = new byte[1024];
//                    for (int count; (count = in.read(buffer)) != -1; ) {
//                        out.write(buffer, 0, count);
//                    }
//                    byte[] response = out.toByteArray();
//                    rssFeed[i] = new String(response, "UTF-8");
//                    Log.d("Flux RSS", rssFeed[i]);

                    XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = xppf.newPullParser();

                    xpp.setInput(in, "utf-8");
                    this.articles = parser.parse(xpp);
                    Log.d("articles", this.articles.toString());


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

            }

            return rssFeed;

        }

    }

}
