package com.example.tododemo.todo;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.sqlite.Constant;

import java.util.List;

public class TodoAdapter extends BaseQuickAdapter<TodoBean, BaseViewHolder> {


    public TodoAdapter(@Nullable List<TodoBean> data) {
        super(R.layout.item_todo, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TodoBean todoBean) {
        baseViewHolder.setText(R.id.mtv_todo_title,todoBean.getTitle())
                .setText(R.id.mtv_todo_time,todoBean.getDate())
                .setText(R.id.mb_todo_classify,todoBean.getClassify());
    }
}
