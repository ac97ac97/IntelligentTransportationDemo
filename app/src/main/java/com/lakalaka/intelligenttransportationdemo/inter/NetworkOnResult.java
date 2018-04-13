package com.lakalaka.intelligenttransportationdemo.inter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lakalaka on 2018/3/17/0017.
 */

public interface NetworkOnResult {
    void onSuccess(JSONObject jsonObject) throws JSONException;
    void onError();
}
