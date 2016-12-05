package com.example.nicolas.rssreader;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nicolas on 05/12/16.
 */

public class GetFeedTask extends AsyncTask<URL, Integer, Void> {

    private ArrayList<Article> articles;
    private ListView mListView;
    private MainActivity activity;
    private ListAdapter adapter;
    private List<Map<String, String>> liste;

    public GetFeedTask(MainActivity activity){
        this.activity = activity;
    }

    @Override
    protected  Void doInBackground(URL... urls) {

        this.getFeed(urls);
        this.displayArticles();

        return null;
    }

    @Override
    protected void onPostExecute(Void toto){
        //SimpleAdapter instanciation with data to display in ListView

        String[] from = {"title", "date", "image"};
        int[] to = {R.id.article_title_list, R.id.article_date_list, R.id.article_picture_list};

        this.adapter = new SimpleAdapter(this.activity, liste, R.layout.list_item, from, to);

        this.mListView.setAdapter(this.adapter);

        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                    Intent intent = new Intent(MainActivity.this.getApplicationContext(), (Class)BeerActivity.class);
//                    intent.putExtra("Beer", (Serializable)MainActivity.this.articles.get(position));
//                    MainActivity.this.startActivity(intent);

            }
        });
    }

    public void getFeed(URL... urls){
        this.articles = new ArrayList<Article>();
        InputStream in = null;
        XMLParser parser = new XMLParser();

        for(int i=0; i<urls.length; i++){ //read URL send to the task and parse them to populate Article list
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) urls[i].openConnection();
                in = connection.getInputStream();

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
    }

    public void displayArticles(){

        this.mListView = (ListView) this.activity.findViewById(R.id.listView);

        this.liste = new ArrayList<Map<String, String>>();
        HashMap<String, String> element;

        //Put title, publication date and picture of the article in a list to display

        for(int i=0; i<this.articles.size(); i++){
            element = new HashMap<String, String>();
            element.put("title",this.articles.get(i).getTitle());
            element.put("date", this.articles.get(i).getPubDate().toString());

            int picturePath = this.activity.getResources().getIdentifier("macaque", "drawable", this.activity.getPackageName());
            if (picturePath == 0){
                picturePath = this.activity.getResources().getIdentifier("macaque", "drawable", this.activity.getPackageName());
            }

            element.put("image",Integer.toString(picturePath));
            this.liste.add(element);
        }
    }

}