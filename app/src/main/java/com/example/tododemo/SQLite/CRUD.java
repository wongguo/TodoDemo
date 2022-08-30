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

public class CRUD {
    private SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private String name;

    public CRUD(String name) {
        dbHelper = new UserDatabase(MyApplication.getContext());
        db = dbHelper.getWritableDatabase();
        this.name = name;
    }
    public void Create(ContentValues values){
        db.insert(name,null,values);
    }

    /**
     * 查询User表下所有数据，返回用户集合
     * @return
     */
    @SuppressLint("Range")
    public List<UserBean> RetrieveUser(){
        List<UserBean> userBeans = new ArrayList<>();
        Cursor cursor = db.query(name,null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                userBeans.add(new UserBean(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password"))));
            }while (cursor.moveToNext());
        }
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
        return todoBeans;
    }

    /**
     * 更新用户表信息， 根据唯一用户名为据
     * @param values
     * @param userName
     */
    public void UpdateUser(ContentValues values,String userName){
        // 须先判断该用户名是否有相同
        int judge = 0;
        Object userName1 = values.get("userName");
        for (UserBean userBean : RetrieveUser()) {
            if (userBean.getUsername().equals(userName1)){
                judge = 1;
            }
        }
        if (judge == 0){
            db.update(name,values,"userName = ?",new String[]{userName});
        }else {
            Toast.makeText(MyApplication.getContext(),"用户名已经被占用",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据日期修改Todo
     * @param values
     */
    public void UpdateTodo(ContentValues values,String time){
        db.update(name,values,"time = ?",new String[]{time});
    }

    /**
     * 注销账号
     */
    public void DeleteUser(String userName){
        db.delete(name,"userNmae = ?", new String[]{userName});
    }

    // 必须是本人才能删除
    public void DeleteTodo(String time){
        // 此处需对当前账户和TOdo账户进行一个遍历对等操作
        db.delete(name,"time = ?",new String[]{time});
    }
}
