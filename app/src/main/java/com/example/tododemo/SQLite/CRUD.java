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
     * 登录时检测账号是否存在,根据结果返回true or false
     */
    @SuppressLint("Range")
    public boolean isExist(String userName,String password){
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                // 检测到账号存在
                if (cursor.getString(cursor.getColumnIndex("username")).equals(userName)&&cursor
                        .getString(cursor.getColumnIndex("password")).equals(password)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
        // 账号不存在
        return false;
    }
    /**
     * 修改用户名的时候进行查找是否存在相同用户名
     * 为类私有方法
     */
    @SuppressLint("Range")
    private boolean isExistSame(String userName){
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                // 检测到用户名相同存在
                if (cursor.getString(cursor.getColumnIndex("username")).equals(userName)){
                    return true;
                }
            }while (cursor.moveToNext());
        }
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
                    return userBean;
                }
            }while (cursor.moveToNext());
        }
        // 不存在登录账号
        return null;
    }

    /**
     * 查找所有笔记，返回笔记集合
     */
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
        String username = (String) values.get("username");
        // 无相同用户名
        if ((isExistSame(username))){
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
