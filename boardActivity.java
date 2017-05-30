package com.example.sungw.knuprojcet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by sungw on 2017-05-28.
 */

public class boardActivity extends AppCompatActivity {

    private ListView noticeListView;
    private NoticeListAdapter adapter;
    private List<Notice> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);

        setContentView(R.layout.boardlist);
        noticeListView = (ListView)findViewById(R.id.noticeListView);
        getWebSite();

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
            case R.id.memo:
                startActivity(new Intent(this,MemoActivity.class));
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

                    String title=doc.title();

                    Elements table = doc.select("table[class=basic_board mg10]");

                    for(Element row : table.select("tr:gt(0)"))
                    {
                        // Identify all the table cell's(td)
                        Elements tds = row.select("td");
                        Elements h = row.select("a[href]");


                        b_name.append(tds.get(1).text()).append("\n");
                        b_date.append(tds.get(3).text()).append("\n");
                        b_link.append(h.attr("href")).append("\n");

                    }

                }catch (IOException e)
                {
                    b_name.append("Error: ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


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
