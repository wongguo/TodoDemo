package com.example.tododemo.sqlite.classify;

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
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return createBaseViewHolder(textView);
    }


    @Override
    protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
        baseViewHolder.setText(R.id.mtv_search_classify,s);
    }
}
