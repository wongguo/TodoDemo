package com.example.tododemo.account;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tododemo.BaseActivity;
import com.example.tododemo.R;
import com.google.android.material.appbar.MaterialToolbar;

public class AccountActivity extends BaseActivity {

    private MaterialToolbar mtb_account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        mtb_account = findViewById(R.id.mtb_account);
    }

    @Override
    protected void initListener() {
        mtb_account.setNavigationOnClickListener(view -> finish());
    }

}
