package com.example.tododemo.classify;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.dialog.BaseDialog;
import com.example.tododemo.R;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ClassifyDialog extends BaseDialog {

    private Context context;
    private RecyclerView rv_classify;
    private MaterialButton mb_cancel_classify;
    private MaterialButton mb_confirm_classify;

    public ClassifyDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_classify;
    }

    @Override
    protected void initView() {
        rv_classify = findViewById(R.id.rv_classify);
        mb_cancel_classify = findViewById(R.id.mb_cancel_classify);

        initRecyclerView();
    }

    private void initRecyclerView() {
        List<TodoBean> data =new CRUD(context,Constant.TODO_TABLE_NAME).RetrieveTodo(Constant.username);
        List<String> list=new ArrayList<>();
        list.add("默认");
        for(TodoBean bean:data){
            String classify=bean.getClassify();
            if(!list.contains(classify)) list.add(classify);
        }
        rv_classify.setLayoutManager(new LinearLayoutManager(context));
        ClassifyDialogAdapter adapter=new ClassifyDialogAdapter(list);
        rv_classify.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter1, view, position) -> {

        });
    }

    @Override
    protected void initListener() {
        mb_cancel_classify.setOnClickListener(view -> dismiss());
    }
}
