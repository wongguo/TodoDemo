package com.example.tododemo.account;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tododemo.Bean.UserBean;
import com.example.tododemo.R;
import com.example.tododemo.SQLite.CRUD;
import com.example.tododemo.SQLite.Constant;
import com.example.tododemo.SQLite.UserDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputLayout til_account; // 账号
    private TextInputLayout til_password; // 密码
    private MaterialButton mb_login;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);
        init(view);

        return view;
    }

    private void init(View view) {
        til_account = view.findViewById(R.id.til_account);
        til_password = view.findViewById(R.id.til_password);
        mb_login = view.findViewById(R.id.mb_login);
        //登录跳转注册
        view.findViewById(R.id.loginToRegister)
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment));
        loginAccount();
    }

    private void loginAccount() {
        mb_login.setOnClickListener(view -> {
            // 取出账号和密码两个输入框的文本
            String username = Objects.requireNonNull(til_account.getEditText()).getText().toString();
            String password= Objects.requireNonNull(til_password.getEditText()).getText().toString();
            // 文本为空
            if(username.isEmpty()||password.isEmpty()){
                Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
            }else {
                // 文本不为空，遍历数据库判断有无此账号且密码是否相同
                if (new CRUD(getActivity(),Constant.ACCOUNT_TABLE_NAME).isExist(username,password)){
                    Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                    //更新数据库登陆状态--用户表中相关账号登陆状态 isLogin ->true
                    ContentValues values=new ContentValues();
                    values.put("isLogin", "true");
                    new CRUD(getActivity(),Constant.ACCOUNT_TABLE_NAME).UpdateUser(values,username);
                    Constant.isLogin=true;
                    Constant.username=username;
                    getActivity().finish();
                }else {
                    Toast.makeText(getActivity(), "账号或者密码输入错误", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}
