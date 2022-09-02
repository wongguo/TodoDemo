package com.example.tododemo;

import android.content.Intent;
import android.os.Bundle;

import com.example.tododemo.Bean.UserBean;
import com.example.tododemo.SQLite.CRUD;
import com.example.tododemo.SQLite.Constant;
import com.example.tododemo.account.AccountActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends BaseActivity {

    private MaterialToolbar mtb_main;
    private MaterialButton mb_search;
    private FloatingActionButton fab_add;
    private ExtendedFloatingActionButton efab_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    /**
     * 初始化控件
     */
    @Override
    protected void initView() {
        mtb_main = findViewById(R.id.mtb_main);
        mb_search = findViewById(R.id.mb_search);
        fab_add = findViewById(R.id.fab_add);
        efab_account = findViewById(R.id.efab_account);
        initSQL();
    }


    //初始化数据库->自动登录
    protected void initSQL(){
        UserBean userBean = new CRUD(MainActivity.this, Constant.ACCOUNT_TABLE_NAME).isLogin();
        if (userBean!= null){
            Constant.username= userBean.getUsername();
            // 给常量赋值，该常量可作为在未退出程序时是否登陆的一个判断值
            Constant.isLogin = true;
            // 给控件设置登陆用户文本
            efab_account.setText(userBean.getUsername());
        }
/*
        for (UserBean userBean : new CRUD(MainActivity.this,Constant.ACCOUNT_TABLE_NAME).RetrieveUser()) {
            if(userBean.getIsLogin().equals("true")){
                Constant.username= userBean.getUsername();
                // 给常量赋值，该常量可作为在未退出程序时是否登陆的一个判断值
                Constant.isLogin = true;
                // 给控件设置登陆用户文本
                efab_account.setText(userBean.getUsername());
                break;
            }
        }*/

/*        UserDatabase userDatabase=new UserDatabase(MainActivity.this);
        SQLiteDatabase sqLiteDatabase=userDatabase.getWritableDatabase();
        //遍历数据库
        Cursor cursor=sqLiteDatabase.query(Constant.ACCOUNT_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int index_user=cursor.getColumnIndex("username");
            String exist_user=cursor.getString(index_user);
            int index_isLogin=cursor.getColumnIndex("isLogin");
            String exist_isLogin=cursor.getString(index_isLogin);
            if("true".equals(exist_isLogin)){
                Constant.username=exist_user;
                Constant.isLogin=true;
                efab_account.setText(exist_user);
                cursor.close();
                break;
            }
        }
        cursor.close();*/
    }


    /**
     * 控件点击事件
     */
    @Override
    protected void initListener() {
        //用户登录界面跳转
        efab_account.setOnClickListener(view -> {
            //如果登录弹出退出登录dialog
            if(Constant.isLogin){

            }
            //如果未登录
            else {
                Intent intent=new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        //搜索功能
        mb_search.setOnClickListener(view -> {

        });

        //添加todo
        fab_add.setOnClickListener(view -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        efab_account.setText(Constant.username);
    }
}