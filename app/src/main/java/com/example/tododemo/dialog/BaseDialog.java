package com.example.tododemo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.example.tododemo.R;

abstract public class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
