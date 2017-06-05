package com.example.sungw.knuprojcet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungw on 2017-06-05.
 */

public class BookMain extends AppCompatActivity {

    private ListView bookListView;
    private BookListAdapter adapter;
    private List<Book> noticeList;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        setContentView(R.layout.book_main);

        button = (Button)findViewById(R.id.toButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BookMain.this,BookInsertActivity.class);
                startActivity(i);
            }
        });


        bookListView = (ListView)findViewById(R.id.booklist);
        noticeList =  new ArrayList<Book>();

        noticeList.add(new Book("2017년 6월 5일","홍길동","전공책 빌려드립니다.","1개","2"));
        noticeList.add(new Book("2017년 6월 5일","홍길동","교양책 빌려드립니다.","3개","4"));
        noticeList.add(new Book("2017년 6월 4일","홍길동","책 빌려드립니다.","2개","1"));
        noticeList.add(new Book("2017년 6월 3일","홍길동","교양책 빌려드립니다.","3개","0"));
        adapter = new BookListAdapter(getApplicationContext(),noticeList);
        bookListView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.book:
                startActivity(new Intent(this,BookMain.class));
                return true;
            case R.id.board:
                startActivity(new Intent(this,boardActivity.class));
                return true;
            case R.id.schedule:
                startActivity(new Intent(this,ScheduleActivity.class));
                return true;
            case R.id.meal:
                startActivity(new Intent(this,MealActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }

}
