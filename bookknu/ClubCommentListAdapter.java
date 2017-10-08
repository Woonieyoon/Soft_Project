package myhome.bookknu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-09-17.
 */

public class ClubCommentListAdapter extends BaseAdapter {

    private Context context;
    private List<ClubComment> commentList;

    public ClubCommentListAdapter(Context context, List<ClubComment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = View.inflate(context,R.layout.clubcommentdesign,null); //inflate 객체화 시킴
        TextView co_title= (TextView)v.findViewById(R.id.club_comment_title); //댓글 내용
        TextView co_name= (TextView)v.findViewById(R.id.club_comment_name); //작성자
        TextView co_date= (TextView)v.findViewById(R.id.club_comment_date);//제목

        co_title.setText(commentList.get(i).getClub_content());
        co_name.setText(commentList.get(i).getClub_name());
        co_date.setText(commentList.get(i).getClub_date());

        return v;

    }
}
