package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.Songinfo;

import java.util.List;

/**
 * Created by Agoni on 2016/10/10.
 */
public class SonglistAdapter extends BaseAdapter {
    private List<Songinfo> songinfoList;
    private Context context;

    public SonglistAdapter(Context context, List<Songinfo> songinfoList) {
        this.songinfoList = songinfoList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songinfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return songinfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.songlist_listview_item_layout, null);
            holder = new ViewHolder();
            holder.song_order = (TextView) convertView.findViewById(R.id.song_order);
            holder.song_name = (TextView) convertView.findViewById(R.id.song_name);
            holder.author = (TextView) convertView.findViewById(R.id.song_author);
            holder.sq_icon = (ImageView) convertView.findViewById(R.id.image_sq);
            holder.mv_icon = (TextView) convertView.findViewById(R.id.mv_icon);
            holder.item_menu= (ImageButton) convertView.findViewById(R.id.item_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.song_order.setText((position + 1) + "");
        holder.song_name.setText(songinfoList.get(position).getTitle());
        holder.author.setText(songinfoList.get(position).getAuthor() + "-" +
                songinfoList.get(position).getAlbum_title());

        //如果有MV，显示MV icon
        if (songinfoList.get(position).getHas_mv().equals("1")){
            holder.mv_icon.setVisibility(View.VISIBLE);
        }
        //如果有超品质格式，显示SQ图标
        if (songinfoList.get(position).getAll_rate().contains("flac")){
            holder.sq_icon.setVisibility(View.VISIBLE);
        }
        holder.item_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView song_order;
        TextView song_name;
        TextView mv_icon;
        ImageView sq_icon;
        TextView author;
        ImageButton item_menu;
    }


}
