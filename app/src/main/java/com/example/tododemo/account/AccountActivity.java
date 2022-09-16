package com.example.tododemo.account;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.tododemo.BaseActivity;
import com.example.tododemo.R;
import com.example.tododemo.sqlite.Constant;
import com.google.android.material.appbar.MaterialToolbar;

public class AccountActivity extends BaseActivity {

    private MaterialToolbar mtb_account;
    private Fragment mCurrentFragment;

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

        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        LoginFragment loginFragment=LoginFragment.newInstance("login");
        RegisterFragment registerFragment=RegisterFragment.newInstance("register");
        transaction.add(R.id.container,loginFragment, Constant.LOGIN_TAG)
                .add(R.id.container,registerFragment,Constant.REGISTER_TAG).hide(registerFragment).commit();
    }

    @Override
    protected void initListener() {
        mtb_account.setNavigationOnClickListener(view -> finish());
    }

    public void switchFragment(String fromTag, String toTag) {
        Fragment from = getSupportFragmentManager().findFragmentByTag(fromTag);
        Fragment to = getSupportFragmentManager().findFragmentByTag(toTag);
        if (mCurrentFragment != to) {
            mCurrentFragment = to;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (!to.isAdded()) {//判断是否被添加到了Activity里面去了
                transaction.hide(from).add(R.id.container, to).commit();
            } else {
                transaction.hide(from).show(to).commit();
            }
        }
    }

}
