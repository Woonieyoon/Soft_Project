package myhome.bookknu;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by sungw on 2017-08-01.
 */

public class MyBookListAdapter extends BaseAdapter {

    private Context context;
    private List<Book> bookList;

    public MyBookListAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public int getCount() {
        return bookList.size();
    }

    @Override
    public Object getItem(int i) {
        return bookList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View v = View.inflate(context,R.layout.mywritelist,null); //inflate 객체화 시킴

        TextView date= (TextView)v.findViewById(R.id.mywrite_date); //날짜
        TextView title= (TextView)v.findViewById(R.id.mywrite_title); //제목


        date.setText(bookList.get(i).getDate());
        title.setText(bookList.get(i).getTitle());


        return v;

    }
}
