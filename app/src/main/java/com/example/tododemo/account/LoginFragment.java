package com.example.tododemo.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.tododemo.R;

public class LoginFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_login,container,false);


        //登录跳转注册
        view.findViewById(R.id.loginToRegister)
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment));

        return view;
    }
}
