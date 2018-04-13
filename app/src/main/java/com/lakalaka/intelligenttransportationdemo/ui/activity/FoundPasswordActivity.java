package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;

public class FoundPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtGetUserName;
    private EditText edtGetUserEmail;
    private Button btnFoundPassword;
    private ImageView ivBack;
    private TextView tvShowPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_password);
        initViews();
    }
    private void initViews(){
        tvShowPassword = (TextView) findViewById(R.id.tv_showPassword);
        edtGetUserName = (EditText) findViewById(R.id.edt_getUserName);
        edtGetUserEmail = (EditText) findViewById(R.id.edt_getUserEmail);
        btnFoundPassword = (Button) findViewById(R.id.btn_foundPassword);
        ivBack = (ImageView) findViewById(R.id.iv_back_foundpawword);

        ivBack.setOnClickListener(this);
        btnFoundPassword.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_foundpawword:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btn_foundPassword:
                SharedPreferences preferences = getSharedPreferences("registerInformation",MODE_PRIVATE);
                if (edtGetUserName.getText().toString().trim().equals(preferences.getString("UserName",""))&&edtGetUserEmail.getText().toString().trim().equals(preferences.getString("UserEmail",""))){
                    tvShowPassword.setText("您的用户密码为:"+preferences.getString("UserPassWord",""));
                }else {
                    Toast.makeText(this,"请输入正确的邮箱或用户名",Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}
