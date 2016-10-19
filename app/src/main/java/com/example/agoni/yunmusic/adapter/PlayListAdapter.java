package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.SongInfoDetail;

import java.util.List;

/**
 * Created by Agoni on 2016/10/19.
 */

public class PlayListAdapter extends BaseAdapter {
    private Context context;
    private List<SongInfoDetail> playlist;

    public PlayListAdapter(Context context, List<SongInfoDetail> playlist){
        this.context=context;
        this.playlist=playlist;
    }
    @Override
    public int getCount() {
        return playlist.size();
    }

    @Override
    public Object getItem(int position) {
        return playlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView = View.inflate(context, R.layout.playlist_item, null);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.playlist_item_songname);
            holder.author= (TextView) convertView.findViewById(R.id.playlist_item_author);
            holder.delete= (ImageButton) convertView.findViewById(R.id.playlist_item_delete);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.name.setText(playlist.get(position).getTitle());
        holder.author.setText("-"+playlist.get(position).getAuthor());
        Log.i("tag",playlist.get(position).getAuthor());
        return convertView;
    }
    class ViewHolder{
        TextView name;
        TextView author;
        ImageButton delete;
    }

}
