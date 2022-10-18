package com.example.tododemo.sqlite.classify;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tododemo.R;
import com.example.tododemo.dialog.BaseDialog;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ClassifyDialog extends BaseDialog {

    private final Context context;
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
        List<String> list =new CRUD(context,Constant.TODO_TABLE_NAME).RetrieveTodoClassify(Constant.username);
        rv_classify.setLayoutManager(new LinearLayoutManager(context));
        ClassifyDialogAdapter adapter=new ClassifyDialogAdapter(list);
        rv_classify.setAdapter(adapter);
        // 分类item点击事件
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            if (onItemClickListener!=null){
                onItemClickListener.OnClassifyItemClickListener(position,list);
            }
        });
    }

    @Override
    protected void initListener() {
        mb_cancel_classify.setOnClickListener(view -> dismiss());
    }

    public interface OnItemClickListener{
        // 1.点击view 2.列表位置 3.列表
        void OnClassifyItemClickListener(int position, List<String> list);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


}
