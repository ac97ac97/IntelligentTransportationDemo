package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;

public class GuideActivity extends AppCompatActivity{

    private GuidePagerAdapter guidePagerAdapter;
    private ViewPager view_pager;
    private LinearLayout point_layout;
    private Button btn_skip;
    private TextView[] dots;
    private int[] layouts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();

        addBottomDots(0);

        guidePagerAdapter=new GuidePagerAdapter();
        view_pager.setAdapter(guidePagerAdapter);
        view_pager.addOnPageChangeListener(viewPagerPageChangeLisatener);



    }

    private void initView() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        point_layout = (LinearLayout) findViewById(R.id.point_layout);
        btn_skip = (Button) findViewById(R.id.btn_skip);

        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class));
                finish();
            }
        });

        layouts=new int[]{R.layout.guide_01,R.layout.guide_02,R.layout.guide_03};



    }

    private void addBottomDots(int currentpage) {
        dots=new TextView[layouts.length];

        int[] colorActive=getResources().getIntArray(R.array.array_dot_active);
        int[] colorInActive=getResources().getIntArray(R.array.array_dot_inactive);

        point_layout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i]=new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInActive[currentpage]);
            point_layout.addView(dots[i]);
        }

        if(dots.length>0){
            dots[currentpage].setTextColor(colorActive[currentpage]);
        }
    }

    private int getItem(int i){return view_pager.getCurrentItem()+i;}


    ViewPager.OnPageChangeListener viewPagerPageChangeLisatener=new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            if(position==2){
                btn_skip.setVisibility(View.VISIBLE);
            }else{
                btn_skip.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class GuidePagerAdapter extends PagerAdapter{


        private LayoutInflater layoutInflater;

        public GuidePagerAdapter(){};


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater= (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view=layoutInflater.inflate(layouts[position],container,false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view= (View) object;
            container.removeView(view);
        }
    }


}
