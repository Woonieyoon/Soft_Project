package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by sungw on 2017-08-24.
 */

public class ClubReadingActivity extends AppCompatActivity {

    private ListView commentListView;
    private ClubCommentListAdapter adapter;
    private List<ClubComment> noticeList;
    private Button insert;
    private Dialog mMainDialog;

    View layout;
    AlertDialog ad;
    TextView cdate;
    TextView cname;
    TextView ctitle;
    TextView ccontent;

    String ccomJSON;
    JSONArray ccomdata = null;

    //댓글
    private static final String CCOMTAG_RESULTS = "result"; //댓글결과
    private static final String CCOMTAG_ID = "id";
    private static final String CCOMTAG_WID = "wid";
    private static final String CCOMTAG_TITLE = "title";
    private static final String CCOMTAG_CONTENT = "content";
    private static final String CCOMTAG_DATE = "date";
    private static final String CCOMTAG_COM = "com"; //댓글내용

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.club_read);

        cdate = (TextView)findViewById(R.id.club_showdate);
        cname = (TextView)findViewById(R.id.club_showname);
        ctitle = (TextView)findViewById(R.id.club_showtitle);
        ccontent = (TextView)findViewById(R.id.club_showcontent);

        Intent intent = getIntent();
        cdate.setText(intent.getExtras().getString("date"));
        cname.setText(intent.getExtras().getString("name"));
        ctitle.setText(intent.getExtras().getString("title"));
        ccontent.setText(intent.getExtras().getString("content"));

        commentListView = (ListView)findViewById(R.id.club_commentlist);
        noticeList =  new ArrayList<ClubComment>();
        adapter = new ClubCommentListAdapter(getApplicationContext(),noticeList);

        //댓글 뿌리기 위함
        ClubReadingActivity.GetDataJSON1 s = new ClubReadingActivity.GetDataJSON1();
        s.execute();

        insert = (Button)findViewById(R.id.club_insertcomment);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.clubcomment_dialog,null);
                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(ClubReadingActivity.this);

                aDlgb.setView(layout);
                ad = aDlgb.create();
                ad.show();

                //저장
                Button save = (Button)layout.findViewById(R.id.club_commentsave); //저장버튼누를시
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText save1 = (EditText)layout.findViewById(R.id.club_commentedit);
                        String str = save1.getText().toString();

                        ClubReadingActivity.CommentTask b = new ClubReadingActivity.CommentTask(ClubReadingActivity.this);
                        b.execute( ctitle.getText().toString() , ccontent.getText().toString(), cname.getText().toString() , Basicinfo.name , str );
                        ad.dismiss();

                        //댓글 갱신
                        ClubReadingActivity.GetDataJSON2 s2 = new ClubReadingActivity.GetDataJSON2();
                        s2.execute();
                    }
                });

                //취소
                Button cancel = (Button)layout.findViewById(R.id.club_commentcancel); //저장버튼누를시
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
                Toast.makeText(ClubReadingActivity.this,"성공",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(ClubReadingActivity.this,"실패",Toast.LENGTH_SHORT).show();
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

            String login_url = "http://" + Basicinfo.URL + "/insertclubcomment.php";

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
            JSONObject jsonObj = new JSONObject(ccomJSON);
            ccomdata = jsonObj.getJSONArray(CCOMTAG_RESULTS);

            for(int i=0; i< ccomdata.length(); i++)
            {
                JSONObject c = ccomdata.getJSONObject(i);

                String title1 = c.getString(CCOMTAG_TITLE);
                String content1 = c.getString(CCOMTAG_CONTENT);
                String id1 = c.getString(CCOMTAG_ID);
                String wid1 = c.getString(CCOMTAG_WID);
                String com1 = c.getString(CCOMTAG_COM);
                String date1 = c.getString(CCOMTAG_DATE);
                String sumdate;

                String t_title,c_content,n_name;
                t_title = ctitle.getText().toString();
                c_content = ccontent.getText().toString();
                n_name = cname.getText().toString();

                if(t_title.equals(title1) && c_content.equals(content1) && n_name.equals(id1)  ) {

                    int ch = Integer.parseInt(date1);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new ClubComment(com1,wid1,sumdate));
                }



            }

            commentListView.setAdapter(adapter);


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //게시판 데이터 뿌리기위함
    class GetDataJSON2 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = "http://" + Basicinfo.URL + "/getclubcomment.php";

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
            ccomJSON = result;
            insertBoard2();
        }
    }

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
            JSONObject jsonObj = new JSONObject(ccomJSON);
            ccomdata = jsonObj.getJSONArray(CCOMTAG_RESULTS);

            for(int i=0; i< ccomdata.length(); i++)
            {
                JSONObject c = ccomdata.getJSONObject(i);

                String title1 = c.getString(CCOMTAG_TITLE);
                String content1 = c.getString(CCOMTAG_CONTENT);
                String id1 = c.getString(CCOMTAG_ID);
                String wid1 = c.getString(CCOMTAG_WID);
                String com1 = c.getString(CCOMTAG_COM);
                String date1 = c.getString(CCOMTAG_DATE);
                String sumdate;

                String t_title,c_content,n_name;
                t_title = ctitle.getText().toString();
                c_content = ccontent.getText().toString();
                n_name = cname.getText().toString();

                if(t_title.equals(title1) && c_content.equals(content1) && n_name.equals(id1)  ) {

                    int ch = Integer.parseInt(date1);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    noticeList.add(new ClubComment(com1,wid1,sumdate));
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

            String uri = "http://" + Basicinfo.URL + "/getclubcomment.php";

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
            ccomJSON = result;
            insertBoard1();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,ClubMainActivity.class));
        }

        return super.onKeyDown(keyCode, event);
    }
}
