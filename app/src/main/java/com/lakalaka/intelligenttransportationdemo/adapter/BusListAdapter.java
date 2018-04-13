package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.BusBean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 * 折叠ListView的自定义适配器
 */

public class BusListAdapter extends BaseExpandableListAdapter {
    List<String> groupList;
    List<List<BusBean>> childList;
    private Context context;

    public BusListAdapter(List<String> groupList, List<List<BusBean>> childList, Context context) {
        this.groupList = groupList;
        this.childList = childList;
        this.context = context;
    }

    public void setChildList(List<List<BusBean>> childList) {
        this.childList = childList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return childList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * getGroupView getChildView 与 自定义listview适配器 的 getview 方法用法一样
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder1 viewHolder1;
        String zhantai=groupList.get(groupPosition);
        if (convertView==null){
            viewHolder1=new ViewHolder1();
            convertView= LayoutInflater.from(context).inflate(R.layout.bus_item_layout,null);
            viewHolder1.mTvZhanTai=convertView.findViewById(R.id.tv_zhantai);
            convertView.setTag(viewHolder1);
        }else {
            viewHolder1= (ViewHolder1) convertView.getTag();
        }
        viewHolder1.mTvZhanTai.setText(""+zhantai);
        return convertView;
    }
    /**
     * 优化listview时 注 ：子布局能用到的子控件也要实例化 防止空指针的导致系统崩溃
     */

    /**
     *优化父列表
     */
    private class ViewHolder1{
        TextView mTvZhanTai;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        BusBean busBean = (BusBean) getChild(groupPosition,childPosition);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=LayoutInflater.from(context).inflate(R.layout.bus_item_list_layout,null);
            viewHolder.tvJvLiNum=convertView.findViewById(R.id.tv_bus_juli);
            viewHolder.tvCarNum=convertView.findViewById(R.id.tv_car);
            convertView.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvJvLiNum.setText(busBean.getDistanceNumber()+"");
        viewHolder.tvCarNum.setText(busBean.getPersonNumber()+"");
        return convertView;
    }

    /**
     * 优化子列表
     */
    private class ViewHolder{
        TextView tvCarNum;
        TextView tvJvLiNum;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
