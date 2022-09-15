package com.example.tododemo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tododemo.account.AccountActivity;
import com.example.tododemo.account.UserDialog;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.bean.UserBean;
import com.example.tododemo.classify.ClassifyDialog;
import com.example.tododemo.dialog.NormalDialog;
import com.example.tododemo.search.ResultActivity;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
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
    private TodoAdapter adapter;
    private CRUD crud;
    private FloatingActionButton fab_delete;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSQL();
        initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
    }

    private void initRecyclerView(boolean isDone,String classification) {
        mtv_classify.setText(Constant.CLASSIFY);
        rv_todo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        beanList = crud.RetrieveTodo(Constant.username, Constant.TODO_STATE, classification);
        adapter = new TodoAdapter(beanList);
        rv_todo.setAdapter(adapter);

        // todo点击事件监听  ->进行编辑修改内容
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            TodoDialog todoDialog = new TodoDialog(MainActivity.this,getSupportFragmentManager());
            todoDialog.show();
            TodoBean bean = beanList.get(position);
            todoDialog.initSetDate(bean);
            todoDialog.setButtonOnClickListener((title, classify, time) -> {
                if(title.isEmpty()){
                    Toast.makeText(MainActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                } else {
                    //插入todo数据
                    ContentValues values=new ContentValues();
                    values.put("username",Constant.username);
                    values.put("title",title);
                    values.put("classify",classify);
                    values.put("date",time);
                    values.put("isDone",String.valueOf(Constant.TODO_STATE));
                    crud.UpdateTodo(values,beanList.get(position).getId());
                    values.clear();
                    Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    todoDialog.dismiss();
                    initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
                }
            });
        });

        //item长按开启popupmenu
        adapter.setOnItemLongClickListener((adapter1, view, position) -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
            // 获取布局文件
            popupMenu.getMenuInflater().inflate(R.menu.todo_delete, popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                // 控件每一个item的点击事件
                switch (item.getItemId()){
                    case R.id.todo_delete:
                        crud.DeleteTodo(beanList.get(position).getId());
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
                        break;
                    case R.id.todo_more:
                        fab_delete.setVisibility(View.VISIBLE);
                        fab_add.setBackground(getDrawable(R.drawable.ic_baseline_close_24));
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return true;
            });
            return true;
        });

        //checkbox选中
        adapter.addChildClickViewIds(R.id.cb_todo);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if(view.getId()==R.id.cb_todo) {
                CheckBox checkBox = findViewById(R.id.cb_todo);
                ContentValues values=new ContentValues();
                values.put("isDone",String.valueOf(checkBox.isChecked()));
                TodoBean bean=beanList.get(position);
                crud.UpdateTodo(values,bean.getId());
                adapter.remove(bean);
                values.clear();
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
        fab_delete = findViewById(R.id.fab_delete);
        efab_account = findViewById(R.id.efab_account);
        mtv_classify = findViewById(R.id.mtv_classify);
        radio_plan = findViewById(R.id.radio_plan);
        radio_finish = findViewById(R.id.radio_finish);
        rv_todo = findViewById(R.id.rv_todo);
        crud=new CRUD(MainActivity.this,Constant.TODO_TABLE_NAME);
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
                    Intent intent=new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("search",dialog.getEditTextContent());
                    startActivity(intent);
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
                    initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
                }
            });
        });

        //分类查看
        mtv_classify.setOnClickListener(view -> {
            ClassifyDialog dialog = new ClassifyDialog(MainActivity.this);
            dialog.show();
            // 分类项点击事件
            dialog.setOnItemClickListener((position, list) -> {
                Constant.CLASSIFY = list.get(position);
                initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
                dialog.dismiss();
            });
        });

        // 计划todo页面切换
        radio_plan.setOnClickListener(v -> {
            Constant.TODO_STATE = false;
            initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
        });

        // 已完成todo页面切换
        radio_finish.setOnClickListener(v -> {
            Constant.TODO_STATE = true;
            initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        efab_account.setText(Constant.username);
        initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
    }

    /**
     * 重新回到主页面，刷新列表
     */
    @Override
    protected void onStart() {
        super.onStart();
        // 满足登陆后显示该账号相关的todo
        //initRecyclerView(Constant.TODO_STATE,Constant.CLASSIFY);
    }
}