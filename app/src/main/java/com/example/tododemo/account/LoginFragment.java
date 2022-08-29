package com.example.tododemo.account;

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

import com.example.tododemo.R;
import com.example.tododemo.SQLite.Constant;
import com.example.tododemo.SQLite.UserDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LoginFragment extends Fragment {

    private TextInputLayout til_account;
    private TextInputLayout til_password;
    private MaterialButton mb_login;
    private boolean isLogin=false;

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
        mb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username= Objects.requireNonNull(til_account.getEditText()).getText().toString();
                String password= Objects.requireNonNull(til_password.getEditText()).getText().toString();
                if(username.isEmpty()||password.isEmpty()){
                    Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
                }else {
                    UserDatabase userDatabase=new UserDatabase(getActivity());
                    SQLiteDatabase sqLiteDatabase=userDatabase.getWritableDatabase();
                    //遍历数据库
                    Cursor cursor=sqLiteDatabase.query(Constant.ACCOUNT_TABLE_NAME,null,null,null,null,null,null);
                    while (cursor.moveToNext()){
                        int index_user=cursor.getColumnIndex("username");
                        String exist_user=cursor.getString(index_user);
                        int index_password=cursor.getColumnIndex("password");
                        String exist_password=cursor.getString(index_password);

                        if(exist_user.equals(username)&&exist_password.equals(password)){
                            Toast.makeText(getActivity(), "登陆成功", Toast.LENGTH_SHORT).show();
                            cursor.close();
                            isLogin=true;
                            getActivity().finish();
                            break;
                        }

                    }
                    if(!isLogin)Toast.makeText(getActivity(), "账号或者密码输入错误", Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
            }
        });

    }
}
