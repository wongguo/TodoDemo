package com.example.tododemo.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserDatabase extends SQLiteOpenHelper {

    public UserDatabase(@Nullable Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //账号密码表
        String account="create table "+Constant.ACCOUNT_TABLE_NAME+
                "(username text,"+
                "password text,"+
                "isLogin text)";
        sqLiteDatabase.execSQL(account);
        //todo表
        String todoList="create table "+Constant.TODO_TABLE_NAME+
                "(username text,"+
                "title text,"+
                "content text,"+
                "classify text,"+
                "date text)";
        sqLiteDatabase.execSQL(todoList);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
