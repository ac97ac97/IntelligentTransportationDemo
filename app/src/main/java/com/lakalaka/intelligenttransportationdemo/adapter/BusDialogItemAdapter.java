package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class BusDialogItemAdapter extends BaseAdapter {
    private List<String> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    public BusDialogItemAdapter(Context context, List<String> objects) {
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.bus_dialog_item,null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((String) getItem(position),(ViewHolder)convertView.getTag(),position);
        return convertView;
    }
    private void  initializeViews(String object,ViewHolder holder,int pos){
        holder.tvNum.setText((pos+1)+"");
        holder.tvBusNum.setText((pos+1)+"");
        holder.tvPersonNum.setText(object);
    }
    public class ViewHolder{
        private TextView tvNum;
        private TextView tvBusNum;
        private TextView tvPersonNum;
        public ViewHolder(View view){
            tvNum=view.findViewById(R.id.tv_num);
            tvPersonNum=view.findViewById(R.id.tv_personNum);
            tvBusNum=view.findViewById(R.id.tv_busNum);
        }
    }
}
