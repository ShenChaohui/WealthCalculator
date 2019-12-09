package com.zydl.wealthcalculator;

import android.app.Application;

import com.coder.zzq.smartshow.core.SmartShow;

import org.xutils.x;

/**
 * Created by Sch.
 * Date: 2019/11/28
 * description:
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        x.Ext.init(this);
        SmartShow.init(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}
