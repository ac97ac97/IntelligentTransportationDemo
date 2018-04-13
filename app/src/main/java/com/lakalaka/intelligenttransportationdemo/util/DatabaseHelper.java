package com.lakalaka.intelligenttransportationdemo.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lakalaka.intelligenttransportationdemo.beans.AccountTable;

import java.sql.SQLException;

/**
 * Created by 无情 on 2018/3/18.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String tableName = "sqlite-1.db";
    private Dao<AccountTable, Integer> userDao;
    private boolean isCreate = true;

    public DatabaseHelper(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, tableName, null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, AccountTable.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            // 删除旧的数据库表。
            TableUtils.dropTable(connectionSource, AccountTable.class, true);
            // 重新创建新版的数据库。
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单例获取实例
     */
    private static DatabaseHelper instance;

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context, tableName, null, 3);
        }
        return instance;
    }


    /**
     *  每一个数据库中的表，要有一个获得Dao的方法。 可以使用一种更通用的模板方法如：
     *
     *
     *   public Dao<Class, Integer> getORMLiteDao(Class cls) throws SQLException {
     *        if (dao == null) { dao = getDao(cls); }
     *
     * return dao; }

     */

    public Dao<AccountTable, Integer> getUserDao() {
        if (userDao == null){
            try {
                userDao = getDao(AccountTable.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userDao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();
        userDao = null;
        isCreate = false;
    }

    public boolean isClose() {
        return isCreate;
    }

}
