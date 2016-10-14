package com.example.agoni.yunmusic.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.agoni.yunmusic.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Agoni on 2016/9/25.
 */
public class DiscoverFragment extends Fragment {
    private List<Fragment> fragmentList;
    private static String[] titles = new String[]{"个性推荐", "歌单", "主播电台", "排行榜"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.discover_layout, null);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(0, new RecommendFragment());
        fragmentList.add(1, new SonglistFragment());
        fragmentList.add(2, new RadioFragment());
        fragmentList.add(3, new RankingFragment());
    }

    private void initView(View view) {
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.discover_viewpager);
        viewPager.setOffscreenPageLimit(4);

        //为viewpager设置适配器
        viewPager.setAdapter(new FragmentPagerAdapter(getFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);//将tablayout与viewpager绑定
        //tablayout监听viewpager的滚动事件
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

}
