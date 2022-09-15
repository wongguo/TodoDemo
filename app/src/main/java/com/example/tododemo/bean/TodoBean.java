package com.example.tododemo.bean;


public class TodoBean{
    private String username;
    private String title;
    private String content;
    private String classify;
    private String date;
    // 增加时间
    private String id;
    private boolean isDone;



    public TodoBean(String username, String title, String classify, String date,String id,String isDone) {
        this.username = username;
        this.title = title;
        this.classify = classify;
        this.date = date;
        this.id = id;
        this.isDone = isDone.equals("true");
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

    public String getId() {
        return id;
    }

    public boolean getIsDone() {
        return isDone;
    }
}
