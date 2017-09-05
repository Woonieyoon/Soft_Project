package myhome.bookknu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-08-01.
 */

public class BookListAdapter extends BaseAdapter {

    private Context context;
    private List<Book> bookList;

    public BookListAdapter(Context context, List<Book> bookList) {
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

        View v = View.inflate(context,R.layout.board_sublist,null); //inflate 객체화 시킴
        TextView bookdate= (TextView)v.findViewById(R.id.bookdate); //날짜
        TextView Topname= (TextView)v.findViewById(R.id.Topname); //작성자
        TextView booktitle= (TextView)v.findViewById(R.id.booktitle);//제목
        TextView booksub= (TextView)v.findViewById(R.id.booksub);//댓글
        TextView bookcount= (TextView)v.findViewById(R.id.bookcount);//조회수

        bookdate.setText(bookList.get(i).getDate());
        Topname.setText(bookList.get(i).getName());
        booktitle.setText(bookList.get(i).getTitle());
        booksub.setText(bookList.get(i).getSub());
        bookcount.setText(bookList.get(i).getCount());

        return v;

    }
}
