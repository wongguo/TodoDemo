package com.example.tododemo;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

abstract public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initView();
        initListener();
    }

    /**
     * 绑定视图
     * @return 布局
     */
    @LayoutRes
    abstract  protected int getLayoutRes();

    /**
     * 初始化控件
     */
    abstract protected void initView();

    /**
     * 设置控件点击事件
     */
    abstract protected void initListener();
}
