package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.RunkingInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Agoni on 2016/11/2.
 */

public class RunkingInfoAdapter extends BaseAdapter {
    private Context context;
    private List<RunkingInfo> runkingInfoList;
    public RunkingInfoAdapter(Context context,List<RunkingInfo> runkingInfoList) {
        this.context=context;
        this.runkingInfoList=runkingInfoList;
    }

    @Override
    public int getCount() {
        return runkingInfoList.size();
    }

    @Override
    public Object getItem(int position) {
        return runkingInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null){
            convertView=View.inflate(context,R.layout.ranking_layout_listview_item,null);
            holder=new ViewHolder();
            holder.img= (ImageView) convertView.findViewById(R.id.ranking_layout_listview_item_img);
            holder.tv_rankname= (TextView) convertView.findViewById(R.id.ranking_layout_listview_item_title);
            holder.tv1= (TextView) convertView.findViewById(R.id.ranking_layout_listview_item_nameandauthor1);
            holder.tv2= (TextView) convertView.findViewById(R.id.ranking_layout_listview_item_nameandauthor2);
            holder.tv3= (TextView) convertView.findViewById(R.id.ranking_layout_listview_item_nameandauthor3);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        RunkingInfo runkingInfo = runkingInfoList.get(position);
        Picasso.with(context).load(runkingInfo.getPic_s192())
                .resize(150,150)
                .centerCrop()
                .into(holder.img);
        holder.tv_rankname.setText(runkingInfo.getName());
        holder.tv1.setText(runkingInfo.getRunkingTopSonginfoList().get(0).getTitle()+" - "
                +runkingInfo.getRunkingTopSonginfoList().get(0).getAuthor());
        holder.tv2.setText(runkingInfo.getRunkingTopSonginfoList().get(1).getTitle()+" - "
                +runkingInfo.getRunkingTopSonginfoList().get(1).getAuthor());
        holder.tv3.setText(runkingInfo.getRunkingTopSonginfoList().get(2).getTitle()+" - "
                +runkingInfo.getRunkingTopSonginfoList().get(2).getAuthor());
        return convertView;
    }
    class ViewHolder {
        ImageView img;
        TextView tv_rankname;
        TextView tv1;
        TextView tv2;
        TextView tv3;
    }
}
