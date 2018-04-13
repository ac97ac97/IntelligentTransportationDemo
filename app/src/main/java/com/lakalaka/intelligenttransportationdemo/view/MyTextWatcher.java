package com.lakalaka.intelligenttransportationdemo.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by Administrator on 2018/3/27.
 */
public class MyTextWatcher implements TextWatcher
{
    public EditText mEditText;
    public EditText [] gwEdit;
    public MyTextWatcher(EditText mEditText) {
        super();
        this.mEditText = mEditText;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
        if(s.length() == 3)
        {
            if(this.mEditText == gwEdit[0])
            {
                gwEdit[1].requestFocus();
            }
            else if(this.mEditText == gwEdit[1])
            {
                gwEdit[2].requestFocus();
            }
            else if(this.mEditText == gwEdit[2])
            {
                gwEdit[3].requestFocus();
            }
        }
        else if(s.length() == 0)
        {
            if(this.mEditText == gwEdit[3])
            {
                gwEdit[2].requestFocus();
            }
            else if(this.mEditText == gwEdit[2])
            {
                gwEdit[1].requestFocus();
            }
            else if(this.mEditText == gwEdit[1])
            {
                gwEdit[0].requestFocus();
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        // TODO Auto-generated method stub

    }

}