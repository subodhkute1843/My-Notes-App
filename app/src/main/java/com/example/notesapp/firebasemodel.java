package com.example.notesapp;

public class firebasemodel {


    //names match with createnote.java
    private String title ;
    private String content;


    public firebasemodel(){

    }

    public firebasemodel(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
