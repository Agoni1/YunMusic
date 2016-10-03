package com.example.agoni.yunmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.agoni.yunmusic.R;
import com.example.agoni.yunmusic.activity.TestActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/9/25.
 */
public class MainContentFragment extends Fragment {
    private List<Fragment> fragmentList;
    private RadioButton rb_discover,rb_music,rb_friends;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.maincontent_layout, null);
        initData();
        initView(view);
        return view;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        fragmentList=new ArrayList<>();
        fragmentList.add(new DiscoverFragment());
        fragmentList.add(new MusicFragment());
        fragmentList.add(new FriendsFragment());
    }

    /**
     * 初始化组件
     * @param view
     */
    private void initView(View view) {
        final ViewPager viewpager = (ViewPager) view.findViewById(R.id.viewPager);
        rb_discover = (RadioButton) view.findViewById(R.id.rb_discover);
        rb_music = (RadioButton) view.findViewById(R.id.rb_music);
        rb_friends = (RadioButton) view.findViewById(R.id.rb_friends);
        RadioGroup radioGroup= (RadioGroup) view.findViewById(R.id.radiogroup);

        RadioButton menu = (RadioButton) view.findViewById(R.id.rb_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TestActivity.class));
            }
        });


        //点击radiobutton时，切换相应页面
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_discover:{
                        viewpager.setCurrentItem(0);
                    }break;
                    case R.id.rb_music:{
                        viewpager.setCurrentItem(1);
                    }break;
                    case R.id.rb_friends:{
                        viewpager.setCurrentItem(2);
                    }break;
                }
            }
        });

        //为viewpager设置适配器
        viewpager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public int getCount() {
                return fragmentList.size();
            }

            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
        });

        //监听页面变化
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //判断当前的显示页面，切换radiobutton的选中状态
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:{
                        rb_discover.setChecked(true);
                    }break;
                    case 1:{
                        rb_music.setChecked(true);
                    }break;
                    case 2:{
                        rb_friends.setChecked(true);
                    }break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
}
