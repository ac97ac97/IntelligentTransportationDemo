package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.adapter.TopupListItemAdapter;
import com.lakalaka.intelligenttransportationdemo.beans.AccountTable;
import com.lakalaka.intelligenttransportationdemo.dao.AccountDao;
import com.lakalaka.intelligenttransportationdemo.util.DatabaseHelper;
import com.lakalaka.intelligenttransportationdemo.util.SpinnerAdapterUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Administrator on 2018/3/21.
 * TabHost的充值记录
 */

public class TopUpLogFragment extends Fragment {
    private ListView mLvTopup;
    private TopupListItemAdapter adapter;
    private AccountDao accountDao;
    private TextView mTvMoneySum;
    private Spinner spSortIime;
    private String TAG = "TopUpLogFragment";

    String[] arr = {"时间升序", "时间降序"};

    public TopUpLogFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_up_log, container, false);
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
        initAdapter();
        initEvent();
    }
    //-------------------------------------------------------------------------------------------------------------------------

    /**
     * 时间升序排列
     * @param list
     * @return
     */
    private List<AccountTable> ListSort(List<AccountTable> list) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1;
        Date d2;
        List<AccountTable> abc = accountDao.getAccountTableAll();

        AccountTable temp_r = new AccountTable();
        Log.i(TAG, "list的值为 " + list);

        for (int i = 0; i < list.size() - 1; i++) {

            for (int j = i + 1; j < list.size(); j++) {

                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                d1 = format.parse(list.get(i).getAccountTime(), pos1);
                d2 = format.parse(list.get(j).getAccountTime(), pos2);
                if (d2.before(d1)) {//如果日期靠前，调换顺序
                    temp_r = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp_r);
                }
            }
            Log.i(TAG, "list排序之后的每一项为: " + list.get(i).getAccountTime());

        }
        return list;
    }
    //-------------------------------------------------------------------------------------------------------------------------


    /**
     * 时间降序排列
     * @param list
     * @return
     */

    private List<AccountTable> ListJiangSort(List<AccountTable> list) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1;
        Date d2;
        List<AccountTable> abc = accountDao.getAccountTableAll();

        AccountTable temp_r = new AccountTable();
        Log.i(TAG, "list的值为 " + list);

        for (int i = 0; i < list.size() - 1; i++) {

            for (int j = i + 1; j < list.size(); j++) {

                ParsePosition pos1 = new ParsePosition(0);
                ParsePosition pos2 = new ParsePosition(0);
                d1 = format.parse(list.get(i).getAccountTime(), pos1);
                d2 = format.parse(list.get(j).getAccountTime(), pos2);
                if (d1.before(d2)) {//如果日期靠前，调换顺序
                    temp_r = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp_r);
                }
            }
            Log.i(TAG, "list排序之后的每一项为: " + list.get(i).getAccountTime());

        }
        return list;
    }


    private void initEvent() {
        spSortIime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            //通过选择spinner中的项目
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<AccountTable> abc = accountDao.getAccountTableAll();
                switch (position) {
                    case 0:
                        ListSort(abc);//升序
                        break;
                    case 1:
                        ListJiangSort(abc);//降序
                        break;
                }
                adapter.setObjects(abc);//传递数据给adapter
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
//----------------------------------------------------------------------------------------------------------------------------
    private void initAdapter() {

        Context context = getContext();
        if (DatabaseHelper.getInstance(context).isClose()) {
            accountDao = new AccountDao(context);
            List<AccountTable> tables = accountDao.getAccountTableAll();
            //处理总金额字符串
            String sumText = "";
            int sum = 0;
            for (AccountTable accountTable : tables) {
                sum += accountTable.getCarMoney();
            }
            sumText = sum + sumText;
            String sumTextBuf = "";
            for (int i = 0; i < sumText.length(); i++) {
                sumTextBuf += sumText.charAt(i);
                if ((i + 1) % 3 == 0 && i != sumText.length() - 1) {
                    sumText += ",";
                }
            }
            sumTextBuf += ".00";
            mTvMoneySum.setText(sumTextBuf);

            adapter = new TopupListItemAdapter(getActivity(), tables);
            Log.d("TopUpLogFragment", "-----------------------------------------------");
            mLvTopup.setAdapter(adapter);
        } else {
            Toast.makeText(getActivity(), "请重新进入更新程序", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {

        mLvTopup = getView().findViewById(list);
        mLvTopup.setEmptyView(getView().findViewById(android.R.id.empty));
        mTvMoneySum = getView().findViewById(R.id.tv_money_sum);
        spSortIime = getView().findViewById(R.id.sp_sortIime);
        spSortIime.setAdapter(SpinnerAdapterUtils.getSpinnerAdapter(getContext(), arr));
    }
}
