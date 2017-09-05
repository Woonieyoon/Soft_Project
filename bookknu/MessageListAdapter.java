package myhome.bookknu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-08-28.
 */

public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> MessageList;

    public MessageListAdapter(Context context, List<Message> MessageList) {
        this.context = context;
        this.MessageList = MessageList;
    }

    @Override
    public int getCount() {
        return MessageList.size();
    }

    @Override
    public Object getItem(int i) {
        return MessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = View.inflate(context,R.layout.message_send,null); //inflate 객체화 시킴
        TextView date= (TextView)v.findViewById(R.id.send_date); //날짜
        TextView name= (TextView)v.findViewById(R.id.send_name); //작성자
        TextView content = (TextView)v.findViewById(R.id.send_content);

        date.setText(MessageList.get(i).getM_date());
        name.setText(MessageList.get(i).getM_send());
        content.setText(MessageList.get(i).getM_content());
        return v;

    }
}
