package com.example.tododemo;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    private MaterialButton mb_search;
    private FloatingActionButton fab_add;
    private ExtendedFloatingActionButton efab_account;
    private MaterialTextView mtv_classify;
    private RecyclerView rv_todo;
    private List<TodoBean> beanList;
    private TodoAdapter adapter;
    private CRUD crud;
    private FloatingActionButton fab_delete;
    private int SELECT_MOD = 0; // 选择模式 0为普通模式，1为多选删除模式
    private final List<String> delete_ids = new ArrayList<>(); // 存储多选删除的todo id
    private TabLayout tl_todo;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSQL();
        initRecyclerView(Constant.CLASSIFY);
    }

    private void initRecyclerView(String classification) {
        mtv_classify.setText(Constant.CLASSIFY);
        rv_todo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        beanList = crud.RetrieveTodo(Constant.username, Constant.TODO_STATE, classification);
        adapter = new TodoAdapter(beanList, MainActivity.this);
        rv_todo.setAdapter(adapter);


        adapter.addChildClickViewIds(R.id.cb_todo, R.id.cv_todo);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            //checkbox设置
            if (view.getId() == R.id.cb_todo ) {
                    CheckBox checkBox = findViewById(view.getId());
                if(SELECT_MOD==0) {
                    checkBox.setClickable(true);
                    ContentValues values = new ContentValues();
                    values.put("isDone", String.valueOf(checkBox.isChecked()));
                    TodoBean bean = beanList.get(position);
                    crud.UpdateTodo(values, bean.getId());
                    adapter.remove(bean);
                    values.clear();
                }
                else if(SELECT_MOD==1){
                    checkBox.setChecked(Constant.TODO_STATE);
                    checkBox.setClickable(false);
                }
            }
            if (view.getId() == R.id.cv_todo) {
                //点击进行编辑
                if (SELECT_MOD == 0) {
                    TodoDialog todoDialog = new TodoDialog(MainActivity.this, getSupportFragmentManager());
                    todoDialog.show();
                    TodoBean bean = beanList.get(position);
                    todoDialog.initSetDate(bean);
                    todoDialog.setButtonOnClickListener((title, classify, time) -> {
                        if (title.isEmpty()) {
                            Toast.makeText(MainActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                        } else {
                            //插入todo数据
                            if (classify.isEmpty()) classify = "默认";
                            ContentValues values = new ContentValues();
                            values.put("username", Constant.username);
                            values.put("title", title);
                            values.put("classify", classify);
                            values.put("date", time);
                            values.put("isDone", String.valueOf(Constant.TODO_STATE));
                            crud.UpdateTodo(values, beanList.get(position).getId());
                            values.clear();
                            Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            todoDialog.dismiss();
                            initRecyclerView(Constant.CLASSIFY);
                        }
                    });
                }
                //多选模式下选则删除项
                else if (SELECT_MOD == 1) {
                    // 获取点击item的id
                    int id = beanList.get(position).getId();
                    // 如果颜色是白色，即从未选中->选中
                    if (view.getBackground().getConstantState() == ContextCompat.getDrawable(this, R.color.white).getConstantState()) {
                        // 将item变成选中颜色
                        view.setBackground(ContextCompat.getDrawable(this, R.color.gray));
                        // 往id集合添加此item的id
                        delete_ids.add(String.valueOf(id));
                    } else {
                        view.setBackground(ContextCompat.getDrawable(this, R.color.white));
                        //remove为int 类型会判断为索引移除然后溢出，添加（object）强转
                        delete_ids.remove(String.valueOf(id));
                    }
                }
            }
        });

        // item的卡片布局，设置长按事件
        adapter.addChildLongClickViewIds(R.id.cv_todo);
        // 长按监听事件
        adapter.setOnItemChildLongClickListener((adapter, view, position) -> {
            // 点击view为cardView的id且模式为0->唤出弹窗
            if (view.getId() == R.id.cv_todo && SELECT_MOD == 0) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                // 获取布局文件
                popupMenu.getMenuInflater().inflate(R.menu.todo_delete, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {
                    // 控件每一个item的点击事件
                    switch (item.getItemId()) {
                        case R.id.todo_delete:
                            String title = "删除";
                            String content = "请问是否删除该todo";
                            NormalDialog dialog = new NormalDialog(this, Constant.MESSAGE_DIALOG, title, content);
                            dialog.show();
                            dialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                                @Override
                                public void onPositiveClick() {
                                    // 获取点击item的id，根据此id进行删除
                                    crud.DeleteTodo(beanList.get(position).getId());
                                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                                    // 更新列表
                                    initRecyclerView(Constant.CLASSIFY);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onNegativeClick() {
                                    dialog.dismiss();
                                }
                            });
                            break;
                        case R.id.todo_more:
                            // 显示多选删除的悬浮按钮
                            fab_delete.setVisibility(View.VISIBLE);
                            // 同时将添加todo按钮的图像加载成取消样式
                            fab_add.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_close_24));
                            // 变更为多选模式
                            SELECT_MOD = 1;
                            break;
                        default:
                            // 其他按钮进行异常报错
                            throw new IllegalStateException("Unexpected value: " + item.getItemId());
                    }
                    return true;
                });
            }
            return true;
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
        mb_search = findViewById(R.id.mb_search);
        fab_add = findViewById(R.id.fab_add);
        fab_delete = findViewById(R.id.fab_delete);
        efab_account = findViewById(R.id.efab_account);
        mtv_classify = findViewById(R.id.mtv_classify);
        tl_todo = findViewById(R.id.tl_todo);
        rv_todo = findViewById(R.id.rv_todo);
        crud = new CRUD(MainActivity.this, Constant.TODO_TABLE_NAME);
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
            if (Constant.isLogin) {
                UserDialog dialog = new UserDialog(MainActivity.this);
                dialog.show();
                // 弹窗消失的监听事件
                dialog.setOnDismissListener(dialog1 -> {
                    efab_account.setText(Constant.username);
                    initRecyclerView(Constant.CLASSIFY);
                });

            }
            //如果未登录
            else {
                Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(intent);
            }
        });

        //搜索功能
        mb_search.setOnClickListener(view -> {
            String title = "搜索";
            String hint = "请输入搜索内容";
            NormalDialog dialog = new NormalDialog(MainActivity.this, Constant.EDIT_DIALOG, title, hint);
            dialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra("search", dialog.getEditTextContent());
                    intent.putExtra("userName", Constant.username);
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
            //普通模式
            if (SELECT_MOD == 0) {
                TodoDialog todoDialog = new TodoDialog(MainActivity.this, getSupportFragmentManager());
                todoDialog.show();
                todoDialog.setButtonOnClickListener((title, classify, time) -> {
                    if (title.isEmpty()) {
                        Toast.makeText(MainActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                    } else {
                        if (classify.isEmpty()) classify = "默认";
                        //插入todo数据
                        ContentValues values = new ContentValues();
                        values.put("username", Constant.username);
                        values.put("title", title);
                        values.put("classify", classify);
                        values.put("date", time);
                        values.put("isDone", "false");
                        crud.add(values);
                        values.clear();
                        Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                        todoDialog.dismiss();
                        initRecyclerView(Constant.CLASSIFY);
                    }
                });
            }
            //多选删除模式
            else if (SELECT_MOD == 1) {
                // 隐藏
                fab_delete.setVisibility(View.GONE);
                fab_add.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_24));
                SELECT_MOD = 0;
                initRecyclerView(Constant.CLASSIFY);
            }
        });

        // 删除选中todos
        fab_delete.setOnClickListener(view -> {
            if(delete_ids.isEmpty()){
                Toast.makeText(MainActivity.this, "选择为空", Toast.LENGTH_SHORT).show();
            }else {
                String title = "删除全部";
                String content = "请问是否删除选中todo";
                NormalDialog dialog = new NormalDialog(this, Constant.MESSAGE_DIALOG, title, content);
                dialog.show();
                dialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                    @Override
                    public void onPositiveClick() {
                        crud.DeleteTodos(delete_ids);
                        initRecyclerView(Constant.CLASSIFY);
                        Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        // 隐藏
                        fab_delete.setVisibility(View.GONE);
                        fab_add.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_baseline_add_24));
                        SELECT_MOD = 0;
                        initRecyclerView(Constant.CLASSIFY);
                    }

                    @Override
                    public void onNegativeClick() {
                        dialog.dismiss();
                    }
                });
            }
        });

        //分类查看
        mtv_classify.setOnClickListener(view -> {
            ClassifyDialog dialog = new ClassifyDialog(MainActivity.this);
            dialog.show();
            // 分类项点击事件
            dialog.setOnItemClickListener((position, list) -> {
                Constant.CLASSIFY = list.get(position);
                initRecyclerView(Constant.CLASSIFY);
                dialog.dismiss();
            });
        });

        //TabLayout切换todo列表
        tl_todo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position=tab.getPosition();
                if(position==0){
                    Constant.TODO_STATE = false;
                    initRecyclerView(Constant.CLASSIFY);
                }
                else if(position==1){
                    Constant.TODO_STATE = true;
                    initRecyclerView(Constant.CLASSIFY);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tl_todo.addTab(tl_todo.newTab().setText(R.string.tab_todo));
        tl_todo.addTab(tl_todo.newTab().setText(R.string.tab_done));


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onResume() {
        super.onResume();
        efab_account.setText(Constant.username);
        initRecyclerView(Constant.CLASSIFY);
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