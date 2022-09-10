package com.example.tododemo.bean;


public class TodoBean{
    private String username;
    private String title;
    private String content;
    private String classify;
    private String date;

    public TodoBean(String username, String title, String content, String classify, String date) {
        this.username = username;
        this.title = title;
        this.content = content;
        this.classify = classify;
        this.date = date;
    }

    public TodoBean(String username, String title, String classify, String date) {
        this.username = username;
        this.title = title;
        this.classify = classify;
        this.date = date;
    }

    public String getContent() {
        return content;
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

}
