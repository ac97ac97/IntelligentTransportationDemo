package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lakalaka.intelligenttransportationdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无情 on 2018/3/28.
 */

public class TextZongFragment extends android.support.v4.app.Fragment {


    private ViewPager mVpContext;
    private List<Fragment> list;
    private TabLayout mTlTabSelect;//与viewpager结合实现滑动切换fragment的效果


    private LayoutInflater mInflater;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_zong, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        initAddTabs();
    }

    private void initAddTabs() {
        mTlTabSelect.setTabMode(TabLayout.MODE_FIXED);
        mTlTabSelect.setupWithViewPager(mVpContext);
        if (getSet()) {
            mVpContext.setCurrentItem(1, false);
        }
    }

    private boolean getSet() {
        Bundle bundle = getArguments();
        if (bundle == null)
            return false;
        int i = bundle.getInt("key");
        if (i == 1) {
            return true;
        } else {
            return false;
        }
    }

    private void initViewPager() {
        list = new ArrayList<>();
        list.add(new TextOneFragment());
        list.add(new TextTwoFragment());
        mVpContext.setAdapter(new TextAdapter(getChildFragmentManager()));
    }

    private void initView() {
        mVpContext = getView().findViewById(R.id.vp_jiemian);
        mTlTabSelect = getView().findViewById(R.id.tl_zong_tab_select);

    }

    private class TextAdapter extends FragmentPagerAdapter {

        private String textArr[] = {"账户阈值", "速度阈值"};

        public TextAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return textArr[position];
        }
    }


}
