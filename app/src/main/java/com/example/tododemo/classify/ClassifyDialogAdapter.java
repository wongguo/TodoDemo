package com.example.tododemo.classify;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.tododemo.R;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.textview.MaterialTextView;

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
        textView.setPadding(10,10,10,10);
        textView.setTextSize(16);
        textView.setMaxEms(5);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setLines(1);
        // 设置文字剧中
        textView.setGravity(Gravity.CENTER);
        // 即textView设置宽度高度为填满父布局
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return createBaseViewHolder(textView);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        if (s.equals(Constant.CLASSIFY)){
            baseViewHolder.setText(R.id.mtv_search_classify,s);
            baseViewHolder.setTextColor(R.id.mtv_search_classify, Color.parseColor("#000000"));
        }else {
            baseViewHolder.setText(R.id.mtv_search_classify,s);
        }
    }
}
