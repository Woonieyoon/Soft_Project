package myhome.bookknu;

/**
 * Created by sungw on 2017-08-01.
 */
public class Book {

    String date; //날짜
    String name; //작성자
    String title; //제목
    String content;//내용
    String sub;   //댓글
    String count; //조회수

    public Book(String date,String name,String title,String content,String sub,String count)
    {
        this.date = date;
        this.name = name;
        this.title = title;
        this.content = content;
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

    public String getContent() { return  content;}

    public String getSub()
    {
        return sub;
    }

    public String getCount()
    {
        return count;
    }



}
