package com.lakalaka.intelligenttransportationdemo.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lakalaka on 2018/3/18/0018.
 */

public class GsonUtil<T> {
    public void getJsonInfo(JSONObject jsonObj, Class clz) {

        Gson gson = new Gson();
        gson.fromJson(jsonObj.toString(), clz);
    }


    public static <T> List<T> gsonData(JSONObject jsonObj, Class<T> cls) {
        List<T> list = new ArrayList<T>();
        Gson gson = new Gson();
        list.add(gson.fromJson(jsonObj.toString(), cls));
        return list;
    }

    /**
     * 用泛型是因为可以使该方法的返回值成为一个指定类型的集合，
     * 这样再次使用该集合的时候就有一个明确的类型了，
     * 这使的在将来该类型发生改变的时候编译器会报错，
     * 提醒你做相应的修改，而不是让问题暴露在运行阶段。
     * 这是泛型的重要作用之一。
     * <p>
     * * 转成list
     * 泛型在编译期类型被擦除导致报错
     * <p>
     * 第一个是用来声明类型参数的  后面是泛型实现
     *
     * @param gosnString
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> gsonTOList(String gosnString, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<T>();
        JsonArray array = new JsonParser().parse(gosnString).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }
}
