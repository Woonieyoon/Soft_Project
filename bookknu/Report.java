package myhome.bookknu;

/**
 * Created by sungw on 2017-10-12.
 */

public class Report {


    int  aType;
    String sname; //신고인
    String wname; //글쓴이
    String title; //제목
    String content;//내용
    String date; //날짜


    public Report(int aType,String sname,String wname,String title,String content,String date)
    {
        this.aType = aType;
        this.sname = sname;
        this.wname = wname;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public int getaType()  {return aType;}

    public String getSname()
    {
        return sname;
    }

    public String getWname() { return wname;}

    public String getTitle()
    {
        return title;
    }

    public String getContent() { return  content;}

    public String getDate()
    {
        return date;
    }

}
