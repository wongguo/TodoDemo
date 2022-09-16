package com.example.tododemo.search;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.example.tododemo.BaseActivity;
import com.example.tododemo.MainActivity;
import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.dialog.NormalDialog;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.example.tododemo.todo.TodoAdapter;
import com.example.tododemo.todo.TodoDialog;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.List;

public class ResultActivity extends BaseActivity {

    private RecyclerView rv_result;
    private String search;
    private CRUD crud;
    private MaterialToolbar mtb_result;


    private void initRecyclerView() {
        rv_result.setLayoutManager(new LinearLayoutManager(ResultActivity.this));
        List<TodoBean> list=new CRUD(this, Constant.TODO_TABLE_NAME).SearchTodo(search);
        TodoAdapter adapter=new TodoAdapter(list,this);
        rv_result.setAdapter(adapter);
        //单击编辑
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            TodoDialog todoDialog = new TodoDialog(ResultActivity.this,getSupportFragmentManager());
            todoDialog.show();
            TodoBean bean = list.get(position);
            todoDialog.initSetDate(bean);
            todoDialog.setButtonOnClickListener((title, classify, time) -> {
                if(title.isEmpty()){
                    Toast.makeText(ResultActivity.this, "待办事项不为空", Toast.LENGTH_SHORT).show();
                } else {
                    //插入todo数据
                    ContentValues values=new ContentValues();
                    values.put("username",Constant.username);
                    values.put("title",title);
                    values.put("classify",classify);
                    values.put("date",time);
                    values.put("isDone",String.valueOf(Constant.TODO_STATE));
                    crud.UpdateTodo(values,list.get(position).getId());
                    values.clear();
                    Toast.makeText(ResultActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    todoDialog.dismiss();
                    initRecyclerView();
                }
            });
        });
        //长按删除
        adapter.setOnItemLongClickListener((adapter1, view, position) -> {
            String title="删除";
            String content="是否要删除该Todo";
            NormalDialog normalDialog=new NormalDialog(ResultActivity.this,Constant.MESSAGE_DIALOG,title,content);
            normalDialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    crud.DeleteTodo(list.get(position).getId());
                    Toast.makeText(ResultActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    adapter.remove(list.get(position));
                    normalDialog.dismiss();
                }

                @Override
                public void onNegativeClick() {
                    normalDialog.dismiss();
                }
            });
            normalDialog.show();
            return true;
        });
        //checkbox选中
        adapter.addChildClickViewIds(R.id.cb_todo);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            if(view.getId()==R.id.cb_todo) {
                CheckBox checkBox = findViewById(R.id.cb_todo);
                ContentValues values=new ContentValues();
                values.put("isDone",String.valueOf(checkBox.isChecked()));
                TodoBean bean=list.get(position);
                crud.UpdateTodo(values,bean.getId());
                values.clear();
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    protected void initView() {
        mtb_result = findViewById(R.id.mtb_result);
        rv_result = findViewById(R.id.rv_result);
        search = getIntent().getStringExtra("search");
        crud = new CRUD(this,Constant.TODO_TABLE_NAME);
        initRecyclerView();
    }

    @Override
    protected void initListener() {
        mtb_result.setNavigationOnClickListener(view -> finish());
    }
}
