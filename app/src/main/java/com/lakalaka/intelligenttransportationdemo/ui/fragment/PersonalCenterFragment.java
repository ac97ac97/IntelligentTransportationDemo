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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.util.DensityUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 * 个人中心
 */

public class PersonalCenterFragment extends Fragment {
    private ViewPager mVpContext;
    private List<Fragment> list;
    private TabLayout mTlTabSelect;//与viewpager结合实现滑动切换fragment的效果

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_center, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
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
        list.add(new PersonInfoFragment());
        list.add(new TopUpLogFragment());
        list.add(new ThresholdSettingFragment());
        mVpContext.setAdapter(new FragmentAdapter(getChildFragmentManager()));//fragment中嵌套Fragment碎片管理器
        reflex(mTlTabSelect);
    }

    private void initView() {
        mVpContext = getView().findViewById(R.id.vp_context);
        mTlTabSelect = getView().findViewById(R.id.tl_tab_select);

    }

    private class FragmentAdapter extends FragmentPagerAdapter {
        private String[] titleArr = {"个人信息", "充值信息", "阀值设置"};

        public FragmentAdapter(FragmentManager fm) {
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
            return titleArr[position];
        }
    }

    public void reflex(final TabLayout tabLayout) {
        //了解源码得知线的宽度是根据 tabview的宽度来设置的
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                //拿到tabLayout的mTabStrip属性
                LinearLayout mTabStrip = (LinearLayout) tabLayout.getChildAt(0);
                int dp10 = DensityUtils.dp2px(tabLayout.getContext(), 20);
                for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                    View tabView = mTabStrip.getChildAt(i);
                    //拿到tabview的mtextview属性 tab的字数不固定一定用反射取mtextview
                    try {
                        Field mTextViewField = tabView.getClass().getDeclaredField("mTextView");
                        mTextViewField.setAccessible(true);
                        TextView mTextView = (TextView) mTextViewField.get(tabView);
                        tabView.setPadding(0, 0, 0, 0);
                        //因为我想要的效果是 字多宽线就多宽 所以 测量mtextview 的宽度
                        int width = 0;
                        width = mTextView.getWidth();
                        if (width == 0) {

                            mTextView.measure(0, 0);
                            width = mTextView.getMeasuredWidth();
                        }
                        //设置tab左右边距为10dp 注意这里不能使用padding 因为源码中线的宽度是根据 tabview 的宽度来设置的
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                        params.width = width;
                        params.leftMargin = dp10;
                        params.rightMargin = dp10;
                        tabView.setLayoutParams(params);
                        tabView.invalidate();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

    }

}
