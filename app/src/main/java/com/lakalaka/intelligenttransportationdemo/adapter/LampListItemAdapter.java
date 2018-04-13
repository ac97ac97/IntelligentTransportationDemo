package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.LampBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 无情 on 2018/3/19.
 */

public class LampListItemAdapter extends BaseAdapter {

    //给json添加的列表
    private List<LampBean> objects = new ArrayList<>();

    private Context context;

    private LayoutInflater layoutInflater;


    public interface OnsettingListener {
        void OnSetting(View view, int pos);
    }

    public void setObjects(List<LampBean> objects){
        this.objects = objects;
    }

    private  OnsettingListener onsettingListener;

    public void setOnsettingListener(OnsettingListener onsettingListener) {
        this.onsettingListener = onsettingListener;
    }
    public LampListItemAdapter(Context context,List<LampBean> objects){
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
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
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.lamp_list_item,null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((LampBean)getItem(position),(ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private List<LampBean> selectList = new ArrayList<>();

    public List<LampBean> getSelectList() {
        return selectList;
    }

    //初始化视图
    private void initializeViews(final LampBean object, ViewHolder holder, final int position) {
        holder.tvLampLukou.setText(object.getId()+"");
        holder.tvLampHongdeng.setText(object.getRed_light()+"");
        holder.tvLampHuangdeng.setText(object.getYellow_light()+"");
        holder.tvLampLvdeng.setText(object.getGreen_light()+"");

        //给多选选项设置监听器
        holder.cbControlCaozuo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    selectList.add(object);
                }else {
                    selectList.remove(object);
                }
            }
        });
        //给设置设置监听器
        holder.btnLampSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onsettingListener.OnSetting(view,position);
            }
        });


    }

    protected class ViewHolder {
        private TextView tvLampLukou;
        private TextView tvLampHongdeng;
        private TextView tvLampHuangdeng;
        private TextView tvLampLvdeng;
        private CheckBox cbControlCaozuo;
        private Button btnLampSetting;

        public ViewHolder(View view) {
            tvLampLukou = (TextView) view.findViewById(R.id.tv_lamp_lukou);
            tvLampHongdeng = (TextView) view.findViewById(R.id.tv_lamp_hongdeng);
            tvLampHuangdeng = (TextView) view.findViewById(R.id.tv_lamp_huangdeng);
            tvLampLvdeng = (TextView) view.findViewById(R.id.tv_lamp_lvdeng);
            cbControlCaozuo = (CheckBox) view.findViewById(R.id.cb_control_caozuo);
            btnLampSetting = (Button) view.findViewById(R.id.btn_lamp_setting);
        }
    }

}
