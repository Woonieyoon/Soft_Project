package myhome.bookknu;

import android.app.Dialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.Date;
import java.util.List;

/**
 * Created by sungw on 2017-08-01.
 */

public class BookMainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private ListView bookListView;
    private BookListAdapter adapter;
    private List<Book> noticeList;
    Button button;
    EditText search;
    private String myJSON;
    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_DATE = "date";
    private static final String TAG_SUB = "sub";
    private static final String TAG_COUNT = "count";

    JSONArray peoples = null;

    private int number=0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
       // getSupportActionBar().setCustomView(R.layout.custom_bar);


        setContentView(R.layout.book_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
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



        //-------------------------------------------------------------------------------------------
        //listview에 값뿌려주기위함
        BookMainActivity.GetDataJSON b = new BookMainActivity.GetDataJSON();
        b.execute("http://" + Basicinfo.URL + "/getcontent.php");

        //-------------------------------------------------------------------------------------------
        bookListView = (ListView)findViewById(R.id.booklist);
        button = (Button)findViewById(R.id.toButton);
        search = (EditText)findViewById(R.id.search_edit);

        noticeList =  new ArrayList<Book>();
        //noticeList.add(new Book("0","0","0","0","1개","2"));
        adapter = new BookListAdapter(getApplicationContext(),noticeList); //원래

        //editText
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String ch = s.toString();// edittext 문자열

                int book_num = adapter.getCount();//글전체 개수 파악
                for(int i=0; i<book_num ;i++)
                {
                   noticeList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
                }

                bookListView.clearChoices();
                adapter.notifyDataSetChanged();

                search_board(ch);

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


        //게시글 삽입위함
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent i = new Intent(BookMainActivity.this,BookInsertActivity.class);
                startActivity(i);
            }
        });


        //-------------------------------------------------------------------------------------------
        //리스트뷰 선택했을때
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String ndate = ((Book)adapter.getItem(position)).getDate();  //날짜
                String nname =  ((Book)adapter.getItem(position)).getName(); //이름
                String ntitle = ((Book)adapter.getItem(position)).getTitle(); //제목
                String ncontent =  ((Book)adapter.getItem(position)).getContent(); //내용

                Intent n_click = new Intent(BookMainActivity.this,BookClickActivity.class);
                n_click.putExtra("date",ndate);
                n_click.putExtra("name",nname);
                n_click.putExtra("title",ntitle);
                n_click.putExtra("content",ncontent);

                startActivity(n_click);

                //Toast.makeText(BookMainActivity.this,a, Toast.LENGTH_SHORT).show();
                //Uri uri = Uri.parse("http://gp.knu.ac.kr"+a);
                //Intent t = new Intent(BookMainActivity.this,BookClickActivity.class);
                //startActivity(t);
            }
        });

    }

    //page넘기기 위함
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




    //검색할때 호출됨
    public void search_board(String edit)
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String date = c.getString(TAG_DATE);
                String sub = c.getString(TAG_SUB);
                String count = c.getString(TAG_COUNT);
                String sumdate;

                if(title.contains(edit)) {
                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new Book(sumdate,id,title,content,sub,count));
                }

            }

            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            bookListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //데이터게시판 뿌리기 위함
    public void insertBoard()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String date = c.getString(TAG_DATE);
                String sub = c.getString(TAG_SUB);
                String count = c.getString(TAG_COUNT);
                String sumdate;

                if(!date.equals("")) {
                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new Book(sumdate,id,title,content,sub,count));
                }

            }

            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            bookListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

       //게시판 데이터 뿌리기위함
       class GetDataJSON extends AsyncTask<String,Void,String> {

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
                myJSON = result;
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

               final Dialog dig = new Dialog(BookMainActivity.this);
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
