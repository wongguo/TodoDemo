package com.example.tododemo.Bean;

public class TodoBean {
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

    public String getUsername() {
        return username;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getClassify() {
        return classify;
    }

    public String getDate() {
        return date;
    }
}
