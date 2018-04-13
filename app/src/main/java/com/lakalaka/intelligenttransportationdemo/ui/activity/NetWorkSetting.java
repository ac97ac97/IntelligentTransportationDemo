package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.view.MyTextWatcher;

public class NetWorkSetting extends AppCompatActivity implements View.OnClickListener{

    private EditText netSettingOne;
    private EditText netSettingTwo;
    private EditText netSettingThree;
    private EditText netSettingFour;
    private ImageView iv_back;
    private Button btnCancel;
    private Button btnCommit;
    public MyTextWatcher[] myTextWatchernew =new MyTextWatcher[4];
    private EditText[] gwEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_work_setting);
        initViews();
    }
    private void initViews(){
        netSettingOne = (EditText) findViewById(R.id.netset_one);
        netSettingTwo = (EditText) findViewById(R.id.netset_two);
        netSettingThree = (EditText) findViewById(R.id.netset_three);
        netSettingFour = (EditText) findViewById(R.id.netset_four);

        iv_back = (ImageView) findViewById(R.id.iv_netsettings_back);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        iv_back.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_netsettings_back:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btn_cancel:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btn_commit:
                for(int i = 0; i < 4;i++)
                {
                    gwEdit = new EditText[]{netSettingOne,netSettingTwo,netSettingThree,netSettingFour};
                    myTextWatchernew[i] = new MyTextWatcher(gwEdit[i]);
                    gwEdit[i].addTextChangedListener(myTextWatchernew[i]);
                }
                Toast.makeText(NetWorkSetting.this,"设置成功",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,LoginActivity.class));
//                if (myTextWatchernew[i] = new MyTextWatcher(gwEdit[i])){
//                    Toast.makeText(NetWorkSetting.this,"设置成功",Toast.LENGTH_SHORT).show();
//                }else {
//                    netSettingOne.setText("");
//                    netSettingTwo.setText("");
//                    netSettingThree.setText("");
//                    netSettingFour.setText("");
//                    Toast.makeText(NetWorkSetting.this,"格式不正确请重新输入",Toast.LENGTH_SHORT).show();
//                }
                break;
            default:
        }
    }
}
