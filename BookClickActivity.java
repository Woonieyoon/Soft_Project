package com.example.sungw.knuprojcet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungw on 2017-06-06.
 */

//리스트뷰클릭->보여주기
public class BookClickActivity extends AppCompatActivity{

    private ListView commentListView;
    private CommentListAdapter adapter;
    private List<Comment> noticeList;
    private Button insert;
    private Dialog mMainDialog;
    View layout;
    AlertDialog ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);
        setContentView(R.layout.bookclickactivity);

        commentListView = (ListView)findViewById(R.id.commentlist);
        noticeList =  new ArrayList<Comment>();

        noticeList.add(new Comment("  감사합니다","홍길동","2017-06-05"));
        noticeList.add(new Comment("  감사합니다","홍길동","2017-06-04"));

        adapter = new CommentListAdapter(getApplicationContext(),noticeList);
        commentListView.setAdapter(adapter);

        //dialog만들기




        //댓글쓰기
        insert = (Button)findViewById(R.id.insertcomment);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.comment_dialog,null);
                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(BookClickActivity.this);

                aDlgb.setView(layout);
                ad = aDlgb.create();
                ad.show();

                //저장
                Button save = (Button)layout.findViewById(R.id.commentsave); //저장버튼누를시
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText save = (EditText)layout.findViewById(R.id.commentedit);
                        String str = save.getText().toString();
                        noticeList.add(new Comment("  "+str,"sungwon126","2017-06-21"));
                        ad.dismiss();
                    }
                });

                //취소
                Button cancel = (Button)layout.findViewById(R.id.commentcancel); //저장버튼누를시
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();

                    }
                });

            }
        });


    }
}




//    Button a = (Button)findViewById(R.id.btnclose);
//        a.setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        SlidingDrawer drawer = (SlidingDrawer)findViewById(R.id.slide);
//        drawer.animateClose();
//        }
//        });