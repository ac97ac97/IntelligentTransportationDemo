package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.util.SpinnerAdapterUtils;

/**
 * Created by 无情 on 2018/3/28.
 */

public class TextOneFragment extends Fragment {

    private Button btnQuery;
    private TextView tvYuzhiShow;
    private TextView tvCarId;
    private Spinner spCarId;
    private TextView tvMax;
    private TextView tvMaxShow;
    private TextView tvMin;
    private TextView tvMinShow;


    private String TAG = "TextOneFragment";

    AlphaAnimation alp;

    String arr[] = {"1", "2", "3", "4"};
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alp = new AlphaAnimation(0.0f, 1.0f);
        alp.setDuration(4000);
        alp.setRepeatCount(AlphaAnimation.INFINITE);
        alp.setAnimationListener(new Animation.AnimationListener() {

            public void onAnimationStart(Animation animation) {

            }

            public void onAnimationRepeat(Animation animation) {

            }

            public void onAnimationEnd(Animation animation) {

                Log.i(TAG, "onAnimationEnd: 也进来了");
                alp.setFillAfter(true);
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initEvent();
    }



    private void initEvent() {
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvYuzhiShow.setAnimation(alp);
                Log.i(TAG, "进入了bend点击按钮: ");
            }
        });


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.text_one, container, false);
    }


    private void initView() {

        btnQuery = getView().findViewById(R.id.btn_one_query);
        tvYuzhiShow = (TextView) getView().findViewById(R.id.tv_yuzhi_show);
        tvCarId = (TextView) getView().findViewById(R.id.tv_car_id);
        spCarId = (Spinner) getView().findViewById(R.id.sp_carId);
        spCarId.setAdapter(SpinnerAdapterUtils.getSpinnerAdapter(getContext(), arr));
        tvMax = (TextView) getView().findViewById(R.id.tv_max);
        tvMaxShow = (TextView) getView().findViewById(R.id.tv_max_show);
        tvMin = (TextView) getView().findViewById(R.id.tv_min);
        tvMinShow = (TextView) getView().findViewById(R.id.tv_min_show);


    }

}
