package com.example.tododemo.sqlite;

public class Constant {
    public static final String DATABASE_NAME = "userDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String ACCOUNT_TABLE_NAME = "userTable";
    public static final String TODO_TABLE_NAME = "todoTable";

    public static boolean isLogin = false;
    public static String username = "公共账号";

    public static int EDIT_DIALOG = 1;
    public static int MESSAGE_DIALOG = 0;

    public static int TYPE_DAY = 0;
    public static int TYPE_TODO = 1;


    // 0在数据库表示false 1表示true
    public static boolean TODO_STATE = false;

    public static String CLASSIFY = "全部";

    public static String LOGIN_TAG="login";
    public static String REGISTER_TAG="register";
}
