package com.lakalaka.intelligenttransportationdemo.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.lakalaka.intelligenttransportationdemo.beans.AccountTable;
import com.lakalaka.intelligenttransportationdemo.util.DatabaseHelper;

import java.sql.SQLException;
import java.util.List;

/**  Dao类进行增删改查
 * Created by 无情 on 2018/3/18.
 */

public class AccountDao {
    private Context context;
    //两个泛型约束 一个是对应的实体类类型，一个是主键类型
    private Dao<AccountTable, Integer> userDaoOpe;
    public static DatabaseHelper helper;

    public AccountDao(Context context) {
        this.context = context;
    }

    public int addAccountTable(AccountTable accountTable) {
        int result = 0;
        try {
            userDaoOpe = DatabaseHelper.getInstance(context).getUserDao();
            userDaoOpe.create(accountTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AccountTable> getAccountTableAll() {
        List<AccountTable> tableList = null;
        try {
            userDaoOpe = DatabaseHelper.getInstance(context).getUserDao();
            tableList = userDaoOpe.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableList;
    }


}
