package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;

public class UserRegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtUserName;
    private Button btnCommit;
    private EditText edtUserPassWordOk;
    private EditText edtUserPassWord;
    private EditText edtUserEmail;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        initViews();
    }
    private void initViews(){
        edtUserName = (EditText) findViewById(R.id.edt_username);
        edtUserEmail = (EditText) findViewById(R.id.edt_useremail);
        edtUserPassWord = (EditText) findViewById(R.id.edt_userpassword);
        edtUserPassWordOk= (EditText) findViewById(R.id.edt_userpassword_ok);
        iv_back= (ImageView) findViewById(R.id.iv_back_register);
        btnCommit = (Button) findViewById(R.id.btn_commit);
//        btnBack.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back_register:
                startActivity(new Intent(this,LoginActivity.class));
                break;
            case R.id.btn_commit:
                SharedPreferences.Editor editor = getSharedPreferences("registerInformation",MODE_PRIVATE).edit();
                editor.putString("UserName",edtUserName.getText().toString().trim());
                editor.putString("UserEmail",edtUserEmail.getText().toString().trim());
                editor.putString("UserPassWord",edtUserPassWord.getText().toString().trim());
                editor.putString("UserPassWordOk",edtUserPassWordOk.getText().toString().trim());
                if (edtUserPassWord.getText().toString().trim().equals(edtUserPassWordOk.getText().toString().trim())){
                    editor.apply();
                    startActivity(new Intent(this,LoginActivity.class));
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                }else {
                    edtUserPassWord.setText("");
                    edtUserPassWordOk.setText("");
                    Toast.makeText(this,"两次密码不一致请重新输入",Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
