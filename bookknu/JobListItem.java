package myhome.bookknu;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by sungw on 2017-08-24.
 */

//취업 아이템
public class JobListItem{

    private String j_title;
    private String j_content;
    private String j_id;
    private String j_date;

    public JobListItem(String j_title,String j_content,String j_id,String j_date)
    {
        this.j_title = j_title;
        this.j_content = j_content;
        this.j_id = j_id;
        this.j_date = j_date;
    }

    public String getJ_title()
    {
        return j_title;
    }
    public String getJ_content()
    {
        return j_content;
    }
    public String getJ_id()
    {
        return j_id;
    }
    public String getJ_date()
    {
        return j_date;
    }
}
