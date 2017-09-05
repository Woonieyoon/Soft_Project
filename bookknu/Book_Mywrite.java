package myhome.bookknu;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungw on 2017-08-11.
 */
//자신이 쓴글 보기
public class Book_Mywrite extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private ListView mybookListView;
    private BookListAdapter myadapter;
    private List<Book> mynoticeList;

    String myJSON1;
    private static final String MyTAG_RESULTS = "result";
    private static final String MyTAG_ID = "id";
    private static final String MyTAG_TITLE = "title";
    private static final String MyTAG_CONTENT = "content";
    private static final String MyTAG_DATE = "date";
    private static final String MyTAG_SUB = "sub";
    private static final String MyTAG_COUNT = "count";

    JSONArray mydata = null;

    private int number=0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_mymain);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout1);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView n = (NavigationView)findViewById(R.id.nav_view);
        n.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String id = item.toString();

                if(id.equals("책 게시판"))
                {
                    number = 1;
                }else if(id.equals("취업 게시판"))
                {
                    number = 2;
                }else if(id.equals("동아리 게시판"))
                {
                    number = 3;
                }else if(id.equals("경북대 숲"))
                {
                    number = 4;
                }else if(id.equals("내가쓴글"))
                {
                    number =5;
                }else if(id.equals("메시지 관리"))
                {
                    number =6;
                }else if(id.equals("Log out"))
                {
                    number =7;
                }

                pageChange(number);
                return false;
            }
        });

        mybookListView = (ListView)findViewById(R.id.mybooklist);
        mynoticeList =  new ArrayList<Book>();
        myadapter = new BookListAdapter(getApplicationContext(),mynoticeList);

        Book_Mywrite.myGetDataJSON start = new Book_Mywrite.myGetDataJSON();
        start.execute("http://" + Basicinfo.URL + "/getcontent.php");

        mybookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ndate = ((Book)myadapter.getItem(position)).getDate();  //날짜
                String nname =  ((Book)myadapter.getItem(position)).getName(); //이름
                String ntitle = ((Book)myadapter.getItem(position)).getTitle(); //제목
                String ncontent =  ((Book)myadapter.getItem(position)).getContent(); //내용

                Intent n_click = new Intent(Book_Mywrite.this,Book_MywriteClick.class);
                n_click.putExtra("date",ndate);
                n_click.putExtra("name",nname);
                n_click.putExtra("title",ntitle);
                n_click.putExtra("content",ncontent);
                Book_Mywrite.this.startActivity(n_click);

            }
        });
    }

    //-------------------------------------------------------------------------------------------------------
    //페이지넘기기
    private void pageChange(int a)
    {

        if(a==1)//게시판
        {
            Intent i = new Intent(this,BookMainActivity.class);
            startActivity(i);
        }else if(a==2)//취업
        {
            startActivity(new Intent(this,JobMainActivity.class));

        }else if(a==3)//동아리
        {

        }else if(a==4)//자유
        {
            startActivity(new Intent(this,FreeMainActivity.class));

        }else if(a==5)//내가 쓴글
        {
            startActivity(new Intent(this,Book_Mywrite.class));

        }else if(a==6)//메시지
        {
            startActivity(new Intent(this,MessageActivity.class));
        }else if(a==7)
        {
            startActivity(new Intent(this,LoginActivity.class));
        }

    }









    //데이터게시판 뿌리기 위함
    public void insertBoard()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myJSON1);
            mydata = jsonObj.getJSONArray(MyTAG_RESULTS);

            for(int i=0; i< mydata.length(); i++)
            {
                JSONObject c = mydata.getJSONObject(i);
                String id = c.getString(MyTAG_ID);
                String title = c.getString(MyTAG_TITLE);
                String content = c.getString(MyTAG_CONTENT);
                String date = c.getString(MyTAG_DATE);
                String sub = c.getString(MyTAG_SUB);
                String count = c.getString(MyTAG_COUNT);
                String sumdate;

                if(id.equals(Basicinfo.name)) {
                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    mynoticeList.add(new Book(sumdate,id,title,content,sub,count));
                }

            }

            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            mybookListView.setAdapter(myadapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //게시판 데이터 뿌리기위함
    class myGetDataJSON extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while ((json = bufferedReader.readLine()) != null) {
                    sb.append(json + "\n");
                }

                return sb.toString().trim();
            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(String result) {
            myJSON1 = result;
            //Toast.makeText(BookMainActivity.this,result,Toast.LENGTH_SHORT).show();
            insertBoard();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                final Dialog dig = new Dialog(Book_Mywrite.this);
                dig.setTitle("");
                dig.setContentView(R.layout.back);
                dig.show();

                Button su = (Button)dig.findViewById(R.id.button_success);
                Button ca = (Button)dig.findViewById(R.id.button_cancel);

                su.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveTaskToBack(true);
                        finish();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

                ca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dig.cancel();
                    }
                });

        }

        return super.onKeyDown(keyCode, event);
    }





}
