package com.example.tododemo.todo;


import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TodoAdapter extends BaseQuickAdapter<TodoBean, BaseViewHolder> {
    private Context context;
    private boolean isDone;
    public TodoAdapter(@Nullable List<TodoBean> data, Context context,boolean isDone) {
        super(R.layout.item_todo, data);
        this.context = context;
        this.isDone = isDone;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, TodoBean todoBean) {
        MaterialCheckBox checkBox = baseViewHolder.findView(R.id.cb_todo);
        baseViewHolder.setText(R.id.mtv_todo_title,todoBean.getTitle())
                .setText(R.id.mtv_todo_time,todoBean.getDate())
                .setText(R.id.mb_todo_classify,todoBean.getClassify());
        if (isDone){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    // 1秒钟后消除该item,后续可以查看是否存在绘制时间
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            // 标志
                            message.what = 1;
                            // 需要删除的item
                            message.obj = todoBean;
                            handler.sendMessage(message);
                        }
                    },1000);
                }else {
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Message message = new Message();
                            // 标志
                            message.what = 2;
                            // 需要删除的item
                            message.obj = todoBean;
                            handler.sendMessage(message);
                        }
                    },1000);
                }
            }
        });
    }
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            TodoBean bean = (TodoBean) msg.obj;
            // 借助BRAVH的remove方法移除列表中的item
            remove(bean);
            if (msg.what==1){
                // 更新完成todo的状态
                ContentValues values = new ContentValues();
                values.put("isDone","true");
                new CRUD(context, Constant.TODO_TABLE_NAME).UpdateTodo(values,bean.getId());
                values.clear();
/*                // 如果为普通RV适配器可以使用
                data.remove(position);
                notifyDataSetChanged();*/
            }else {
                // 更新未完成todo的状态
                ContentValues values = new ContentValues();
                values.put("isDone","false");
                new CRUD(context, Constant.TODO_TABLE_NAME).UpdateTodo(values,bean.getId());
                values.clear();
            }
            return false;
        }
    });
}
