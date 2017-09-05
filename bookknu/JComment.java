package myhome.bookknu;

/**
 * Created by sungw on 2017-06-06.
 */
//댓글 data
public class JComment {

    private String j_content; // 댓글 내용
    private String j_name; //작성자
    private String j_date; //날짜


    public JComment(String j_content, String j_name, String j_date)
    {
        this.j_content=j_content;
        this.j_name = j_name;
        this.j_date=j_date;
    }

    public String getJ_content()
    {
        return j_content;
    }

    public String getJ_name()
    {
        return j_name;
    }

    public String getJ_date()
    {
        return j_date;
    }


}
