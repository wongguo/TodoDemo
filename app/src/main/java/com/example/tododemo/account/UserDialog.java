package com.example.tododemo.account;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.tododemo.dialog.BaseDialog;
import com.example.tododemo.dialog.NormalDialog;
import com.example.tododemo.R;
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

public class UserDialog extends BaseDialog {
    private final Context context;
    private MaterialTextView mtv_username;
    private MaterialButton mb_change_name;
    private MaterialButton mb_change_key;
    private MaterialButton mb_exit;
    private MaterialButton mb_delete;
    private CRUD crud;

    public UserDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_account;
    }

    @Override
    protected void initView() {
        mtv_username = findViewById(R.id.mtv_username);
        mtv_username.setText(Constant.username);
        mb_change_name = findViewById(R.id.mb_change_name);
        mb_change_key = findViewById(R.id.mb_change_key);
        mb_exit = findViewById(R.id.mb_exit);
        mb_delete = findViewById(R.id.mb_delete);
        crud = new CRUD(context,Constant.ACCOUNT_TABLE_NAME);
    }


    @Override
    protected void initListener() {
        //修改用户名
        mb_change_name.setOnClickListener(view -> {
            String title="修改用户名";
            String content="请输入用户名";
            NormalDialog normalDialog=new NormalDialog(context,Constant.EDIT_DIALOG,title,content);
            normalDialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    String change_name=normalDialog.getEditTextContent();
                    if(change_name.isEmpty()) {
                        Toast.makeText(context, "输入不为空", Toast.LENGTH_SHORT).show();
                    }else if(crud.isExistSame(change_name)){
                        Toast.makeText(context, "该用户名已存在", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                        ContentValues values=new ContentValues();
                        values.put("username",change_name);
                        crud.UpdateUser(values,Constant.username);
                        values.clear();
                        Constant.username=change_name;
                        mtv_username.setText(Constant.username);
                        normalDialog.dismiss();
                    }
                }

                @Override
                public void onNegativeClick() {
                    normalDialog.dismiss();
                }
            });
            normalDialog.show();
        });
        //修改密码
        mb_change_key.setOnClickListener(view -> {
            String title="修改密码";
            String content="请输入密码";
            NormalDialog normalDialog=new NormalDialog(context,Constant.EDIT_DIALOG,title,content);
            normalDialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    String change_key=normalDialog.getEditTextContent();
                    if(change_key.isEmpty()) {
                        Toast.makeText(context, "输入不为空", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, "修改成功,请重新登录", Toast.LENGTH_SHORT).show();
                        ContentValues values=new ContentValues();
                        values.put("password",change_key);
                        crud.UpdateUser(values,Constant.username);
                        values.clear();
                        initAccount(normalDialog);
                        Intent intent=new Intent(context,AccountActivity.class);
                        context.startActivity(intent);
                    }
                }

                @Override
                public void onNegativeClick() {
                    normalDialog.dismiss();
                }
            });
            normalDialog.show();
        });
        //退出账号
        mb_exit.setOnClickListener(view -> {
            String title="退出账号";
            String content="确认是否退出账号";
            NormalDialog normalDialog=new NormalDialog(context,Constant.MESSAGE_DIALOG,title,content);
            normalDialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    Toast.makeText(context, "退出成功", Toast.LENGTH_SHORT).show();
                    ContentValues values=new ContentValues();
                    values.put("isLogin","false");
                    crud.UpdateUser(values,Constant.username);
                    values.clear();
                    initAccount(normalDialog);
                }

                @Override
                public void onNegativeClick() {
                    normalDialog.dismiss();
                }
            });
            normalDialog.show();
        });
        //注销账号
        mb_delete.setOnClickListener(view -> {
            String title="注销账号";
            String content="确认是否注销账号";
            NormalDialog normalDialog=new NormalDialog(context,Constant.MESSAGE_DIALOG,title,content);
            normalDialog.setItemOnClickListener(new NormalDialog.ItemOnClickListener() {
                @Override
                public void onPositiveClick() {
                    Toast.makeText(context, "注销成功", Toast.LENGTH_SHORT).show();
                    crud.DeleteUser(Constant.username);
                    initAccount(normalDialog);
                }

                @Override
                public void onNegativeClick() {
                    normalDialog.dismiss();
                }
            });
            normalDialog.show();
        });
    }

    private void initAccount(NormalDialog dialog){
        Constant.username="公共账号";
        Constant.isLogin=false;
        dialog.dismiss();
        dismiss();
    }

}
