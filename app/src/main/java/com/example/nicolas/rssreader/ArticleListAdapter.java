package com.example.nicolas.rssreader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by nicolas on 06/12/16.
 */

public class ArticleListAdapter extends BaseAdapter {

    private ArrayList listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public ArticleListAdapter(Context context, ArrayList listData) {
        this.context=context;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();
            holder.titleView= (TextView) convertView.findViewById(R.id.article_title_list);
            holder.dateView = (TextView) convertView.findViewById(R.id.article_date_list);
            holder.imageView = (ImageView) convertView.findViewById(R.id.article_picture_list);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Article article = (Article) listData.get(position);
        holder.titleView.setText(article.getTitle());
        Format formatter = new SimpleDateFormat("EEE, dd MMM HH:mm");
        holder.dateView.setText(formatter.format(article.getPubDate()));
        holder.imageView.setImageBitmap(article.getPicture());
        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        TextView dateView;
        ImageView imageView;
    }

}
