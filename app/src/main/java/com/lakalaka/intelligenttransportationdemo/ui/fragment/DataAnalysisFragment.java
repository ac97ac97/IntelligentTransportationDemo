package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lakalaka.intelligenttransportationdemo.R;

/**
 * Created by Administrator on 2018/3/19.
 */

public class DataAnalysisFragment extends Fragment implements ViewPager.OnPageChangeListener {




    private Fragment fragments[] = {
            new Char1Fragment(),
            new Char2Fragment(),
            new Char3Fragment(),
            new Char4Fragment(),
            new Char5Fragment(),
            new Char6Fragment(),
            new Char7Fragment()
    };


    private MyPagerAdapter myPagerAdapter;
    private ViewPager mVpChartView;
    private LinearLayout mLlIndicator;

    public DataAnalysisFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data_analysis,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initAdapter();
        initEvent();
        selectView(0);
    }

    private void selectView(int index) {
        for (int i = 0; i < mLlIndicator.getChildCount(); i++) {
            if(index==i)
                mLlIndicator.getChildAt(i)
                .setBackgroundResource(R.drawable.dot_select);
            else
                mLlIndicator.getChildAt(i)
                .setBackgroundResource(R.drawable.dot_back);
        }
    }

    private void initEvent() {
        mVpChartView.addOnPageChangeListener(this);
    }

    private void initAdapter() {
        myPagerAdapter=new MyPagerAdapter(getChildFragmentManager());
        mVpChartView.setAdapter(myPagerAdapter);
    }

    private void initView() {

        mVpChartView = (ViewPager) getView().findViewById(R.id.vp_chart_view);
        mLlIndicator = (LinearLayout) getView().findViewById(R.id.ll_indicator);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        selectView(position);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mVpChartView.removeOnPageChangeListener(this);
    }
}
