package com.example.tododemo.account;

import android.content.ContentValues;
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
import com.example.tododemo.sqlite.CRUD;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    private MaterialButton mb_register;
    private TextInputLayout til_repassword;
    private TextInputLayout til_reg_password;
    private TextInputLayout til_reg_account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        init(view);

        return view;
    }

    /**
     * 初始化控件
     *
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

        saveAccount(view);
    }

    //保存账号
    private void saveAccount(View view) {
        mb_register.setOnClickListener(view1 -> {
            String username = Objects.requireNonNull(til_reg_account.getEditText()).getText().toString();
            String password = Objects.requireNonNull(til_reg_password.getEditText()).getText().toString();
            String rePassword = Objects.requireNonNull(til_repassword.getEditText()).getText().toString();
            if (username.isEmpty() || password.isEmpty() || rePassword.isEmpty()) {
                Toast.makeText(getActivity(), "输入不能为空", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(rePassword)) {
                Toast.makeText(getActivity(), "密码输入不一致", Toast.LENGTH_SHORT).show();
            } else {
                if (!new CRUD(getActivity(), Constant.ACCOUNT_TABLE_NAME).isExistSame(username)) {
                    //注册账号
                    ContentValues values = new ContentValues();
                    values.put("username", username);
                    values.put("password", password);
                    values.put("isLogin", "false");
                    new CRUD(getActivity(), Constant.ACCOUNT_TABLE_NAME).add(values);
                    values.clear();
                    //注册跳转登录
                    NavController controller = Navigation.findNavController(view);
                    controller.navigate(R.id.action_registerFragment_to_loginFragment);
                    Toast.makeText(getActivity(), "注册成功，跳转回登录界面", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "该用户已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
