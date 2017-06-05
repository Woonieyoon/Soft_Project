package com.example.sungw.knuprojcet;

/**
 * Created by sungw on 2017-06-05.
 */

public class Book {

    String date; //날짜
    String name; //작성자
    String title; //제목
    String sub;   //댓글
    String count; //조회수

    public Book(String date,String name,String title,String sub,String count)
    {
        this.date = date;
        this.name = name;
        this.title = title;
        this.sub = sub;
        this.count = count;
    }

    public String getDate()
    {
        return date;
    }

    public String getName()
    {
        return name;
    }

    public String getTitle()
    {
        return title;
    }

    public String getSub()
    {
        return sub;
    }

    public String getCount()
    {
        return count;
    }



}
