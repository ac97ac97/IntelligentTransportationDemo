package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.lakalaka.intelligenttransportationdemo.util.PerferenceUtil;

import java.util.Locale;

/**
 * Created by Administrator on 2018/3/23.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化perferenceUtil
        PerferenceUtil.init(this);
        //根据上次的语言重新设置语言

    }
    protected void switchLanguage(String language){
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config=resources.getConfiguration();
        DisplayMetrics dm=resources.getDisplayMetrics();
        if (language.equals("en")){
            config.locale= Locale.ENGLISH;

        }else if (language.equals("zh")){
            config.locale = Locale.SIMPLIFIED_CHINESE;
        }
        resources.updateConfiguration(config,dm);
        //保存设置语言的类型
        PerferenceUtil.commitString("language",language);
    }
}
