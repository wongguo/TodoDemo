package com.example.tododemo.Dialog;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.tododemo.R;
import com.example.tododemo.SQLite.Constant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class NormalDialog extends BaseDialog {

    private MaterialTextView mtv_title;
    private MaterialTextView mtv_content;
    private TextInputLayout til_init;
    private MaterialButton mb_confirm;
    private MaterialButton mb_cancel;
    private final String title;
    private final String content;
    private final int DIALOG_STYLE;

    //创建消息提示Dialog
    public NormalDialog(@NonNull Context context,int DIALOG_STYLE,String title,String content) {
        super(context);
        this.DIALOG_STYLE=DIALOG_STYLE;
        this.title=title;
        this.content=content;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_normal;
    }

    @Override
    protected void initView() {
        mtv_title = findViewById(R.id.mtv_title);
        mtv_content = findViewById(R.id.mtv_content);
        til_init = findViewById(R.id.til_init);
        mb_confirm = findViewById(R.id.mb_confirm);
        mb_cancel = findViewById(R.id.mb_cancel);
    }

    @Override
    public void show() {
        super.show();
        setData();
    }

    protected void setData(){
        mtv_title.setText(title);
        if(DIALOG_STYLE== Constant.EDIT_DIALOG){
            mtv_content.setVisibility(View.GONE);
            til_init.setVisibility(View.VISIBLE);
            til_init.setHint(content);
        }else if(DIALOG_STYLE==Constant.MESSAGE_DIALOG){
            mtv_content.setVisibility(View.VISIBLE);
            til_init.setVisibility(View.GONE);
            mtv_content.setText(content);
        }
    }

    @Override
    protected void initListener() {
        mb_confirm.setOnClickListener(view -> {
            if(itemOnClickListener!=null){
                itemOnClickListener.onPositiveClick();
            }
        });
        mb_cancel.setOnClickListener(view -> {
            if(itemOnClickListener!=null){
                itemOnClickListener.onNegativeClick();
            }
        });
    }

    public ItemOnClickListener itemOnClickListener;

    public void setItemOnClickListener(ItemOnClickListener itemOnClickListener) {
        this.itemOnClickListener = itemOnClickListener;
    }

    public interface ItemOnClickListener{
        //确认按钮点击事件
        void onPositiveClick();

        //取消按钮点击事件
        void onNegativeClick();

    }

    public String getEditTextContent(){
        return Objects.requireNonNull(til_init.getEditText()).getText().toString();
    }
}
