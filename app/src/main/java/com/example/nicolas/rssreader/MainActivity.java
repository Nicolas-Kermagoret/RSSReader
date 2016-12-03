package com.example.nicolas.rssreader;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public void onStart(){
        super.onStart();
        try {
            URL url = new URL("http://www.ouest-france.fr/rss-en-continu.xml");
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

        @Override
        protected  String[] doInBackground(URL... urls) {
            InputStream in = null;
            String[] rssFeed = new String[urls.length];

            for(int i=0; i<urls.length; i++){
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) urls[i].openConnection();
                    in = connection.getInputStream();
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    for (int count; (count = in.read(buffer)) != -1; ) {
                        out.write(buffer, 0, count);
                    }
                    byte[] response = out.toByteArray();
                    rssFeed[i] = new String(response, "UTF-8");
                    Log.d("Flux RSS", rssFeed[i]);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return rssFeed;

        }

    }

}
