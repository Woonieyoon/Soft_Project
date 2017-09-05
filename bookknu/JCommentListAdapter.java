package myhome.bookknu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-06-06.
 */

public class JCommentListAdapter extends BaseAdapter {

    private Context context;
    private List<JComment> commentList;

    public JCommentListAdapter(Context context, List<JComment> commentList) {
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

        View v = View.inflate(context,R.layout.jcommentdesign,null); //inflate 객체화 시킴
        TextView co_title= (TextView)v.findViewById(R.id.j_comment_title); //댓글 내용
        TextView co_name= (TextView)v.findViewById(R.id.j_comment_name); //작성자
        TextView co_date= (TextView)v.findViewById(R.id.j_comment_date);//제목

        co_title.setText(commentList.get(i).getJ_content());
        co_name.setText(commentList.get(i).getJ_name());
        co_date.setText(commentList.get(i).getJ_date());

        return v;

    }
}
