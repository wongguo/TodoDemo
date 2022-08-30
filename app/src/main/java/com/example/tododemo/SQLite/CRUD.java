package com.example.tododemo.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.tododemo.Bean.TodoBean;
import com.example.tododemo.Bean.UserBean;
import com.example.tododemo.MyApplication;

import java.util.ArrayList;
import java.util.List;

// 对两表的操作
public class CRUD {
    private final SQLiteDatabase db;
    private final String name;


    // 传入参数为表名
    public CRUD(Context context,String name) {
        SQLiteOpenHelper dbHelper = new UserDatabase(context);
        db = dbHelper.getWritableDatabase();
        // 将表名赋值给全局变量
        this.name = name;
    }

    /**
     * 往表里添加数据
     */
    public void add(ContentValues values){
        db.insert(name,null,values);

    }

    /**
     * 查询User表下所有数据，返回用户集合
     */
    @SuppressLint("Range")
    public List<UserBean> RetrieveUser(){
        List<UserBean> userBeans = new ArrayList<>();
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                userBeans.add(new UserBean(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("isLogin"))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return userBeans;
    }
    @SuppressLint("Range")
    public List<TodoBean> RetrieveTodo(){
        List<TodoBean> todoBeans = new ArrayList<>();
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                todoBeans.add(new TodoBean(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("title")),
                        cursor.getString(cursor.getColumnIndex("content")),
                        cursor.getString(cursor.getColumnIndex("classify")),
                        cursor.getString(cursor.getColumnIndex("date"))));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return todoBeans;
    }

    /**
     * 更新用户表信息， 根据唯一用户名为据
     */
    public void UpdateUser(ContentValues values,String userName){
        // 须先判断该用户名是否有相同
        int judge = 0;
        Object userName1 = values.get("userName");
        for (UserBean userBean : RetrieveUser()) {
            if (userBean.getUsername().equals(userName1)) {
                judge = 1;
                break;
            }
        }
        if (judge == 0){
            db.update(name,values,"userName = ?",new String[]{userName});
        }else {
            Toast.makeText(MyApplication.getContext(),"用户名已经被占用",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 必须本人才能更新相关Todo
     * 根据日期修改Todo
     */
    public void UpdateTodo(ContentValues values,String time){
        db.update(name,values,"time = ?",new String[]{time});
    }

    /**
     * 注销账号
     */
    public void DeleteUser(String userName){
        db.delete(name,"userName = ?", new String[]{userName});
    }

    // 必须是本人才能删除
    public void DeleteTodo(String time){
        // 此处需对当前账户和TOdo账户进行一个遍历对等操作
        db.delete(name,"time = ?",new String[]{time});
    }
}
