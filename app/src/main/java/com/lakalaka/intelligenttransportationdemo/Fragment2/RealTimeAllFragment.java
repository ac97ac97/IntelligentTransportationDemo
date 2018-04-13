package com.lakalaka.intelligenttransportationdemo.Fragment2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.ui.activity.MainActivity;

/**
 * Created by lakalaka on 2018/3/29/0029.
 */

public class RealTimeAllFragment extends Fragment implements ViewPager.OnPageChangeListener {

    private ViewPager real_viewpager;
    private LinearLayout real_layout;
    private ImageView real_image;
    private int p;
    private Fragment fragment[] = {
            new Co2Fragment2(), new LightFragment2(),
            new TemperatureFragment2(), new StatusFragment(),
            new PM2_5Fragment2(),new busFragment()};

    private MyPagerAdapter myPagerAdapter;
    public static LinearLayout ll_realtime_title;

    private class MyPagerAdapter extends FragmentPagerAdapter{

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragment[position];
        }

        @Override
        public int getCount() {
            return fragment.length;
        }
    }

    public RealTimeAllFragment(int postion){
        p=postion;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.realtimeall_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        ll_realtime_title = getView().findViewById(R.id.title_realtime);
        real_viewpager=getView().findViewById(R.id.real_viewpager);
        real_layout=getView().findViewById(R.id.real_layout);
        real_image=getView().findViewById(R.id.back_fragment);
        real_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE).replace(R.id.fl_fragment_group,
                        new RealTimeFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initAdapter();
        initEvent();
        real_viewpager.setCurrentItem(p);
        selectView(p);

    }

    private void selectView(int index) {
        for (int i = 0; i < real_layout.getChildCount(); i++) {
            if(index==i){
                real_layout.getChildAt(i)
                        .setBackgroundResource(R.drawable.dot_select);
            }else{
                real_layout.getChildAt(i)
                        .setBackgroundResource(R.drawable.dot_back);
            }
        }
    }

    private void initEvent() {
        real_viewpager.addOnPageChangeListener(this);
    }

    private void initAdapter() {
        myPagerAdapter=new MyPagerAdapter(getChildFragmentManager());
        real_viewpager.setAdapter(myPagerAdapter);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        real_viewpager.removeOnPageChangeListener(this);
    }
}
