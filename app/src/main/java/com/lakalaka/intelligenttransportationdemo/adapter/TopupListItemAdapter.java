package com.lakalaka.intelligenttransportationdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lakalaka.intelligenttransportationdemo.R;
import com.lakalaka.intelligenttransportationdemo.beans.AccountTable;
import com.lakalaka.intelligenttransportationdemo.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/21.
 * 充值中心fragment listview适配器
 */

public class TopupListItemAdapter extends BaseAdapter {
    private List<AccountTable> objects = new ArrayList<>();
    private Context context;
    private LayoutInflater layoutInflater;
    private DatabaseHelper helper;



    public TopupListItemAdapter(Context context, List<AccountTable> objects) {
        this.objects = objects;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }



    public void setObjects(List<AccountTable> objects){
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
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.topup_list_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }

        initializeViews((AccountTable) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }


    private void initializeViews(AccountTable object, ViewHolder holder) {

        holder.tvListTime.setText(object.getAccountTime().substring(0, 10) + "\n" + object.getAccountWeek());
        holder.tvTopupPerson.setText("充值人:" + object.getAccountPerson());
        holder.tvTopupCarNum.setText("车牌号:" + object.getCarId());
        holder.tvTopupAdd.setText("充值:" + object.getCarMoney());
        holder.tvTopupYue.setText("余额:" + object.getCarBalance());
        holder.tvTopupAddTime.setText(object.getAccountTime());

//        helper = new DatabaseHelper(context, "sqlite-1", null, 1);
//
//        SQLiteDatabase db = helper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("select * from accounts order by accountTime desc",null);
//        while (cursor.moveToNext()) {
//            String time = cursor.getString(cursor.getColumnIndex("accountTime"));
//            Log.d("abc", time);
//        }
//
//        cursor.close();
    }

    protected class ViewHolder {
        private TextView tvListTime;
        private TextView tvTopupPerson;
        private TextView tvTopupCarNum;
        private TextView tvTopupAdd;
        private TextView tvTopupYue;
        private TextView tvTopupAddTime;

        public ViewHolder(View view) {
            tvListTime = view.findViewById(R.id.tv_list_time);
            tvTopupPerson = view.findViewById(R.id.tv_topup_person);
            tvTopupCarNum = view.findViewById(R.id.tv_topup_car_num);
            tvTopupAdd = view.findViewById(R.id.tv_topup_add);
            tvTopupYue = view.findViewById(R.id.tv_topup_yue);
            tvTopupAddTime = view.findViewById(R.id.tv_topup_add_time);
        }
    }
}
