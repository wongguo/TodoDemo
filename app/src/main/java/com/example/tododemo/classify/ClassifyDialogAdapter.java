package com.example.tododemo.classify;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tododemo.R;
import com.example.tododemo.bean.TodoBean;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.List;

public class ClassifyDialogAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public ClassifyDialogAdapter(@Nullable List<String> data) {
        super(0, data);
    }

    @NonNull
    @Override
    protected BaseViewHolder onCreateDefViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialTextView textView=new MaterialTextView(getContext());
        textView.setId(R.id.mtv_search_classify);
        return createBaseViewHolder(textView);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.mtv_search_classify,s);
    }
}
