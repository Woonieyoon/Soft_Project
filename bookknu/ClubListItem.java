package myhome.bookknu;

/**
 * Created by sungw on 2017-09-17.
 */

public class ClubListItem {

    private String c_title;
    private String c_content;
    private String c_id;
    private String c_date;

    public ClubListItem(String c_title,String c_content,String c_id,String c_date)
    {
        this.c_title = c_title;
        this.c_content = c_content;
        this.c_id = c_id;
        this.c_date = c_date;
    }

    public String getC_title()
    {
        return c_title;
    }
    public String getC_content()
    {
        return c_content;
    }
    public String getC_id()
    {
        return c_id;
    }
    public String getC_date()
    {
        return c_date;
    }
}
