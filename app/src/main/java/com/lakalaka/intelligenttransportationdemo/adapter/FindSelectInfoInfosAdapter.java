package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.FaKuanInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 * 查询具体违章信息 具体时间 处理结果 那一条路 罚款 扣分
 */

public class FindSelectInfoInfosAdapter extends BaseAdapter {
    private List<FaKuanInfoBean> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;

    public FindSelectInfoInfosAdapter(List<FaKuanInfoBean> objects, Context context) {
        this.objects = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }
    public void setObjects(List<FaKuanInfoBean> objects){
        this.objects=objects;
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public FaKuanInfoBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.find_select_info_infos,null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position),(ViewHolder)convertView.getTag());
        return convertView;
    }
    private void  initializeViews(FaKuanInfoBean object,ViewHolder holder){
        holder.tvTime.setText(object.getDate()+"");
        holder.tvTrafficlu.setText(object.getAddress()+"");
        holder.tvTvInfo.setText(object.getInfo()+"");
        holder.tvFakuan.setText("罚款:"+object.getAmerce());
        holder.tvKoufen.setText("扣分:"+object.getDeduction());

    }
    protected class ViewHolder{
        private TextView tvTime;
        private TextView tvTrafficlu;
        private TextView tvTvInfo;
        private TextView tvKoufen;
        private TextView tvFakuan;
        public ViewHolder(View view){
            tvTime = view.findViewById(R.id.tv_time);
            tvTrafficlu = view.findViewById(R.id.tv_trafficlu);
            tvTvInfo = view.findViewById(R.id.tv_tv_info);
            tvKoufen = view.findViewById(R.id.tv_koufen);
            tvFakuan = view.findViewById(R.id.tv_fakuan);

        }
    }
}
