package com.example.tododemo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.tododemo.bean.UserBean;
import com.example.tododemo.dialog.NormalDialog;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.example.tododemo.account.AccountActivity;
import com.example.tododemo.account.UserDialog;
import com.example.tododemo.classify.ClassifyDialog;
import com.example.tododemo.todo.TodoAdapter;
import com.example.tododemo.todo.TodoDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;

public class MainActivity extends BaseActivity {

    private MaterialToolbar mtb_main;
    private MaterialButton mb_search;
    private FloatingActionButton fab_add;
    private ExtendedFloatingActionButton efab_account;
    private MaterialTextView mtv_classify;
    private MaterialRadioButton radio_plan;
    private MaterialRadioButton radio_finish;
    private RecyclerView rv_todo;
    private TodoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSQL();
        initRecyclerView();
        initAdapterListener();
    }

    private void initAdapterListener() {
        //todo单击编辑
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });

        //todo长按打开popupmenu
        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                return false;
            }
        });

        //checkBox选中
        adapter.addChildClickViewIds(R.id.cb_todo);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if(view.getId()==R.id.cb_todo){


                }
            }
        });
    }

    private void initRecyclerView() {
        rv_todo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new TodoAdapter(new CRUD(MainActivity.this,Constant.TODO_TABLE_NAME).RetrieveTodo(Constant.username));
        rv_todo.setAdapter(adapter);
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
        mtv_classify = findViewById(R.id.mtv_classify);
        radio_plan = findViewById(R.id.radio_plan);
        radio_finish = findViewById(R.id.radio_finish);
        rv_todo = findViewById(R.id.rv_todo);
    }


    //初始化数据库->自动登录
    protected void initSQL() {
        UserBean userBean = new CRUD(MainActivity.this, Constant.ACCOUNT_TABLE_NAME).isLogin();
        if (userBean != null) {
            Constant.username = userBean.getUsername();
            // 给常量赋值，该常量可作为在未退出程序时是否登陆的一个判断值
            Constant.isLogin = true;
            // 给控件设置登陆用户文本
            efab_account.setText(userBean.getUsername());
        }
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
                UserDialog dialog=new UserDialog(MainActivity.this);
                dialog.show();
                dialog.setOnDismissListener(dialogInterface -> efab_account.setText(Constant.username));
            }
            //如果未登录
            else {
                Intent intent=new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        //搜索功能
        mb_search.setOnClickListener(view -> {
            String title="搜索";
            String hint="请输入搜索内容";
            NormalDialog dialog=new NormalDialog(MainActivity.this,Constant.EDIT_DIALOG,title,hint);
            dialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onNegativeClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        //添加todo
        fab_add.setOnClickListener(view -> {
            TodoDialog todoDialog=new TodoDialog(MainActivity.this,getSupportFragmentManager());
            todoDialog.show();
            todoDialog.setButtonOnClickListener((title, classify, time) -> {
                if(title.isEmpty()){
                    Toast.makeText(MainActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                }else {
                    if (classify.isEmpty()) classify="默认";
                    //插入todo数据
                    CRUD crud=new CRUD(MainActivity.this, Constant.TODO_TABLE_NAME);
                    ContentValues values=new ContentValues();
                    values.put("username",Constant.username);
                    values.put("title",title);
                    values.put("classify",classify);
                    values.put("date",time);
                    values.put("isDone",false);
                    crud.add(values);
                    values.clear();
                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    todoDialog.dismiss();
                    initRecyclerView();
                }
            });
        });

        //分类查看
        mtv_classify.setOnClickListener(view -> {
            ClassifyDialog dialog=new ClassifyDialog(MainActivity.this);
            dialog.show();
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        efab_account.setText(Constant.username);
    }
}