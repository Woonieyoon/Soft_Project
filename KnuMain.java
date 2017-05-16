package com.example.sungw.knuprojcet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager; //Fragmentmanager 에러안나기 위함
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KnuMain extends AppCompatActivity {


    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knu_main);
        getWebSite();//게시판 가져오기

        final Button courseButton = (Button) findViewById(R.id.courseButton);
        final Button statisticsButton = (Button) findViewById(R.id.statisticsButton);
        final Button scheduleButton = (Button) findViewById(R.id.scheduleButton);
        final LinearLayout notice = (LinearLayout)findViewById(R.id.notice);
        noticeListView = (ListView) findViewById(R.id.noticeListView);

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));//버튼색 어둡게
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager =getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new CourseFragement());
                fragmentTransaction.commit();

            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));//버튼색 어둡게
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager =getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new StatisticsFragment());
                fragmentTransaction.commit();

            }
        });

        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.setVisibility(View.GONE);
                scheduleButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));//버튼색 어둡게
                statisticsButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                courseButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                FragmentManager fragmentManager =getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment,new ScheduleFragment());
                fragmentTransaction.commit();

            }
        });


        noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               String a =  ((Notice)adapter.getItem(position)).getNamogi();
                //Toast.makeText(KnuMain.this,a,Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("http://gp.knu.ac.kr"+a);
               Intent t = new Intent(Intent.ACTION_VIEW,uri);
               startActivity(t);
            }
        });
    }

    private void getWebSite() //국제교류원 게시판 가져오기
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder b_name = new StringBuilder(); //게시판 명
                final StringBuilder b_date = new StringBuilder(); //게시판 날짜
                final StringBuilder b_link = new StringBuilder(); //게시판 링크

                try{
                    Document doc = Jsoup.connect("http://gp.knu.ac.kr/bbs/board.html?code=bbs_notice").get();

                   

                }catch (IOException e)
                {
                    b_name.append("Error: ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        View v = View.inflate(KnuMain.this,R.layout.notice,null);

                        TextView dateText= (TextView)v.findViewById(R.id.dateText);


                        noticeList = new ArrayList<Notice>();

                        StringTokenizer str = new StringTokenizer(b_name.toString(),"\n");
                        StringTokenizer str1 = new StringTokenizer(b_date.toString(),"\n");
                        StringTokenizer str2 = new StringTokenizer(b_link.toString(),"\n");

                        while(str.hasMoreTokens())
                        {
                            String data = str.nextToken();
                            String data1 = str1.nextToken();
                            String data2 = str2.nextToken();
                            noticeList.add(new Notice(data,data1,"  보시려면 'Click' ",data2));
                        }

                        adapter = new NoticeListAdapter(getApplicationContext(),noticeList);
                        noticeListView.setAdapter(adapter);

                    }
                });

            }
        }).start();
    }

}
