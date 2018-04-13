package com.lakalaka.intelligenttransportationdemo.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.util.NetWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener {



    private EditText et_username;
    private EditText et_password;
    private CheckBox cb_password;
    private CheckBox cb_auto_login;
    private Button btn_login;
    private Button btn_zhuce;
    private TextView tvFoundPassword;
    private TextView tvLanguageSetting;
    private TextView tv_netSetting;


    private void showNetWorkSettingDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage("当前网络不可用,请先设置网络!");
        builder.setNegativeButton("取消",null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent().setAction("android.settings.WIRELESS_SETTINGS"));
            }
        });
        builder.create().show();

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        if(!NetWorkUtils.isConnection(this)){
            showNetWorkSettingDialog();
        }
        isAutoLogin();//自动登录

        setContentView(R.layout.activity_login);

        initView();//初始化控件
        initEvent();//自动登录必定记住密码
        isCheck();//记住密码显示


    }

    private void initEvent() {
        cb_auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cb_password.setChecked(true);
                }
            }
        });
    }

    private void initView() {
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        cb_password = (CheckBox) findViewById(R.id.cb_password);
        cb_auto_login = (CheckBox) findViewById(R.id.cb_auto_login);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_zhuce = (Button) findViewById(R.id.btn_register);
        tvFoundPassword = (TextView) findViewById(R.id.tv_found_password);
        tvLanguageSetting = (TextView) findViewById(R.id.tv_setLanguage);

        btn_login.setOnClickListener(this);
        btn_zhuce.setOnClickListener(this);
        tvFoundPassword.setOnClickListener(this);
        tvLanguageSetting.setOnClickListener(this);

        tv_netSetting = (TextView) findViewById(R.id.network_setup_tv);
        tv_netSetting.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.btn_register:
               startActivity(new Intent(this,UserRegisterActivity.class));
                break;
            case R.id.tv_found_password:
                startActivity(new Intent(this,FoundPasswordActivity.class));
                break;
            case R.id.tv_setLanguage:
                startActivity(new Intent(this,LanguageSettingActivity.class));
                break;
            case R.id.network_setup_tv:
                startActivity(new Intent(this,NetWorkSetting.class));
                break;
            default:
                break;
        }
    }

    private void login() {

        final String username = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();


        //进行用户名密码长度检测
//        if(username.length()<6||username.length()>12){
//            Toast.makeText(this, "用户名应大于6位小于12位", Toast.LENGTH_SHORT).show();
//        }
//
//        if (password.length()<6||password.length()>12) {
//            Toast.makeText(this, "密码应大于6位小于12位", Toast.LENGTH_SHORT).show();
//            return;
//        }


        if (!(cb_password.isChecked())) {
            cb_password.setChecked(false);
            AppClient.setBoolean("isPassword", false);
        }
        final JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("username", username);
            jsonObj.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new NetRequest("login.in")
                .setJsonBody(jsonObj)
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        try {
                            String request = jsonObject.getString("response");
                            if ("成功".equals(request)) {
                                String identity = jsonObject.getString("result");
                                AppClient.setString("identity", identity);
                                AppClient.setString("username", username);
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

                                //记住密码不一定自动登录
                                if (cb_password.isChecked()) {
                                    AppClient.setString("username", username);
                                    AppClient.setString("password", password);
                                    AppClient.setBoolean("isPassword", true);
                                }
                                //自动登录一定是记住密码的
                                if (cb_auto_login.isChecked()) {
                                    AppClient.setString("username", username);
                                    AppClient.setString("password", password);
                                    AppClient.setBoolean("isPassword", true);
                                    AppClient.setBoolean("isAutoLogin", true);
                                }
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                LoginActivity.this.finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });


    }


    public void isCheck() {
        if (AppClient.getBoolean("isPassword")) {
            cb_password.setChecked(true);
            et_username.setText(AppClient.getString("username"));
            et_password.setText(AppClient.getString("password"));
        }
    }

    public void isAutoLogin() {
        if (AppClient.getBoolean("isAutoLogin")) {
            final String username = AppClient.getString("username");
            String password = AppClient.getString("password");

            final JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put("username", username);
                jsonObj.put("password", password);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            new NetRequest("login.in")
                    .setJsonBody(jsonObj)
                    .setNetWorkOnResult(new NetworkOnResult() {
                        @Override
                        public void onSuccess(JSONObject jsonObject) {
                            try {
                                String request = jsonObject.getString("response");
                                if ("成功".equals(request)) {
                                    String identity = jsonObject.getString("result");

                                    AppClient.setString("identity", identity);
                                    AppClient.setString("username", username);

                                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    LoginActivity.this.finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError() {
                        }
                    });
        }
    }
}
