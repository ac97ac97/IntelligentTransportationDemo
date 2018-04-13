package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.AccountMessageItemLayoutAdapter;
import com.lakalaka.intelligenttransportationdemo.app.AppClient;
import com.lakalaka.intelligenttransportationdemo.beans.AccountBean;
import com.lakalaka.intelligenttransportationdemo.beans.AccountTable;
import com.lakalaka.intelligenttransportationdemo.dao.AccountDao;
import com.lakalaka.intelligenttransportationdemo.inter.NetworkOnResult;
import com.lakalaka.intelligenttransportationdemo.network.NetRequest;
import com.lakalaka.intelligenttransportationdemo.util.GsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class AccountMangerFragment extends Fragment {
    /**
     * 不再弹出对话框功能的常量
     */
    public static final String DATA="data";
    public static final String CONTENT="content";
    public static  int nIsReminder=1;//是否提醒：0不提醒 ，1提醒 首次进入需要提醒
    public static  int REMINDER_NO=0;
    public static int REMINDER_YES=1;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "AccountMangerFragment";

    private OnFragmentInteractionListener listener;

    public static ImageView mIvMenu;
    private TextView mTvAccountBatch;
    private TextView mTvAccountLog;
    private ListView mLvAccountList;

    //这个list泛型，gson解析的数据最终都会给他
    private List<AccountBean> accountBeans;
    private AccountMessageItemLayoutAdapter accountMessageItemLayoutAdapter;

    //建立图片数组
    private int carIcons[] = {R.drawable.bmw, R.drawable.zhonghua,
            R.drawable.benci, R.drawable.mazida};
    //dao负责表的增删改查
    private AccountDao accountDao;

    public AccountMangerFragment() {

    }

    public static AccountMangerFragment newInstance(String param1, String param2) {
        AccountMangerFragment fragment = new AccountMangerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_manger, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();//初始化控件
        initAdapter();//给界面中的list加载适配器
        initData();//加载初始数据
        initEvent();//事件再起控制
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        accountDao = new AccountDao(getContext());
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Message object);
    }


    private void initEvent() {
        //标题栏左侧的按钮点击事件
        mIvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
                onButtonPressed(msg);
            }
        });

        //监听列表中按钮
        accountMessageItemLayoutAdapter.setOnAccountCheckListener(new AccountMessageItemLayoutAdapter.OnAccountCheckListener() {
            @Override
            public void OnBtnCheck(View view, int pos) {
                Log.i(TAG, "OnBtnCheck:POS是 " + pos);
                showDialog(pos);
            }
        });
        //批量管理强id出现为-1然后来分明是批量和单个更改
        mTvAccountBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(-1);
            }
        });

        //充值记录
        mTvAccountLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("key", 1);
                PersonalCenterFragment fragment = new PersonalCenterFragment();
                fragment.setArguments(bundle);
                //替换碎片布局
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fl_fragment_group, fragment)
                        .commit();
                Message msg = new Message();
                msg.what = 2;
                onButtonPressed(msg);
            }
        });

    }


    public void onButtonPressed(Message msg) {
        if (listener != null) {
            listener.onFragmentInteraction(msg);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {

        }

    }

    //初始化事件
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    /**
     * 实现批量充值和单条充值的逻辑
     *
     * @param pos pos就是car的id名称
     */
    public void showDialog(final int pos) {

        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("车辆账户充值");
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.accout_add_money_dialog_layout, null);
        final EditText etMoney = view.findViewById(R.id.et_money_input);
        Button btnChongZhi = view.findViewById(R.id.btn_chongzhi);
        Button btnQuXiao = view.findViewById(R.id.btn_quxiao);
        TextView tvCarId = view.findViewById(R.id.tv_car_id);
        String person = "车牌号：";
        if (pos != -1) {
            List<AccountBean> accountBeans = accountMessageItemLayoutAdapter.getObjects();
            AccountBean bean = accountBeans.get(pos);
            Log.i(TAG, "bean的值为" + bean);
            Log.d("TAG", "money"+bean.getCar_money());
            person += bean.getCar_number() + " ";
            tvCarId.setText(person);

        } else {
            List<AccountBean> accountList = accountMessageItemLayoutAdapter.getChongZhiList();
            for (int i = 0; i < accountList.size(); i++) {
                AccountBean bean = accountList.get(i);
                person += bean.getCar_number() + " ";
            }
            tvCarId.setText(person);
        }







        //输入金额的监听器
        etMoney.addTextChangedListener(new textAdd());

        /**
         * 点击取消弹出提示 如果选中复选框 将不再弹出提示
          */


        btnQuXiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedata = getActivity().getSharedPreferences(DATA, 0);
                int nData = sharedata.getInt(CONTENT,REMINDER_NO);
                if (REMINDER_YES==nData){
                    dialog.dismiss();
//                    Toast.makeText(getActivity(),"123",Toast.LENGTH_SHORT).show();
                }else if (REMINDER_NO==nData){
                    final  AlertDialog isNextDialog = new AlertDialog.Builder(getActivity()).create();
                    isNextDialog.setTitle("提示");
                    View isNextView = LayoutInflater.from(getActivity()).inflate(R.layout.account_manger_dialog_cancel, null);
                    isNextDialog.setView(isNextView);
                    Button btnYes=isNextView.findViewById(R.id.btn_yes);
                    Button btnNo=isNextView.findViewById(R.id.btn_no);
                    CheckBox cbIsNext=isNextView.findViewById(R.id.cb_isNext);
                    cbIsNext.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked==true){
                                nIsReminder=REMINDER_YES;
                            }else {
                                nIsReminder=REMINDER_NO;
                            }
                        }
                    });
                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor sharedata =getActivity().getSharedPreferences(DATA, 0).edit();
                            sharedata.putInt(CONTENT, nIsReminder);
                            sharedata.commit();
                            isNextDialog.dismiss();
                            dialog.dismiss();
                        }
                    });
                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor sharedata =getActivity().getSharedPreferences(DATA, 0).edit();
                            sharedata.putInt(CONTENT, nIsReminder);
                            sharedata.commit();
                            isNextDialog.dismiss();
                        }
                    });

                    isNextDialog.show();
                }


            }


        });




        btnChongZhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String moneyText = etMoney.getText().toString().trim();
                Log.i(TAG, "onClick: pos carid的值是" + pos);
                if (pos != -1) {
                    final JSONObject jsonObj = new JSONObject();
                    Log.i(TAG, "onClick: jsonObj put之前的值为 " + jsonObj);
                    try {
//                        将json中的数据保存传递一下
                        jsonObj.put("car_number", accountMessageItemLayoutAdapter
                                .getObjects().get(pos).getCar_number());
                        int money = Integer.parseInt(moneyText);
                        /**
                         * 单次充值如果充值金额大于500元 则需要输入支付密码支付  密码正确方可充值成功
                         */
                        if (money > 500) {
                            final AlertDialog dialog_anquan = new AlertDialog.Builder(getActivity()).create();
                            View view_anquan = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anquan_zhifu, null);
                            dialog_anquan.setTitle("安全支付");
                            dialog_anquan.setView(view_anquan);
                            final EditText edt_anquan = view_anquan.findViewById(R.id.edt_anquan_zhifu);
                            Button btn_zhifu = view_anquan.findViewById(R.id.btn_zhifu);
                            btn_zhifu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edt_anquan.getText().toString().equals("123456")) {
                                        try {
                                            jsonObj.put("car_money", moneyText);
                                            dialog_anquan.dismiss();
                                            new NetRequest("single.car")
                                                    .setJsonBody(jsonObj)
                                                    .setNetWorkOnResult(new NetworkOnResult() {
                                                        @Override
                                                        public void onSuccess(JSONObject jsonObject) {
                                                            Log.i(TAG, "onSuccess:刚请求的json值为 " + jsonObject.toString());
                                                            try {
                                                                if ("充值成功".equals(jsonObject.getString("response"))) {
                                                                    int money = jsonObject.getInt("result");
                                                                    List<AccountBean> accounts = accountMessageItemLayoutAdapter.getObjects();
                                                                    AccountBean accountBean = accounts.get(pos);
                                                                    accountBean.setCar_money(money);
                                                                    accounts.set(pos, accountBean);
                                                                    //将充值之后的值添加到adapter中并且刷新
                                                                    accountMessageItemLayoutAdapter.setObjects(accounts);
                                                                    accountMessageItemLayoutAdapter.notifyDataSetChanged();
                                                                    Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
//                                        设置充值记录时间的格式
                                                                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    AccountTable accountTable = new AccountTable(accountBean.getCar_number(),
                                                                            Integer.parseInt(moneyText), money, AppClient.getString("username"),
                                                                            time.format(new Date()), getWeekOfDate(new Date()));
                                                                    Log.i(TAG, "onSuccess:accountTable的值为 " + accountTable);

                                                                    //将数据添加到数据表中
                                                                    accountDao.addAccountTable(accountTable);
                                                                    dialog.dismiss();
                                                                } else {
                                                                    Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();

                                                                }

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onError() {

                                                        }
                                                    });

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        dialog_anquan.dismiss();
                                        Toast.makeText(getActivity(), "密码不正确充值失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog_anquan.show();
                        } else {
                            jsonObj.put("car_money", moneyText);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new NetRequest("single.car")
                            .setJsonBody(jsonObj)
                            .setNetWorkOnResult(new NetworkOnResult() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    Log.i(TAG, "onSuccess:刚请求的json值为 " + jsonObject.toString());
                                    try {
                                        if ("充值成功".equals(jsonObject.getString("response"))) {
                                            int money = jsonObject.getInt("result");


                                            List<AccountBean> accounts = accountMessageItemLayoutAdapter.getObjects();
                                            AccountBean accountBean = accounts.get(pos);
                                            accountBean.setCar_money(money);
                                            accounts.set(pos, accountBean);
                                            //将充值之后的值添加到adapter中并且刷新
                                            accountMessageItemLayoutAdapter.setObjects(accounts);
                                            accountMessageItemLayoutAdapter.notifyDataSetChanged();
                                            Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
//                                        设置充值记录时间的格式
                                            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            AccountTable accountTable = new AccountTable(accountBean.getCar_number(),
                                                    Integer.parseInt(moneyText), money, AppClient.getString("username"),
                                                    time.format(new Date()), getWeekOfDate(new Date()));
                                            Log.i(TAG, "onSuccess:accountTable的值为 " + accountTable);

                                            //将数据添加到数据表中
                                            accountDao.addAccountTable(accountTable);
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError() {

                                }
                            });
                } else {
                    //将界面中的ListView内容加入到所给list泛型之中
                    List<AccountBean> accounts = accountMessageItemLayoutAdapter.getChongZhiList();
                    final JSONObject jsonObj = new JSONObject();

                    try {
                        int money = Integer.parseInt(moneyText);
                        /**
                         * 批次充值如果充值金额大于500元 则需要输入支付密码支付  密码正确方可充值成功
                         */
                        if (money > 500) {
                            final AlertDialog dialog_anquan = new AlertDialog.Builder(getActivity()).create();
                            View view_anquan = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_anquan_zhifu, null);
                            dialog_anquan.setTitle("安全支付");
                            dialog_anquan.setView(view_anquan);
                            final EditText edt_anquan = view_anquan.findViewById(R.id.edt_anquan_zhifu);
                            Button btn_zhifu = view_anquan.findViewById(R.id.btn_zhifu);
                            btn_zhifu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (edt_anquan.getText().toString().equals("123456")) {

                                        try {
                                            jsonObj.put("car_money", moneyText);
                                            dialog_anquan.dismiss();
                                            new NetRequest("batch.car")
                                                    .setJsonBody(jsonObj)
                                                    .setNetWorkOnResult(new NetworkOnResult() {
                                                        @Override
                                                        public void onSuccess(JSONObject jsonObject) {
                                                            Log.i(TAG, "onSuccess: jsonObject的值为 " + jsonObject);
                                                            try {
                                                                if ("充值成功".equals(jsonObject.getString("response"))) {
                                                                    initData();//刷新数据
                                                                    Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                                                                    List<AccountBean> beans = accountMessageItemLayoutAdapter.getChongZhiList();
                                                                    SimpleDateFormat time =
                                                                            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    for (int i = 0; i < beans.size(); i++) {
                                                                        AccountTable accountTable = new AccountTable(beans.get(i).getCar_number() + "",
                                                                                Integer.parseInt(moneyText), beans.get(i).getCar_money(),
                                                                                AppClient.getString("username"), time.format(new Date()), getWeekOfDate(new Date()));
                                                                        Log.i(TAG, "onSuccess: accountTable shi  " + accountTable);
                                                                        accountDao.addAccountTable(accountTable);
                                                                    }
                                                                    dialog.dismiss();
                                                                } else {
                                                                    Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        @Override
                                                        public void onError() {

                                                        }
                                                    });
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        dialog_anquan.dismiss();
                                        Toast.makeText(getActivity(), "支付密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            dialog_anquan.show();
                        } else {
                            jsonObj.put("car_money", moneyText);
                        }

                        for (int i = 0; i < accounts.size(); i++) {
                            AccountBean bean = accounts.get(i);
                            jsonObj.put("car_number" + (i + 1), bean.getCar_number());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "onClick: json的jsontoString的值为 " + jsonObj.toString());
                    new NetRequest("batch.car")
                            .setJsonBody(jsonObj)
                            .setNetWorkOnResult(new NetworkOnResult() {
                                @Override
                                public void onSuccess(JSONObject jsonObject) {
                                    Log.i(TAG, "onSuccess: jsonObject的值为 " + jsonObject);
                                    try {
                                        if ("充值成功".equals(jsonObject.getString("response"))) {
                                            initData();//刷新数据
                                            Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                                            List<AccountBean> beans = accountMessageItemLayoutAdapter.getChongZhiList();
                                            SimpleDateFormat time =
                                                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                            for (int i = 0; i < beans.size(); i++) {
                                                AccountTable accountTable = new AccountTable(beans.get(i).getCar_number() + "",
                                                        Integer.parseInt(moneyText), beans.get(i).getCar_money(),
                                                        AppClient.getString("username"), time.format(new Date()), getWeekOfDate(new Date()));
                                                Log.i(TAG, "onSuccess: accountTable shi  " + accountTable);
                                                accountDao.addAccountTable(accountTable);
                                            }
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();
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
        });
        dialog.setView(view);
        dialog.show();
    }


    /**
     * 获取当前日期是星期几
     *
     * @param date
     * @return当前的日期
     */
    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    /**
     * 加载初始化数据
     */
    private void initData() {
        new NetRequest("all.car")
                .showDialog(getActivity())
                .setNetWorkOnResult(new NetworkOnResult() {
                    @Override
                    public void onSuccess(JSONObject jsonObject) {
                        Log.i(TAG, "onSuccess: " + jsonObject);
                        try {
                            if ("成功".equals(jsonObject.getString("response"))) {
                                JSONArray jsonArr = jsonObject.getJSONArray("result");
                                accountBeans = GsonUtil.gsonTOList(jsonArr.toString(), AccountBean.class);
                                for (int i = 0; i < accountBeans.size(); i++) {
                                    AccountBean accountbean = accountBeans.get(i);
                                    accountbean.setBitmapRes(carIcons[i]);
                                    accountBeans.set(i, accountbean);
                                }
                                accountMessageItemLayoutAdapter.setObjects(accountBeans);
                                accountMessageItemLayoutAdapter.notifyDataSetChanged();
                                Log.i(TAG, "onSuccess: 中的accountBeans是" + accountBeans);
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


    /**
     * 给界面中的list加载适配器
     */
    private void initAdapter() {

        accountMessageItemLayoutAdapter = new AccountMessageItemLayoutAdapter(getActivity(), accountBeans);
        mLvAccountList.setAdapter(accountMessageItemLayoutAdapter);

    }

    /**
     * \
     * c初始化控件
     */
    private void initView() {
        accountBeans = new ArrayList<>();
        mIvMenu = getView().findViewById(R.id.iv_menu);
        mTvAccountBatch = getView().findViewById(R.id.tv_account_batch);
        mTvAccountLog = getView().findViewById(R.id.tv_account_log);
        mLvAccountList = getView().findViewById(R.id.lv_account_list);
    }


    /**
     * editText中的监听器看字体的加减
     */
    private class textAdd implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int after) {
            Log.i(TAG, "onTextChanged: " + s);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String ss = editable.toString();
            Log.i(TAG, "afterTextChanged: " + ss);
            if (ss.equals("0")) {
                editable.delete(0, 1);
            }
        }
    }



    /**
     * 最后结束碎片进程
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



}

