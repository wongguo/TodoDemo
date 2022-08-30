package com.example.tododemo;

import android.app.Application;
import android.content.Context;

// 设置此类达到可以全局获取context的效果
public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        // 调用getApplicationContext()方法得到应用程序基本的Context
        context= getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }
}
