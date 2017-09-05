package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

    private TextView date;
    private TextView name;
    private TextView title;
    private TextView content;

    String comJSON;
    JSONArray comdata = null;

    String countJSON;
    JSONArray countdata = null;

    //댓글
    private static final String COMTAG_RESULTS = "result"; //댓글결과
    private static final String COMTAG_ID = "id";
    private static final String COMTAG_WID = "wid";
    private static final String COMTAG_TITLE = "title";
    private static final String COMTAG_CONTENT = "content";
    private static final String COMTAG_DATE = "date";
    private static final String COMTAG_COM = "com"; //댓글내용

    //조회수
    private static final String COUNTTAG_RESULTS = "result"; //댓글결과
    private static final String COUNTTAG_ID = "id";
    private static final String COUNTTAG_TITLE = "title";
    private static final String COUNTTAG_CONTENT = "content";
    private static final String COUNTTAG_DATE = "date";
    private static final String COUNTTAG_SUB = "sub";
    private static final String COUNTTAG_COUNT = "count"; //댓글내용

    String count="0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.bookclickactivity);

        date = (TextView)findViewById(R.id.showdate);
        name = (TextView)findViewById(R.id.showname);
        title = (TextView)findViewById(R.id.showtitle);
        content = (TextView)findViewById(R.id.showcontent);

        Intent intent = getIntent();
        date.setText(intent.getExtras().getString("date"));
        name.setText(intent.getExtras().getString("name"));
        title.setText(intent.getExtras().getString("title"));
        content.setText(intent.getExtras().getString("content"));

        commentListView = (ListView)findViewById(R.id.commentlist);
        noticeList =  new ArrayList<Comment>();
        adapter = new CommentListAdapter(getApplicationContext(),noticeList);

        //noticeList.add(new Comment("  굿!","홍길동","2017-06-20"));
        //noticeList.add(new Comment("  감사합니다","홍길동","2017-06-05"));
        //noticeList.add(new Comment("  감사합니다","홍길동","2017-06-04"));

        //adapter = new CommentListAdapter(getApplicationContext(),noticeList);
        //commentListView.setAdapter(adapter);

        //댓글 뿌리기 위함
        BookClickActivity.GetDataJSON1 s = new BookClickActivity.GetDataJSON1();
        s.execute();

        //조회수 가져오기 및 조회수 1증가
        BookClickActivity.GetCount ge = new BookClickActivity.GetCount();
        ge.execute();




        //-----------------------------------------------------------------------------------------------------------------
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
                        //noticeList.add(new Comment("  "+str,"sungwon126","2017-06-20"));
                        BookClickActivity.CommentTask b = new BookClickActivity.CommentTask(BookClickActivity.this);
                        b.execute( title.getText().toString() , content.getText().toString(), name.getText().toString() , Basicinfo.name , str );
                        ad.dismiss();

                        //댓글 갱신
                        BookClickActivity.GetDataJSON2 s2 = new BookClickActivity.GetDataJSON2();
                        s2.execute();
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

    //--------------------------------------------------------------------------------------------------------------
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







    //-----------------------------------------------------------------------------------------------------------------------
    //댓글 데이터뿌리기 위함
    //데이터게시판 뿌리기 위함
    public void insertBoard1()
    {
        //test---------------------------------------------------------------------------------------
        int book_num = adapter.getCount();//글전체 개수 파악
        for(int i=0; i<book_num ;i++)
        {
            noticeList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
        }

        try{
            JSONObject jsonObj = new JSONObject(comJSON);
            comdata = jsonObj.getJSONArray(COMTAG_RESULTS);

            for(int i=0; i< comdata.length(); i++)
            {
                JSONObject c = comdata.getJSONObject(i);

                String title1 = c.getString(COMTAG_TITLE);
                String content1 = c.getString(COMTAG_CONTENT);
                String id1 = c.getString(COMTAG_ID);
                String wid1 = c.getString(COMTAG_WID);
                String com1 = c.getString(COMTAG_COM);
                String date1 = c.getString(COMTAG_DATE);
                String sumdate;

                String t_title,c_content,n_name;
                t_title = title.getText().toString();
                c_content = content.getText().toString();
                n_name = name.getText().toString();

                if(t_title.equals(title1) && c_content.equals(content1) && n_name.equals(id1)  ) {

                    int ch = Integer.parseInt(date1);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new Comment(com1,wid1,sumdate));
                }



            }

            commentListView.setAdapter(adapter);
            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            //adapter = new CommentListAdapter(getApplicationContext(),noticeList);
            //commentListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //게시판 데이터 뿌리기위함
    class GetDataJSON1 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = "http://" + Basicinfo.URL + "/getcomment.php";

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
            comJSON = result;
            insertBoard1();
        }
    }









    //-----------------------------------------------------------------------------------------------------------------------
    //데이터뿌리기 위함
    //데이터게시판 뿌리기 위함
    //댓글 저장버튼 누른후 뿌려주기
    public void insertBoard2()
    {
        //test---------------------------------------------------------------------------------------
        int book_num = adapter.getCount();//글전체 개수 파악
        for(int i=0; i<book_num ;i++)
        {
            noticeList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
        }

        try{
            JSONObject jsonObj = new JSONObject(comJSON);
            comdata = jsonObj.getJSONArray(COMTAG_RESULTS);

            for(int i=0; i< comdata.length(); i++)
            {
                JSONObject c = comdata.getJSONObject(i);

                String title1 = c.getString(COMTAG_TITLE);
                String content1 = c.getString(COMTAG_CONTENT);
                String id1 = c.getString(COMTAG_ID);
                String wid1 = c.getString(COMTAG_WID);
                String com1 = c.getString(COMTAG_COM);
                String date1 = c.getString(COMTAG_DATE);
                String sumdate;

                String t_title,c_content,n_name;
                t_title = title.getText().toString();
                c_content = content.getText().toString();
                n_name = name.getText().toString();

                if(t_title.equals(title1) && c_content.equals(content1) && n_name.equals(id1)  ) {

                    int ch = Integer.parseInt(date1);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new Comment(com1,wid1,sumdate));
                }



            }

            commentListView.setAdapter(adapter);



            // 댓글 넣기
            BookClickActivity.GetDataJSON3 g = new BookClickActivity.GetDataJSON3();
            String sub_count = adapter.getCount()+"";
            //Toast.makeText(BookClickActivity.this,"카운터는"+count,Toast.LENGTH_SHORT).show();
            g.execute(name.getText().toString() , title.getText().toString() , content.getText().toString(), sub_count );





        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //게시판 데이터 뿌리기위함
    class GetDataJSON2 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = "http://" + Basicinfo.URL + "/getcomment.php";

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
            comJSON = result;
            insertBoard2();
        }
    }






    //-----------------------------------------------------------------------------------------------------------------------
    //댓글,조회수 삽입

    //게시판 데이터 뿌리기위함
    class GetDataJSON3 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {


            String url1 = "http://" + Basicinfo.URL + "/incount.php";

                try
                {

                    String result="";
                    String line="";


                    String id = params[0];
                    String title = params[1];
                    String content = params[2];
                    String sub = params[3];

                    URL url =new URL(url1);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //httpURLConnection.setConnectTimeout(8000);
                    //httpURLConnection.setReadTimeout(8000);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));

                    String post_data = URLEncoder.encode("id","utf-8") + "=" + URLEncoder.encode(id,"utf-8") + "&"
                            + URLEncoder.encode("title","utf-8") + "=" + URLEncoder.encode(title,"utf-8") + "&"
                            + URLEncoder.encode("content","utf-8") + "=" + URLEncoder.encode(content,"utf-8")+ "&"
                            + URLEncoder.encode("sub","utf-8") + "=" + URLEncoder.encode(sub,"utf-8");


                    bufferedWriter.write(post_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    outputStream.close();


                    InputStream inputStream = null;
                    inputStream = httpURLConnection.getInputStream();

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                    result = "g";
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

        @Override
        protected void onPostExecute(String result) {

        }

    }










    //-------------------------------------------------------------------------------------------------------------------------
    //조회수 가져오기
    class GetCount extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {


            String uri = "http://" + Basicinfo.URL + "/getcount.php";

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
            //Toast.makeText(BookMainActivity.this,result,Toast.LENGTH_SHORT).show();
            getBoard();
        }

    }

    //조회수 가져오기 실행
    public void getBoard()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(countJSON);
            countdata = jsonObj.getJSONArray(COUNTTAG_RESULTS);

            for(int i=0; i< countdata.length(); i++)
            {
                JSONObject c = countdata.getJSONObject(i);
                String c_id = c.getString(COUNTTAG_ID);
                String c_title = c.getString(COUNTTAG_TITLE);
                String c_content = c.getString(COUNTTAG_CONTENT);
                String c_date = c.getString(COUNTTAG_DATE);
                String c_sub = c.getString(COUNTTAG_SUB);
                String c_count = c.getString(COUNTTAG_COUNT);


                if(c_id.equals( name.getText().toString() ) && c_title.equals(title.getText().toString()) && c_content.equals(content.getText().toString()) )
                {
                    if(c_count.equals(""))
                    {
                        //Toast.makeText(BookClickActivity.this,c_id,Toast.LENGTH_SHORT).show();
                        count="1";
                        BookClickActivity.InCountTask ist = new BookClickActivity.InCountTask();
                        ist.execute(name.getText().toString() , title.getText().toString() , content.getText().toString(), count);
                    }else
                    {
                        int n = Integer.parseInt(c_count);
                        n++;
                        count=n+"";
                        BookClickActivity.InCountTask ist = new BookClickActivity.InCountTask();
                        ist.execute(name.getText().toString() , title.getText().toString() , content.getText().toString(), count);
                    }

                }


            }



        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //조회수 삽입하기
    class InCountTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {


            String url1 = "http://" + Basicinfo.URL + "/insecount.php";

            try
            {

                String result="";
                String line="";


                String id = params[0];
                String title = params[1];
                String content = params[2];
                String count = params[3];

                URL url =new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));

                String post_data = URLEncoder.encode("id","utf-8") + "=" + URLEncoder.encode(id,"utf-8") + "&"
                        + URLEncoder.encode("title","utf-8") + "=" + URLEncoder.encode(title,"utf-8") + "&"
                        + URLEncoder.encode("content","utf-8") + "=" + URLEncoder.encode(content,"utf-8")+ "&"
                        + URLEncoder.encode("count","utf-8") + "=" + URLEncoder.encode(count,"utf-8");


                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();


                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));


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

        @Override
        protected void onPostExecute(String result) {

        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,BookMainActivity.class));
        }

        return super.onKeyDown(keyCode, event);
    }


}

