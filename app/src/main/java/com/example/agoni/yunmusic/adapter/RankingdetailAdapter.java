package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.RankingDetailInfo;
import com.example.agoni.yunmusic.bean.RankingSonginfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Agoni on 2016/11/5.
 */

public class RankingdetailAdapter extends BaseAdapter {
    private Context context;
    private List<RankingSonginfo> rankingSonginfoList;


    public RankingdetailAdapter(Context context, RankingDetailInfo rankingDetailInfo) {
        this.context=context;
        this.rankingSonginfoList = rankingDetailInfo.getRankingSonginfoList();
    }

    @Override
    public int getCount() {
        return rankingSonginfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return rankingSonginfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.rangkingdetail_listview_item_layout, null);
            holder = new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.rankingdetail_listview_item_img);
            holder.song_name = (TextView) convertView.findViewById(R.id.rankingdetail_listview_item_song_name);
            holder.author = (TextView) convertView.findViewById(R.id.rankingdetail_listview_item_song_author);
            holder.sq_icon = (ImageView) convertView.findViewById(R.id.rankingdetail_listview_item_image_sq);
            holder.mv_icon = (TextView) convertView.findViewById(R.id.rankingdetail_listview_item_mv_icon);
            holder.item_menu= (ImageButton) convertView.findViewById(R.id.rankingdetail_listview_item_menu);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context).load(rankingSonginfoList.get(position).getPic_small()).into(holder.img);
        holder.song_name.setText(rankingSonginfoList.get(position).getTitle());
        holder.author.setText(rankingSonginfoList.get(position).getAuthor() + " - " +
                rankingSonginfoList.get(position).getAlbum_title());

        //如果有MV，显示MV icon
        if ((rankingSonginfoList.get(position).getHas_mv()).equals("1")){
            holder.mv_icon.setVisibility(View.VISIBLE);
        }else {
            holder.mv_icon.setVisibility(View.GONE);//一定要进行设置，将view恢复成初始状态，不然复用时可能出错
        }
        //如果有超品质格式，显示SQ图标
        if (rankingSonginfoList.get(position).getAll_rate().contains("flac")){
            holder.sq_icon.setVisibility(View.VISIBLE);
        }else {
            holder.sq_icon.setVisibility(View.GONE);//同上
        }
        holder.item_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView img;
        TextView song_name;
        TextView mv_icon;
        ImageView sq_icon;
        TextView author;
        ImageButton item_menu;
    }
}
