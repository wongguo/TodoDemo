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

public class RegisterFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        //注册跳转登录
        view.findViewById(R.id.registerToLogin)
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment));


        return view;
    }
}
