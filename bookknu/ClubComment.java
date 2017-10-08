package myhome.bookknu;

/**
 * Created by sungw on 2017-09-17.
 */

public class ClubComment {

    String club_content; // 댓글 내용
    String club_name; //작성자
    String club_date; //날짜


    public ClubComment(String club_content, String club_name, String club_date)
    {
        this.club_content=club_content;
        this.club_name = club_name;
        this.club_date=club_date;
    }

    public String getClub_content()
    {
        return club_content;
    }

    public String getClub_name()
    {
        return club_name;
    }

    public String getClub_date()
    {
        return club_date;
    }
}
