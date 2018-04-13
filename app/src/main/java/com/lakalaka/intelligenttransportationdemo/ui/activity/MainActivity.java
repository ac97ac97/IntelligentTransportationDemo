package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lakalaka.intelligenttransportationdemo.Fragment2.RealTimeFragment;
import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.LeftMenu;
import com.lakalaka.intelligenttransportationdemo.adapter.LeftMenuAdapter;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.AccountMangerFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.BusFindFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.CreativeFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.DataAnalysisFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.IllegalFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.LampFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.LifeAssistantFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.PersonalCenterFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.TextZongFragment;
import com.lakalaka.intelligenttransportationdemo.ui.fragment.TrafficQueryFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AccountMangerFragment.OnFragmentInteractionListener{
    public static DrawerLayout drawerLayout;
    public static Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private List<LeftMenu> leftMenus= new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (Toolbar) findViewById(R.id.tb_toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.dl_drawerlayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);//设置可以通过点击menu图片进行打开与关闭子侧滑menu
        initActionBarDrawerToggle();//开启与关闭 子menu
//        initViews();//初始化5 个 fragment 相关布局

        ListView listView = (ListView) findViewById(R.id.lv_left_menu);
        initData();
        LeftMenuAdapter adapter = new LeftMenuAdapter(this,leftMenus);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();//关闭侧滑
                        toolbar.setTitle("路况查询");//设置 toolbar 的顶部标题
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new TrafficQueryFragment()).commitAllowingStateLoss();
                        break;
                    case 1:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("车辆查询");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new IllegalFragment()).commitAllowingStateLoss();
                        break;

                    //管理员权限
                    case 2:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("公交查询");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new BusFindFragment()).commitAllowingStateLoss();
                        break;
                    case 3:
                        toolbar.setVisibility(View.GONE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("账户管理");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new AccountMangerFragment()).commitAllowingStateLoss();
                    break;
                    case 4:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("红路灯管理");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new LampFragment()).commitAllowingStateLoss();
                        break;
                    case 5:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("个人中心");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new PersonalCenterFragment()).commitAllowingStateLoss();
                        break;
                    case 6:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("生活助手");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new LifeAssistantFragment()).commitAllowingStateLoss();
                        break;
                    case 7:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("数据分析");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new DataAnalysisFragment()).commitAllowingStateLoss();
                        break;
                    case 8:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("创意题");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new CreativeFragment()).commitAllowingStateLoss();
                        break;
                    case 9:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("测试");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new TextZongFragment()).commitAllowingStateLoss();
                        break;
                    case 10:
                        toolbar.setVisibility(View.VISIBLE);
                        drawerLayout.closeDrawers();
                        toolbar.setTitle("环境指标");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new RealTimeFragment()).commitAllowingStateLoss();
                        break;

                    default:
                }
            }
        });

        drawerLayout.addDrawerListener(drawerListener);//移动的侧滑
        drawerLayout.setScrimColor(Color.TRANSPARENT);//设置侧滑子菜单 之外的 颜色(透明)
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_group, new TrafficQueryFragment()).commitAllowingStateLoss();

    }

    /**
     * 侧滑以平移展示
     */
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            View content = drawerLayout.getChildAt(0);
            int offset = (int) (drawerView.getWidth() * slideOffset);
            content.setTranslationX(offset);
        }
    };

    /**
     * 监听home 的 开启侧滑与关闭侧滑
     */
    private void initActionBarDrawerToggle() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar, R.string.open, R.string.close);
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }

    private void initData(){
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon1,"路况查询"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon2,"车辆违章"));


        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon7,"公交查询"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon3,"账户管理"));

        if ("管理员".equals(AppClient.getString("identity")))
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon4,"红绿灯管理"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon5,"个人中心"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon8,"生活助手"));


        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon9,"数据分析"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon6,"创意题"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon3,"测试"));
        leftMenus.add(new LeftMenu(R.drawable.menu_item_icon6,"环境指标"));

    }




    @Override
    public void onFragmentInteraction(Message object) {
        if (object.what == 1) {

            drawerLayout.openDrawer(Gravity.LEFT);
        }else {
            drawerLayout.closeDrawers();
        }
    }
}
