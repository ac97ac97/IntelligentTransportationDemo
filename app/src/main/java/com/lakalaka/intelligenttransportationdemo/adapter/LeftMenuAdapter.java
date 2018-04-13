package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;

import java.util.List;

/**
 * Created by Administrator on 2018/3/28.
 */

public class LeftMenuAdapter extends BaseAdapter {
    private Context context;
    private List<LeftMenu> leftMenus;

    public LeftMenuAdapter(Context context, List<LeftMenu> leftMenus) {
        this.context = context;
        this.leftMenus = leftMenus;
    }

    @Override
    public int getCount() {
        return leftMenus.size();
    }

    @Override
    public Object getItem(int position) {
        return leftMenus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LeftMenu leftMenu = (LeftMenu) getItem(position);
        View view ;
        ViewHolder holder;
        if (convertView == null){
            view = LayoutInflater.from(context).inflate(R.layout.activity_main_list_item,parent,false);
            holder=new ViewHolder();
            holder.leftMenuImage=view.findViewById(R.id.iv_left_menu_img);
            holder.leftMenutext = view.findViewById(R.id.tv_left_menu_text);
            view.setTag(holder);
        }else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.leftMenutext.setText(leftMenu.getLeftMenuText());
        holder.leftMenuImage.setImageResource(leftMenu.getLeftMenuImg());
        return view;
    }
    class ViewHolder{
        ImageView leftMenuImage;
        TextView leftMenutext;


    }
}
