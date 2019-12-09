package com.zydl.wealthcalculator.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zydl.wealthcalculator.model.FundAccount;
import com.zydl.wealthcalculator.model.HistoryBean;

import java.sql.SQLException;

/**
 * 数据库操作类
 * Created by Genius on 2017/7/7/0007.
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_NAME = "cm_ct.db";
    //数据库名
    private static final int DB_VERSION = 2;
    //数据库版本
    private static DatabaseHelper instance;

    //Helper单例
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, FundAccount.class);
            TableUtils.createTable(connectionSource, HistoryBean.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, FundAccount.class, true);
            TableUtils.dropTable(connectionSource, HistoryBean.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 双重加锁检查
     *
     * @param context 上下文
     * @return 单例
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null) {
                    instance = new DatabaseHelper(context);
                }
            }
        }
        return instance;
    }
}
