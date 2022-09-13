package com.example.tododemo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.tododemo.account.AccountActivity;
import com.example.tododemo.account.UserDialog;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.bean.UserBean;
import com.example.tododemo.classify.ClassifyDialog;
import com.example.tododemo.dialog.NormalDialog;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.example.tododemo.sqlite.DateUtils;
import com.example.tododemo.todo.TodoAdapter;
import com.example.tododemo.todo.TodoDialog;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class MainActivity extends BaseActivity {

    private MaterialToolbar mtb_main;
    private MaterialButton mb_search;
    private FloatingActionButton fab_add;
    private ExtendedFloatingActionButton efab_account;
    private MaterialTextView mtv_classify;
    private MaterialRadioButton radio_plan;
    private MaterialRadioButton radio_finish;
    private RecyclerView rv_todo;
    private List<TodoBean> beanList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSQL();
        initRecyclerView(Constant.TODO_State,Constant.classify);
    }

    private void initRecyclerView(boolean isDone,String classification) {
        mtv_classify.setText("分类："+Constant.classify);
        rv_todo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        beanList = new CRUD(MainActivity.this, Constant.TODO_TABLE_NAME).RetrieveTodo(Constant.username, Constant.TODO_State, classification);
        TodoAdapter adapter1 = new TodoAdapter(beanList, this, isDone);
        rv_todo.setAdapter(adapter1);


        // todo点击事件监听  ->进行编辑修改内容
        adapter1.setOnItemClickListener((adapter, view, position) -> {
            TodoDialog todoDialog = new TodoDialog(MainActivity.this,getSupportFragmentManager());
            todoDialog.show();
            TodoBean bean = beanList.get(position);
            todoDialog.initSetDate(bean);
            todoDialog.setButtonOnClickListener((title, classify, time) -> {
                if(title.isEmpty()){
                    Toast.makeText(MainActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                } else {
                    //插入todo数据
                    CRUD crud=new CRUD(MainActivity.this, Constant.TODO_TABLE_NAME);
                    ContentValues values=new ContentValues();
                    values.put("username",Constant.username);
                    values.put("title",title);
                    values.put("classify",classify);
                    values.put("date",time);
                    values.put("isDone",String.valueOf(Constant.TODO_State));
                    crud.UpdateTodo(values,beanList.get(position).getId());
                    values.clear();
                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    todoDialog.dismiss();
                    initRecyclerView(Constant.TODO_State,Constant.classify);
                }
            });
        });
        adapter1.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                // 获取布局文件
                popupMenu.getMenuInflater().inflate(R.menu.todo_delete, popupMenu.getMenu());
                popupMenu.show();
                // 通过上面这几行代码，就可以把控件显示出来了
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override public boolean onMenuItemClick(MenuItem item) {
                        // 控件每一个item的点击事件
                        switch (item.getItemId()){
                            case R.id.todo_delete:
                                CRUD crud=new CRUD(MainActivity.this, Constant.TODO_TABLE_NAME);
                                crud.DeleteTodo(beanList.get(position).getId());
                                Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                                initRecyclerView(Constant.TODO_State,Constant.classify);
                                break;
                            case R.id.todo_batchRemove:
                                Toast.makeText(MainActivity.this,"批量删除",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        return true;
                    }
                });
                return true;
            }
        });

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
                    values.put("isDone","false");
                    crud.add(values);
                    values.clear();
                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                    todoDialog.dismiss();
                    initRecyclerView(Constant.TODO_State,Constant.classify);
                }
            });
        });


        ClassifyDialog dialog = new ClassifyDialog(MainActivity.this);
        //分类查看
        mtv_classify.setOnClickListener(view -> {
            dialog.show();
        });

        // 分类项点击事件
        dialog.setOnItemClickListener((view, position, list) -> {
           Constant.classify = list.get(position);
           initRecyclerView(Constant.TODO_State,Constant.classify);
           dialog.dismiss();
        });

        // 计划todo页面切换
        radio_plan.setOnClickListener(v -> {
            Constant.TODO_State = false;
            initRecyclerView(Constant.TODO_State,Constant.classify);
        });
        // 已完成todo页面切换
        radio_finish.setOnClickListener(v -> {
            Constant.TODO_State = true;
            initRecyclerView(Constant.TODO_State,Constant.classify);
        });


    }



    @Override
    protected void onResume() {
        super.onResume();
        efab_account.setText(Constant.username);
    }

    /**
     * 重新回到主页面，刷新列表
     */
    @Override
    protected void onStart() {
        super.onStart();
        // 满足登陆后显示该账号相关的todo
        initRecyclerView(Constant.TODO_State,Constant.classify);
    }
}