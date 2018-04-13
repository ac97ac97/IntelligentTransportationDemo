package com.lakalaka.intelligenttransportationdemo.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by lakalaka on 2018/3/21/0021.
 *
 * 检测是否有网络
 */

public class NetWorkUtils {


   public static boolean isConnection(Context context){
        //得到网利网络的服务实例
        ConnectivityManager connMgr= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //得到网络信息  判断网络是4G 3G
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
//        if(networkInfo.getType()==ConnectivityManager.TYPE_WIFI){
//            Toast.makeText(context, "您当前为WIFI网络", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(context, "您当前为3/4G网络", Toast.LENGTH_SHORT).show();
//        }
        //判断网络是否连接  连接返回true  否则 false
        return (networkInfo!=null&&networkInfo.isConnected());
    }

}
