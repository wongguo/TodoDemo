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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.tododemo.R;
import com.example.tododemo.SQLite.Constant;
import com.example.tododemo.SQLite.UserDatabase;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private MaterialButton mb_register;
    private TextInputLayout til_repassword;
    private TextInputLayout til_reg_password;
    private TextInputLayout til_reg_account;
    private UserDatabase userDatabase;
    private SQLiteDatabase sqLiteDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        init(view);

        return view;
    }

    /**
     * 初始化控件
     * @param view 视图
     */
    private void init(View view) {
        til_reg_account = view.findViewById(R.id.til_reg_account);
        til_reg_password = view.findViewById(R.id.til_reg_password);
        til_repassword = view.findViewById(R.id.til_repassword);
        mb_register = view.findViewById(R.id.mb_register);
        //注册跳转登录
        view.findViewById(R.id.registerToLogin)
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment));
        //数据库加载
        userDatabase = new UserDatabase(getActivity());
        sqLiteDatabase = userDatabase.getWritableDatabase();

        saveAccount(view);
    }

    //保存账号
    private void saveAccount(View view){
        mb_register.setOnClickListener(view1 -> {
            String username= Objects.requireNonNull(til_reg_account.getEditText()).getText().toString();
            String password= Objects.requireNonNull(til_reg_password.getEditText()).getText().toString();
            String rePassword= Objects.requireNonNull(til_repassword.getEditText()).getText().toString();
            if(username.isEmpty()||password.isEmpty()||rePassword.isEmpty()){
                Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
            }else if(!password.equals(rePassword)){
                Toast.makeText(getActivity(), "密码输入不一致", Toast.LENGTH_SHORT).show();
            }else {
                if(!isExist(username)) {
                    //注册账号
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", password);
                    values.put("isLogin","false");
                    sqLiteDatabase.insert(Constant.ACCOUNT_TABLE_NAME, null, values);
                    values.clear();
                    //注册跳转登录
                    view1.findViewById(R.id.registerToLogin)
                            .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment));
                    Toast.makeText(getActivity(), "注册成功，跳转回登录界面", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "该用户已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //判断是否存在该用户
    private boolean isExist(String register_name){
        //遍历数据库
        Cursor cursor=sqLiteDatabase.query(Constant.ACCOUNT_TABLE_NAME,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            int index_user=cursor.getColumnIndex("username");
            String exist_user=cursor.getString(index_user);
            if(exist_user.equals(register_name)){
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }


}
