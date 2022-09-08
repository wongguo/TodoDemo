package com.example.tododemo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TodoBean implements MultiItemEntity {
    private String username;
    private String title;
    private String classify;
    private String date;

    public TodoBean(String username, String title,String classify, String date) {
        this.username = username;
        this.title = title;
        this.classify = classify;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }


    public String getClassify() {
        return classify;
    }

    public String getDate() {
        return date;
    }

    @Override
    public int getItemType() {
        return 0;
    }
}
