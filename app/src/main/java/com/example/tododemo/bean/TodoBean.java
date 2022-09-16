package com.example.tododemo.bean;


public class TodoBean{
    private int id;
    private String username;
    private String title;
    private String content;
    private String classify;
    private String date;
    private boolean isDone;



    public TodoBean(int id,String username, String title, String classify, String date,String isDone) {
        this.id = id;
        this.username = username;
        this.title = title;
        this.classify = classify;
        this.date = date;
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

    public int getId() {
        return id;
    }

    public boolean getIsDone() {
        return isDone;
    }
}
