package com.example.tododemo.Bean;

public class UserBean {
    private String username;
    private String password;
    private String isLogin;

    public UserBean(String username, String password,String isLogin) {
        this.username = username;
        this.password = password;
        this.isLogin = isLogin;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getIsLogin() {
        return isLogin;
    }
}
