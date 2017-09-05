package myhome.bookknu;

/**
 * Created by sungw on 2017-06-06.
 */
//댓글 data
public class Comment {

    String co_content; // 댓글 내용
    String co_name; //작성자
    String co_date; //날짜


    public Comment(String co_content, String co_name, String co_date)
    {
        this.co_content=co_content;
        this.co_name = co_name;
        this.co_date=co_date;
    }

    public String getCo_content()
    {
        return co_content;
    }

    public String getCo_name()
    {
        return co_name;
    }

    public String getCo_date()
    {
        return co_date;
    }


}
