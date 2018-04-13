package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.ChartDataBean;
import com.lakalaka.intelligenttransportationdemo.beans.LampBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 无情 on 2018/3/22.
 */

public class CreativeListAdapter extends BaseAdapter {

//    private List<LampBean> objects = new ArrayList<>();

    private List<LampBean> objects = new ArrayList<>();

    private int a[];

    private int[] colors = {
            0,
            Color.parseColor("#6ab82e"),
            Color.parseColor("#ece93a"),
            Color.parseColor("#f49b25"),
            Color.parseColor("#e33532"),
            Color.parseColor("#b01e23"),
    };

    private Context context;

    private LayoutInflater layoutInflater;

    List<ChartDataBean> status = new ArrayList<>();


    public CreativeListAdapter(Context context, List<LampBean> objects, List<ChartDataBean> status) {
        this.objects = objects;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.status = status;
    }


    public void setStatus(List<ChartDataBean> status) {
        this.status=status;
    }


    public void setObjects(List<LampBean> objects) {
        this.objects = objects;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    private List<ChartDataBean> selectList = new ArrayList<>();

    public List<ChartDataBean> getSelectList() {
        return selectList;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.fragment_creative_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }


        initializeViews((LampBean) getItem(position), (ViewHolder) convertView.getTag(), position);

        return convertView;
    }

    private void getCount(int size) {
    }


    private void initializeViews(LampBean object, final ViewHolder holder, int position) {


        String[] names = {"1", "2", "3", "4", "5", "6", "7"};


        holder.tvLampLukouId.setText(names[position] + "");


        String a = status.toString();

        Pattern p = Pattern.compile("^\\D+|\\D+$");
        Matcher m = p.matcher(a);
        a = m.replaceAll("");

        List<String> aa = new ArrayList<>();
        for (String b : a.split(",")) {
            aa.add(b);
        }



        String[] array = aa.toArray(new String[0]);
        for (int i = 0; i < array.length; i++) {
            Log.d("TAG", "initializeViews: " + array[i]);
        }

        holder.tvLampStatus.setBackgroundColor(colors[Integer.parseInt(array[position])]);
        holder.tvLampStatus.setText(array[position]);
        double crowdedness = Double.parseDouble(array[position]);
        int actualTime = object.getGreen_light();

        if (crowdedness>3) {
            actualTime = 30;
        }

        holder.tvLampStatus.setText(crowdedness+"");



        holder.tvLampNumber.setText(object.getId() + "");
        holder.tvLampLvdeng.setText(object.getGreen_light() + "");

        Log.d("Tag", "111 "+object.getGreen_light());


        holder.tvCreateLampTime.setText(object.getGreen_light() + "");
        //holder.tvChangeTime.setText(object.getGreen_light()+ "");
        holder.tvCreateLampTime.setText(actualTime + "");
//        holder.tvChangeTime.setText(object.getGreen_light()+ "");


    }


    protected class ViewHolder {
        private TextView tvLampLukouId;
        private TextView tvLampStatus;
        private TextView tvLampNumber;
        private TextView tvLampLvdeng;
        private TextView tvCreateLampTime;
        private TextView tvChangeTime;


        public ViewHolder(View view) {
            tvLampLukouId = (TextView) view.findViewById(R.id.tv_lamp_lukou_id);
            tvLampStatus = (TextView) view.findViewById(R.id.tv_lamp_status);
            tvLampNumber = (TextView) view.findViewById(R.id.tv_lamp_number);
            tvLampLvdeng = (TextView) view.findViewById(R.id.tv_lamp_lvdeng);
            tvCreateLampTime = (TextView) view.findViewById(R.id.tv_create_lamp_time);
            tvChangeTime = (TextView) view.findViewById(R.id.tv_change_time);


        }
    }
}
