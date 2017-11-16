package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
 * Created by sungw on 2017-08-27.
 */
//메시지 확인 Activity
public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    private Button m_send,m_receive;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private ListView messageListView;
    private MessageListAdapter adapter;
    private List<Message> messageList;

    private static final String message_TAG_RESULTS = "result";
    private static final String message_TAG_SEND = "send";
    private static final String message_TAG_RECEIVE = "receive";
    private static final String message_TAG_CONTENT = "content";
    private static final String message_TAG_DATE = "date";

    private String myJSON;
    JSONArray peoples = null;
    private int number=0;

    private View layout;
    private AlertDialog ad;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.message_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.message_drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        messageListView = (ListView)findViewById(R.id.messagelist);

        messageList =  new ArrayList<Message>();
        //noticeList.add(new Book("0","0","0","0","1개","2"));
        adapter = new MessageListAdapter(getApplicationContext(),messageList); //원래


        m_send = (Button)findViewById(R.id.send_button);
        m_receive = (Button)findViewById(R.id.receive_button);

        m_send.setOnClickListener(this);
        m_receive.setOnClickListener(this);

        m_send.setSelected(true);

        //default send 화면
        MessageActivity.GetDataJSON b = new MessageActivity.GetDataJSON();
        b.execute("http://" + Basicinfo.URL + "/messagegetcontent.php");



        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int s = position;
                CharSequence info[] = new CharSequence[] {"Message 보내기", "Message 확인"};

                AlertDialog.Builder builder = new AlertDialog.Builder(MessageActivity.this);
                builder.setTitle("선택하세요!!");
                builder.setIcon(R.drawable.knu);

                builder.setItems(info, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which)
                        {
                            case 0: //Message 보내기

                                Context ctx = MessageActivity.this;
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
                                        String receive = ((Message)adapter.getItem(s)).getM_send(); ;
                                        String message = save.getText().toString();

                                        MessageActivity.MessageTask b = new MessageActivity.MessageTask(MessageActivity.this);
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

                            case 1: //Message 확인

                                Context ctx1 = MessageActivity.this;
                                LayoutInflater inflater1 = (LayoutInflater)ctx1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                layout = inflater1.inflate(R.layout.message_confirm,null);
                                final AlertDialog.Builder aDlgb1 = new AlertDialog.Builder(ctx1);
                                aDlgb1.setView(layout);
                                ad = aDlgb1.create();
                                ad.show();

                                TextView tv = (TextView)layout.findViewById(R.id.message_cf);
                                tv.setText( ((Message)adapter.getItem(s)).getM_content().toString() );
                                break;



                        }

                    }
                });
                builder.show();
//                String ndate = ((Book)adapter.getItem(position)).getDate();  //날짜
//                String nname =  ((Book)adapter.getItem(position)).getName(); //이름
//                String ntitle = ((Book)adapter.getItem(position)).getTitle(); //제목
//                String ncontent =  ((Book)adapter.getItem(position)).getContent(); //내용
//
//                Intent n_click = new Intent(BookMainActivity.this,BookClickActivity.class);
//                n_click.putExtra("date",ndate);
//                n_click.putExtra("name",nname);
//                n_click.putExtra("title",ntitle);
//                n_click.putExtra("content",ncontent);
//
//                startActivity(n_click);

                //Toast.makeText(BookMainActivity.this,a, Toast.LENGTH_SHORT).show();
                //Uri uri = Uri.parse("http://gp.knu.ac.kr"+a);
                //Intent t = new Intent(BookMainActivity.this,BookClickActivity.class);
                //startActivity(t);
            }
        });





        NavigationView n = (NavigationView)findViewById(R.id.mnav_view);
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

        }else if(a==6)
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


    //message 작업
    class MessageTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        MessageTask(Context ctx)
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
                Toast.makeText(context,"성공",Toast.LENGTH_SHORT).show();
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





    //메시지 send 게시판 뿌리기 위함
    public void insertBoard()
    {
        //test---------------------------------------------------------------------------------------
        int message_num = adapter.getCount();//글전체 개수 파악
        for(int i=0; i<message_num ;i++)
        {
            messageList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
        }


        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(message_TAG_RESULTS);

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String send = c.getString(message_TAG_SEND);
                String receive = c.getString(message_TAG_RECEIVE);
                String content = c.getString(message_TAG_CONTENT);
                String date = c.getString(message_TAG_DATE);

                String sumdate;

                if(send.equals(Basicinfo.name)) {
                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    messageList.add(new Message(receive,send,content,sumdate));
                }

            }

            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            messageListView.setAdapter(adapter);

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

    //receive message
    public void receive_MBoard()
    {
        //test---------------------------------------------------------------------------------------
        int message_num = adapter.getCount();//글전체 개수 파악
        for(int i=0; i<message_num ;i++)
        {
            messageList.remove(0); //지워지면 position 값이 계속 감소되므로 맨위에서 지우는게 옳다고 판단됨.
        }


        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(message_TAG_RESULTS);

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String send = c.getString(message_TAG_SEND);
                String receive = c.getString(message_TAG_RECEIVE);
                String content = c.getString(message_TAG_CONTENT);
                String date = c.getString(message_TAG_DATE);

                String sumdate;

                if(receive.equals(Basicinfo.name)) {
                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    //Toast.makeText(BookMainActivity.this,content,Toast.LENGTH_SHORT).show();
                    messageList.add(new Message(send,receive,content,sumdate));
                }

            }

            //adapter = new BookListAdapter(getApplicationContext(),noticeList);
            messageListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //게시판 데이터 뿌리기위함
    class GetDataJSON1 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader = null;

            try {
                String send = Basicinfo.name;

                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = con.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));

                String post_data = URLEncoder.encode("send","utf-8") + "=" + URLEncoder.encode(send,"utf-8") + "&"
                      ;

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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
            receive_MBoard();
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

                final Dialog dig = new Dialog(MessageActivity.this);
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

    @Override
    public void onClick(View v) {

        m_send.setSelected(false);
        m_receive.setSelected(false);


        switch(v.getId())
        {
            case R.id.send_button :
                m_send.setSelected(true);
                MessageActivity.GetDataJSON b = new MessageActivity.GetDataJSON();
                b.execute("http://" + Basicinfo.URL + "/messagegetcontent.php");
                break;

            case R.id.receive_button :
                m_receive.setSelected(true);
                MessageActivity.GetDataJSON1 c = new MessageActivity.GetDataJSON1();
                c.execute("http://" + Basicinfo.URL + "/msget.php");


                break;
        }
    }








}
