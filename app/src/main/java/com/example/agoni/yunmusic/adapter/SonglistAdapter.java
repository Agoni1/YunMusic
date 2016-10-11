package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.Song;

import java.util.List;

/**
 * Created by Agoni on 2016/10/10.
 */
public class SonglistAdapter extends BaseAdapter {
    private List<Song> songList;
    private Context context;

    public SonglistAdapter(Context context, List<Song> songList) {
        this.songList = songList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int position) {
        return songList.get(position);
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
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.song_order.setText((position + 1) + "");
        holder.song_name.setText(songList.get(position).getTitle());
        holder.author.setText(songList.get(position).getAuthor() + "-" +
                songList.get(position).getAlbum_title());

        //如果有MV，显示MV icon
        if (songList.get(position).getHas_mv().equals("1")){
            holder.mv_icon.setVisibility(View.VISIBLE);
        }
        //如果有超品质格式，显示SQ图标
        if (songList.get(position).getAll_rate().contains("flac")){
            holder.sq_icon.setVisibility(View.VISIBLE);
        }
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
