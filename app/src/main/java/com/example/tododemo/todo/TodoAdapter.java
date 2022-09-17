package com.example.tododemo.todo;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;


public class TodoAdapter extends BaseQuickAdapter<TodoBean, BaseViewHolder> {
    private final Context context;
    public TodoAdapter(@Nullable List<TodoBean> data, Context context) {
        super(R.layout.item_todo, data);
        this.context=context;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TodoBean todoBean) {
        baseViewHolder.setText(R.id.mtv_todo_title,todoBean.getTitle())
                .setText(R.id.mtv_todo_time,todoBean.getDate())
                .setText(R.id.mb_todo_classify,todoBean.getClassify());
        MaterialCheckBox checkBox = baseViewHolder.findView(R.id.cb_todo);
        CardView cardView=baseViewHolder.findView(R.id.cv_todo);
        if (checkBox != null) {
            checkBox.setChecked(todoBean.getIsDone());
        }
        if(cardView!=null){
            cardView.setBackground(ContextCompat.getDrawable(context,R.color.white));
        }
    }


}
