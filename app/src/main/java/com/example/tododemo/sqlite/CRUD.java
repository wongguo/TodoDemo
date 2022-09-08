package com.example.tododemo.sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tododemo.bean.TodoBean;
import com.example.tododemo.bean.UserBean;

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
     * 登录注册时判断账号是否存在,根据结果返回true or false
     */
    @SuppressLint("Range")
    public boolean isExist(String userName,String password){
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                // 检测到账号存在
                if (cursor.getString(cursor.getColumnIndex("username")).equals(userName)&&cursor
                        .getString(cursor.getColumnIndex("password")).equals(password)){
                    cursor.close();
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        // 账号不存在
        return false;
    }
    /**
     * 修改用户名的时候进行查找是否存在相同用户名
     */
    @SuppressLint("Range")
    public boolean isExistSame(String userName){
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                // 检测到用户名相同存在
                if (cursor.getString(cursor.getColumnIndex("username")).equals(userName)){
                    cursor.close();
                    return true;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        // 不存在
        return false;
    }
    /**
     * 检测是否为登录状态  根据结果返回true or false
     */
    @SuppressLint("Range")
    public UserBean isLogin(){
        UserBean userBean;
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                // 检测到有登录账号
                if (cursor.getString(cursor.getColumnIndex("isLogin")).equals("true")){
                    userBean = new UserBean(cursor.getString(cursor.getColumnIndex("username")),
                            cursor.getString(cursor.getColumnIndex("password")),
                            cursor.getString(cursor.getColumnIndex("isLogin")));
                    cursor.close();
                    return userBean;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        // 不存在登录账号
        return null;
    }

    /**
     * 查找所有笔记，返回笔记集合
     */
    @SuppressLint("Range")
    public List<TodoBean> RetrieveTodo(String name){
        List<TodoBean> todoBeans = new ArrayList<>();
        Cursor cursor = db.query(Constant.TODO_TABLE_NAME,null,"username=?",new String[]{name},null,null,null);
        while (cursor.moveToNext()){
            todoBeans.add(new TodoBean(cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("classify")),
                    cursor.getString(cursor.getColumnIndex("date"))));
        }
        cursor.close();
        return todoBeans;
    }

    /**
     * 根据用户名更新用户表信息
     */
    public void UpdateUser(ContentValues values,String userName){
        db.update(name,values,"userName = ?",new String[]{userName});
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
