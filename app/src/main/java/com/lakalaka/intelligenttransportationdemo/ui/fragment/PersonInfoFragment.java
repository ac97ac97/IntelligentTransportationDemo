package com.lakalaka.intelligenttransportationdemo.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lakalaka.intelligenttransportationdemo.R;

/**
 * Created by Administrator on 2018/3/21.
 * Tabhost个人信息
 */

public class PersonInfoFragment extends Fragment {
    public PersonInfoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_info,container,false);
        return view;
    }
}
