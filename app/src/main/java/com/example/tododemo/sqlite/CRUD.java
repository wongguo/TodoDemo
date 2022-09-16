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

// 对数据库的操作
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

    //账号处理

    /**
     * 登录注册时判断账号是否存在,根据结果返回true or false
     */
    @SuppressLint("Range")
    public boolean isExist(String userName,String password){
        Cursor cursor = db.query(name,new String[]{"username","password"},"username=? and password=?",new String[]{userName,password},
                null,null,null);
        if (cursor.moveToNext()){
            // 检测到账号存在
            cursor.close();
            return true;
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
        Cursor cursor = db.query(name,new String[]{"username"},"username=?",new String[]{userName},null,null,null);
        if (cursor.moveToNext()){
            // 检测到用户名相同存在
            cursor.close();
            return true;
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
        while (cursor.moveToNext()){
            // 检测到有登录账号
            if (cursor.getString(cursor.getColumnIndex("isLogin")).equals("true")){
                userBean = new UserBean(cursor.getString(cursor.getColumnIndex("username")),
                        cursor.getString(cursor.getColumnIndex("password")),
                        cursor.getString(cursor.getColumnIndex("isLogin")));
                cursor.close();
                return userBean;
            }
        }
        cursor.close();
        // 不存在登录账号
        return null;
    }

    /**
     * 根据用户名更新用户表信息
     */
    public void UpdateUser(ContentValues values,String userName){
        db.update(name,values,"userName = ?",new String[]{userName});
    }

    /**
     * 注销账号
     */
    public void DeleteUser(String userName){
        db.delete(name,"userName = ?", new String[]{userName});
    }

    //Todo处理

    /**
     * 返回账号所属所有Todo集合,根据完成和未完成的todo返回
     */
    @SuppressLint("Range")
    public List<TodoBean> RetrieveTodo(String username,boolean isDone,String classify){
        if(classify.equals("全部")) return RetrieveAllTodo(username,isDone);
        List<TodoBean> todoBeans = new ArrayList<>();
        // 按用户的名字返回相关todo
        Cursor cursor = db.query(name,null,"username=? and isDone=? and classify=?",new String[]{username,String.valueOf(isDone),classify},
                null,null,"date DESC");
        while (cursor.moveToNext()){
            todoBeans.add(new TodoBean(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("classify")),
                    DateUtils.longToDate(cursor.getLong(cursor.getColumnIndex("date"))),
                    cursor.getString(cursor.getColumnIndex("isDone"))));
        }
        cursor.close();
        return todoBeans;
    }

    @SuppressLint("Range")
    public List<TodoBean> RetrieveAllTodo(String username,boolean isDone){
        List<TodoBean> todoBeans = new ArrayList<>();
        // 按用户的名字返回相关todo
        Cursor cursor = db.query(name,null,"username=? and isDone=? ",new String[]{username,String.valueOf(isDone)},
                null,null,"date DESC");
        while (cursor.moveToNext()){
            todoBeans.add(new TodoBean(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("classify")),
                    DateUtils.longToDate(cursor.getLong(cursor.getColumnIndex("date"))),
                    cursor.getString(cursor.getColumnIndex("isDone"))));
        }
        cursor.close();
        return todoBeans;
    }

    /**
     * 获取todo分类
     */
    @SuppressLint("Range")
    public List<String> RetrieveTodoClassify(String username){
        List<String> classifyList = new ArrayList<>();
        classifyList.add("全部");
        // 按用户的名字返回相关todo
        Cursor cursor = db.query(name,null,"username=? ",new String[]{username},
                null,null,null);
        while (cursor.moveToNext()){
            String classify = cursor.getString(cursor.getColumnIndex("classify"));
            if (!classifyList.contains(classify))classifyList.add(classify);
        }
        cursor.close();
        return classifyList;
    }


    /**
     * 必须本人才能更新相关Todo
     * 根据日期修改Todo
     */
    public void UpdateTodo(ContentValues values,int id){
        db.update(name,values,"id = ?",new String[]{String.valueOf(id)});
    }

    // 删除单条todo
    public void DeleteTodo(int id){
        // 此处需对当前账户和Todo账户进行一个遍历对等操作
        db.delete(name,"id = ?",new String[]{String.valueOf(id)});
    }

    //删除多条todo
    public void DeleteTodos(List<String> idsList){
        for(String id:idsList){
            db.delete(name,"id=?",new String[]{id});
        }
        //db.delete(name, String.format("id in (%s)", ListToStrings(idsList)), idsList.toArray(new String[0]));
    }
    public void DeleteTodosByUser(String userName){
        db.delete(name,"username = ?",new String[]{userName});

    }
    private String ListToStrings(List<String> list){
        StringBuilder sb=new StringBuilder();
        for(String id:list){
            sb.append(id).append(",");
        }
        return sb.deleteCharAt(sb.length()-1).toString();
    }


    /**
     * @param search 模糊搜索内容
     * @return 搜索相关的todoList
     */
    @SuppressLint("Range")
    public List<TodoBean> SearchTodo(String search,String userNmae){
        List<TodoBean> todoBeans = new ArrayList<>();
        // 模糊搜索
        Cursor cursor = db.query(name,null,"title LIKE ?and username=?",new String[]{"%"+search+"%",userNmae},
                null,null,null);
        while (cursor.moveToNext()){
            todoBeans.add(new TodoBean(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("username")),
                    cursor.getString(cursor.getColumnIndex("title")),
                    cursor.getString(cursor.getColumnIndex("classify")),
                    DateUtils.longToDate(cursor.getLong(cursor.getColumnIndex("date"))),
                    cursor.getString(cursor.getColumnIndex("isDone"))));
        }
        cursor.close();
        return todoBeans;
    }
}
