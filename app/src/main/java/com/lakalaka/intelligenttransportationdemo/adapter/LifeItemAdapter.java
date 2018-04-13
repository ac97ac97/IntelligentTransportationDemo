package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.LifeBean;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * Created by lakalaka on 2018/3/20/0020.
 */

public class LifeItemAdapter extends BaseAdapter {

    private List<LifeBean> objects = new ArrayList<>();

    private Context context;
    private LayoutInflater layoutInflater;
    private int icons[] = {R.drawable.zhiwaixianzhishu, R.drawable.ganmaozhisu, R.drawable.chuanyizhisu
            , R.drawable.kongqiwurankuoanzhisu, R.drawable.yundongzhisu};

    public LifeItemAdapter(Context context,List<LifeBean> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects=objects;
    }
    public void setObjects(List<LifeBean> objects){this.objects=objects;}



    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public LifeBean getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=layoutInflater.inflate(R.layout.life_item,null);
            convertView.setTag(new ViewHolde(convertView));
        }

        initializeViews((LifeBean) getItem(position), (ViewHolde) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(LifeBean object,ViewHolde holder,int pos) {
        holder.ivIndex.setBackgroundResource(icons[pos]);
        holder.tvLevel.setText(object.getJibie());
        holder.tvWarmprompt.setText(object.getTishiInfo());
    }

    protected class ViewHolde{
        private ImageView ivIndex;
        private TextView tvLevel;
        private TextView tvWarmprompt;

        public ViewHolde(View view){
            ivIndex = (ImageView) view.findViewById(R.id.iv_index);
            tvLevel = (TextView) view.findViewById(R.id.tv_level);
            tvWarmprompt = (TextView) view.findViewById(R.id.tv_warm_prompt);
        }
    }
}
