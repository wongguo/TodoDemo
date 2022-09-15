package com.example.tododemo.classify;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
        LinearLayout layout=new LinearLayout(getContext());
        MaterialTextView textView=new MaterialTextView(getContext());
        layout.addView(textView);
        layout.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setId(R.id.mtv_search_classify);
        return createBaseViewHolder(layout);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.mtv_search_classify,s);
    }
}
