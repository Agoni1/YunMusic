package com.example.agoni.yunmusic.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.bean.RecommendRadio;
import com.example.agoni.yunmusic.util.MD5;

import java.util.List;

/**
 * Created by Agoni on 2016/10/6.
 */
public class RecommendRadioAdapter extends BaseAdapter {
    private Context context;
    private List<RecommendRadio> recommendRadioList;
    public RecommendRadioAdapter(Context context, List<RecommendRadio> recommendRadioList){
        this.context=context;
        this.recommendRadioList=recommendRadioList;
    }
    @Override
    public int getCount() {
        return recommendRadioList.size();
    }

    @Override
    public Object getItem(int position) {
        return recommendRadioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder ;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=View.inflate(context, R.layout.gridview_item_layout,null);
            viewHolder.img= (ImageView) convertView.findViewById(R.id.gridview_item_img);
            viewHolder.title= (TextView) convertView.findViewById(R.id.gridview_item_title);
            viewHolder.author= (TextView) convertView.findViewById(R.id.gridview_item_author);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        try {
            viewHolder.img.setImageBitmap(BitmapFactory.decodeFile(context.getExternalCacheDir().getAbsolutePath()+
                 "/"+MD5.md5Encode(recommendRadioList.get(position).getPic())));
            viewHolder.title.setText(recommendRadioList.get(position).getTitle());
            viewHolder.author.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }
    class ViewHolder{
        ImageView img;
        TextView title;
        TextView author;
    }
}
