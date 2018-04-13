package com.lakalaka.intelligenttransportationdemo.util;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by 无情 on 2018/3/19.
 */

public class SpinnerAdapterUtils {


    public static ArrayAdapter getSpinnerAdapter(Context context, String ...args){
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(context,
                        android.R.layout.simple_spinner_dropdown_item,args);
        return  arrayAdapter;
    }
}
