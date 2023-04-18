package com.example.notespro.Model;

import java.sql.Timestamp;

public class Notes {
    String title;
    String content;

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


    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    String Time;

    public Notes() {
    }

}
