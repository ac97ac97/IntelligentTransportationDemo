package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;

public class LanguageSettingActivity extends BaseActivity implements View.OnClickListener{

    private ImageView ivBack;
    private TextView tv_save;
    private RadioGroup rg_languageGroup;
    private RadioButton rb_languageChinses;
    private RadioButton rb_languageEnglish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_setting);
        initViews();
         rg_languageGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                 switch (checkedId){
                     case R.id.rb_setLanguage_Chinese:
                         switchLanguage("zh");
                         break;
                     case R.id.rb_setLanguage_English:
                         switchLanguage("en");
                         break;

                 }
             }
         });
    }
    private void initViews(){
        ivBack = (ImageView) findViewById(R.id.iv_back_settingLanguage);
        tv_save = (TextView) findViewById(R.id.tv_saveLanguage);


        rg_languageGroup = (RadioGroup) findViewById(R.id.rg_setLanguage);
        rb_languageChinses = (RadioButton) findViewById(R.id.rb_setLanguage_Chinese);
        rb_languageEnglish = (RadioButton) findViewById(R.id.rb_setLanguage_English);

        ivBack.setOnClickListener(this);
        tv_save.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_settingLanguage:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.tv_saveLanguage:
//                int selectCheck=rg_languageGroup.getCheckedRadioButtonId();
//                if (selectCheck==rb_languageChinses.getId()){
//                    rb_languageChinses.isChecked();
//                }else {
//                    rb_languageEnglish.isChecked();
//                }
                Toast.makeText(this,"设置成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginActivity.class));

                break;
        }
    }



}
