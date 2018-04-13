package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.FindInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19.
 */

public class FindInfoItem1Adapter extends BaseAdapter {
    private List<FindInfoBean> objects = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private Context context;
    private static  final String TAG="FindInfoItem1Adapter";


    /**
     * 对外提供访问接口 其他类可通过实现该OnRemoveClick抽象方法 达到删除违章消息效果
     */
    public interface OnRemoveClickListener{
        void OnRemoveClick(int pos);
    }
    private OnRemoveClickListener onRemoveClickListener;
    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener=onRemoveClickListener;
    }

    public FindInfoItem1Adapter(List<FindInfoBean> objects,Context context) {
        this.objects = objects;
        this.layoutInflater = layoutInflater.from(context);
        this.context = context;
    }
    public void setObjects(List<FindInfoBean> objects){
        this.objects = objects;
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public FindInfoBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView=layoutInflater.inflate(R.layout.find_layout_item,parent,false);
            convertView.setTag(new ViewHolder(convertView));
        }
        /**
         * 显示具体违章信息
         */
        initializeViews(getItem(position),(ViewHolder) convertView.getTag(),position);

        return convertView;
    }
    private void initializeViews(final FindInfoBean object, final ViewHolder holder, final  int pos){
        holder.tvCarId.setText(object.getCar_number()+"");
        holder.tvFakuan.setText("未处理违章"+object.getDispose_number()+"次");
        holder.tvWeichuliWeizhang.setText("扣"+object.getDeduction()+"分   罚款"+object.getAmerce());
        holder.ivRemoveItem.setOnClickListener(new View.OnClickListener() {
            /**
             * 清除违章信息
             * @param v
             */
            @Override
            public void onClick(View v) {
            onRemoveClickListener.OnRemoveClick(pos);
            }
        });
    }
    protected class ViewHolder{
        private TextView tvCarId;
        private TextView tvWeichuliWeizhang;
        private TextView tvFakuan;
        private ImageView ivRemoveItem;

        /**
         * 初始化item控件
         * @param view
         */
    public ViewHolder(View view){
        tvCarId = view.findViewById(R.id.tv_car_id);
        tvWeichuliWeizhang=view.findViewById(R.id.tv_weichuli_weizhang);
        tvFakuan=view.findViewById(R.id.tv_fakuan);
        ivRemoveItem=view.findViewById(R.id.iv_remove_item);
    }
    }
}
