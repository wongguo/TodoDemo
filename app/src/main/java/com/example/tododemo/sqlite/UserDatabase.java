package com.example.tododemo.sqlite;

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
        //账号密码-todo表
        String account="create table "+Constant.ACCOUNT_TABLE_NAME+
                "(username text,"+
                "password text,"+
                "isLogin text)";
        sqLiteDatabase.execSQL(account);
        //day-todo表
        String todoList="create table "+Constant.TODO_TABLE_NAME+
                "(username text,"+
                "title text,"+
                "content text,"+
                "classify text,"+
                "date text,"+
                "isDone text)";
        sqLiteDatabase.execSQL(todoList);
        //todo表
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
