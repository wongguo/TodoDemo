package com.example.tododemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.tododemo.account.AccountActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private MaterialToolbar mtb_main;
    private MaterialButton mb_search;
    private FloatingActionButton fab_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        initListener();
    }

    /**
     * 初始化控件
     */
    private void init(){
        mtb_main = findViewById(R.id.mtb_main);
        mb_search = findViewById(R.id.mb_search);
        fab_add = findViewById(R.id.fab_add);
    }

    /**
     * 控件点击事件
     */
    private void initListener() {
        //用户登录界面跳转
        mtb_main.setNavigationOnClickListener(view -> {
            //如果未登录
            Intent intent=new Intent(MainActivity.this, AccountActivity.class);
            startActivity(intent);
            //如果登录弹出退出登录dialog

        });

        //搜索功能
        mb_search.setOnClickListener(view -> {

        });

        //添加todo
        fab_add.setOnClickListener(view -> {

        });
    }
}