package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
    Button book_search;

    ArrayAdapter spadapter;
    Spinner sp;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_TITLE = "title";
    private static final String TAG_CONTENT = "content";
    private static final String TAG_DATE = "date";
    private static final String TAG_SUB = "sub";
    private static final String TAG_COUNT = "count";

    //오래된것,최신
    private String myJSON;
    JSONArray peoples = null;

    //댓글순
    private String subJSON;
    JSONArray sub_count = null;

    //조회순
    private String countJSON;
    JSONArray count_count = null;

    private int number=0;

    private int sp_number = 0;

    private View layout;
    private AlertDialog ad;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                }else if(id.equals("관리"))
                {
                    number =8;
                }

                pageChange(number);
                return false;
            }
        });


        //----------------------------------------------------------------------------------------
        bookListView = (ListView)findViewById(R.id.booklist);
        button = (Button)findViewById(R.id.toButton);
        search = (EditText)findViewById(R.id.search_edit);
        book_search = (Button)findViewById(R.id.book_search);

        noticeList =  new ArrayList<Book>();
        adapter = new BookListAdapter(getApplicationContext(),noticeList); //원래

        sp = (Spinner)findViewById(R.id.Spinner);
        spadapter = ArrayAdapter.createFromResource(BookMainActivity.this,R.array.sorting, android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(spadapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //0:오래된순,1:최신순,2:댓글순,3:조회순
                sp_number = position;
                spinner_board(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
              }
        });

        //검색 눌렀을경우
        book_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(BookMainActivity.this,sp.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
                String getText = search.getText().toString();

                int book_num = adapter.getCount();//글전체 개수 파악
                for(int i=0; i<book_num ;i++)
                {
                   noticeList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
                }

                bookListView.clearChoices();
                adapter.notifyDataSetChanged();
                search_board(getText);

            }
        });


        //게시글 삽입위함
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Basicinfo.po_writing.equals("off"))
                {
                    Toast.makeText(BookMainActivity.this,"글쓰기 제한",Toast.LENGTH_SHORT).show();
                }else
                {
                    Intent i = new Intent(BookMainActivity.this,BookInsertActivity.class);
                    startActivity(i);
                }

            }
        });


        //-------------------------------------------------------------------------------------------
        //리스트뷰 선택했을때
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String ndate = ((Book)adapter.getItem(position)).getDate();  //날짜
                final String nname =  ((Book)adapter.getItem(position)).getName(); //이름
                final String ntitle = ((Book)adapter.getItem(position)).getTitle(); //제목
                final String ncontent =  ((Book)adapter.getItem(position)).getContent(); //내용

                CharSequence info[] = new CharSequence[] {"Message 보내기", "글읽기" , "신고하기" };

                AlertDialog.Builder builder = new AlertDialog.Builder(BookMainActivity.this);

                builder.setTitle("선택하세요!!");
                builder.setIcon(R.drawable.knu);

                builder.setItems(info, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which)
                        {
                            case 0: //Message 보내기

                                Context ctx = BookMainActivity.this;
                                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                layout = inflater.inflate(R.layout.message_dialog,null);
                                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(ctx);
                                aDlgb.setView(layout);
                                ad = aDlgb.create();
                                ad.show();

                                //저장
                                Button save = (Button)layout.findViewById(R.id.message_save); //저장버튼누를시
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText save = (EditText)layout.findViewById(R.id.message_edit);

                                        String send = Basicinfo.name;
                                        String receive = nname;
                                        String message = save.getText().toString();

                                        BookMainActivity.MessageTask b = new BookMainActivity.MessageTask();
                                        b.execute(send,receive,message);
                                        ad.dismiss();

                                    }
                                });


                                //취소
                                Button cancel = (Button)layout.findViewById(R.id.message_cancel); //저장버튼누를시
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ad.dismiss();
                                    }
                                });

                                break;

                            case 1: //글읽기
                                Intent n_click = new Intent(BookMainActivity.this,BookClickActivity.class);
                                n_click.putExtra("date",ndate);
                                n_click.putExtra("name",nname);
                                n_click.putExtra("title",ntitle);
                                n_click.putExtra("content",ncontent);
                                startActivity(n_click);
                                break;

                            case 2: //신고하기
                                BookMainActivity.ReportTask r = new BookMainActivity.ReportTask(BookMainActivity.this);
                                r.execute(Basicinfo.name,nname,ntitle,ncontent,ndate,"off"); //off는 읽지 않았다는 말
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
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
            startActivity(new Intent(this,ClubMainActivity.class));
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
        }else if(a==8)
        {
            startActivity(new Intent(this,SettingActivity.class));
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

                if(SoundSearcher.matchString(title,edit)) {
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

    //일반스피너 선택시 호출됨
    public void spinner_board(int n)
    {
        //test---------------------------------------------------------------------------------------
        int book_num = adapter.getCount();//글전체 개수 파악
        for(int i=0; i<book_num ;i++)
        {
            noticeList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
        }

        try{
            if(n==0) //오래된순
            {
                BookMainActivity.GetDataJSON b = new BookMainActivity.GetDataJSON();
                b.execute("http://" + Basicinfo.URL + "/getcontent.php");
            }
            else if(n==1) //최신순
            {
                JSONObject jsonObj = new JSONObject(myJSON);
                peoples = jsonObj.getJSONArray(TAG_RESULTS);

                for(int i=peoples.length()-1; i>=0; i--) {
                    JSONObject c = peoples.getJSONObject(i);
                    String id = c.getString(TAG_ID);
                    String title = c.getString(TAG_TITLE);
                    String content = c.getString(TAG_CONTENT);
                    String date = c.getString(TAG_DATE);
                    String sub = c.getString(TAG_SUB);
                    String count = c.getString(TAG_COUNT);
                    String sumdate;

                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year + "년 " + month + "월 " + day + "일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new Book(sumdate, id, title, content, sub, count));

                }
                bookListView.setAdapter(adapter);
            }else if(n==2)//댓글순
            {
                BookMainActivity.GetSubDataJSON b = new BookMainActivity.GetSubDataJSON();
                b.execute("http://" + Basicinfo.URL + "/getsub.php");

            }else if(n==3)//조회순
            {
                BookMainActivity.GetCountDataJSON b = new BookMainActivity.GetCountDataJSON();
                b.execute("http://" + Basicinfo.URL + "/getsortcount.php");
            }


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //데이터게시판 뿌리기 위함(최신,오래)
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

       //게시판 데이터 뿌리기위함(최신,오랜된)
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
                insertBoard();
            }
        }


    //데이터게시판 뿌리기 위함(댓글)
    public void insertSubBoard()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(subJSON);
            sub_count = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i< sub_count.length(); i++)
            {
                JSONObject c = sub_count.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String date = c.getString(TAG_DATE);
                String sub = c.getString(TAG_SUB);
                String count = c.getString(TAG_COUNT);
                String sumdate;

                int ch = Integer.parseInt(date);
                int year = ch / 10000;
                int month = (ch % 10000) / 100;
                int day = (ch % 10000) % 100;
                sumdate = year+"년 "+month+"월 "+day+"일";
                noticeList.add(new Book(sumdate,id,title,content,sub,count));

            }
            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            bookListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    //게시판 데이터 뿌리기위함(댓글)
    class GetSubDataJSON extends AsyncTask<String,Void,String> {

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
            subJSON = result;
            insertSubBoard();
        }
    }


    //데이터게시판 뿌리기 위함(조회수)
    public void insertCountBoard()
    {
        //test---------------------------------------------------------------------------------------
        try{
            JSONObject jsonObj = new JSONObject(countJSON);
            count_count = jsonObj.getJSONArray(TAG_RESULTS);

            for(int i=0; i< count_count.length(); i++)
            {
                JSONObject c = count_count.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String title = c.getString(TAG_TITLE);
                String content = c.getString(TAG_CONTENT);
                String date = c.getString(TAG_DATE);
                String sub = c.getString(TAG_SUB);
                String count = c.getString(TAG_COUNT);
                String sumdate;

                int ch = Integer.parseInt(date);
                int year = ch / 10000;
                int month = (ch % 10000) / 100;
                int day = (ch % 10000) % 100;
                sumdate = year+"년 "+month+"월 "+day+"일";

                noticeList.add(new Book(sumdate,id,title,content,sub,count));
            }

            bookListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    //게시판 데이터 뿌리기위함(조회수)
    class GetCountDataJSON extends AsyncTask<String,Void,String> {

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
            countJSON = result;
            insertCountBoard();
        }
    }


    //message 보내기
    public class MessageTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {


            if(result.equals("1"))
            {
                Toast.makeText(BookMainActivity.this,"성공",Toast.LENGTH_SHORT).show();
            }else
            {

            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            long now = System.currentTimeMillis();
            Date d = new Date(now);
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            String date = s.format(d);

            String login_url = "http://" + Basicinfo.URL + "/insertmessage.php";

            try
            {
                String send = params[0];
                String receive = params[1];
                String message = params[2];

                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("send","UTF-8") + "=" + URLEncoder.encode(send,"UTF-8") + "&"
                        + URLEncoder.encode("receive","UTF-8") + "=" + URLEncoder.encode(receive,"UTF-8")  + "&"
                        + URLEncoder.encode("message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8")  + "&"
                        + URLEncoder.encode("date","UTF-8") + "=" + URLEncoder.encode(date,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result="";
                String line="";

                //line = bufferedReader.readLine();
                //result+=line;

                while( (line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }


            return null;
        }
    }

    ////--------------------------------------------------------------------------------------------------------------
    //댓글 삽입
    class CommentTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        CommentTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {


            if(result.equals("1"))
            {
                //Toast.makeText(BookClickActivity.this,"성공",Toast.LENGTH_SHORT).show();
            }else
            {
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            long now = System.currentTimeMillis();
            Date d = new Date(now);
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            String date = s.format(d);

            String login_url = "http://" + Basicinfo.URL + "/insertcomment.php";

            try
            {
                String title = params[0];
                String content = params[1];
                String id = params[2];
                String wid = params[3];
                String com = params[4];
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8") + "&"
                        + URLEncoder.encode("content","UTF-8") + "=" + URLEncoder.encode(content,"UTF-8")  + "&"
                        + URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8")  + "&"
                        + URLEncoder.encode("wid","UTF-8") + "=" + URLEncoder.encode(wid,"UTF-8")  + "&"
                        + URLEncoder.encode("com","UTF-8") + "=" + URLEncoder.encode(com,"UTF-8")  + "&"
                        + URLEncoder.encode("date","UTF-8") + "=" + URLEncoder.encode(date,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result="";
                String line="";

                //line = bufferedReader.readLine();
                //result+=line;

                while( (line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }


            return null;
        }
    }

    ////--------------------------------------------------------------------------------------------------------------
    //신고하기
    class ReportTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        ReportTask(Context ctx)
        {
            context=ctx;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {


            if(result.equals("1"))
            {
                //Toast.makeText(BookClickActivity.this,"성공",Toast.LENGTH_SHORT).show();
            }else
            {
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {


            String login_url = "http://" + Basicinfo.URL + "/insertreport.php";

            try
            {

                String sid = params[0];
                String wid = params[1];
                String title = params[2];
                String content = params[3];
                String date = params[4];
                String state = params[5];
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("sid","UTF-8") + "=" + URLEncoder.encode(sid,"UTF-8") + "&"
                        + URLEncoder.encode("wid","UTF-8") + "=" + URLEncoder.encode(wid,"UTF-8")  + "&"
                        + URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8")  + "&"
                        + URLEncoder.encode("content","UTF-8") + "=" + URLEncoder.encode(content,"UTF-8")  + "&"
                        + URLEncoder.encode("date","UTF-8") + "=" + URLEncoder.encode(date,"UTF-8") + "&"
                        + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(state,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result="";
                String line="";

                //line = bufferedReader.readLine();
                //result+=line;

                while( (line = bufferedReader.readLine()) != null)
                {
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;

            }catch(MalformedURLException e)
            {
                e.printStackTrace();
            }catch (IOException e)
            {
                e.printStackTrace();
            }


            return null;
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
