package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

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
 * Created by sungw on 2017-08-30.
 */
//경북대 숲 메인
public class FreeMainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private Button write;
    private int number=0;

    View layout;
    AlertDialog ad;
    private Spinner sp;
    private ArrayAdapter spadapter;

    //글쓰기위함
    private String myJSON;
    private static final String FreeTAG_RESULTS = "result";
    private static final String FreeTAG_NUM = "num";
    private JSONArray mydata = null;

    String free_num,com; //번호 , 내용


    //게시판 타이틀 위함
    private String srJSON;
    private JSONArray peoples = null;

    private static final String FREE_Cre_RESULTS = "result";
    private static final String FREE_Cre_NUM = "num";
    private static final String FREE_Cre_CONTENT = "content";
    private static final String FREE_Cre_DATE = "date";

    List<ParentObject> li;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.free_drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView)findViewById(R.id.free_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        FreeMainActivity.GetDataJSON b = new FreeMainActivity.GetDataJSON();
        b.execute("http://" + Basicinfo.URL + "/getfreetitle.php");



        write = (Button)findViewById(R.id.free_write_button);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.free_insert_dialog,null);
                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(FreeMainActivity.this);

                aDlgb.setView(layout);
                ad = aDlgb.create();
                ad.show();

                Button save = (Button)layout.findViewById(R.id.free_save); //저장버튼누를시
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText save = (EditText)layout.findViewById(R.id.free_edit);
                        com = save.getText().toString();

                        //순서를 구하고 삽입
                        FreeMainActivity.myGetDataJSON start = new FreeMainActivity.myGetDataJSON();
                        start.execute("http://" + Basicinfo.URL + "/getfreenum.php");

                        ad.dismiss();

                    }
                });

                //취소
                Button cancel = (Button)layout.findViewById(R.id.free_cancel); //저장버튼누를시
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ad.dismiss();

                    }
                });

            }
        });



        NavigationView n = (NavigationView)findViewById(R.id.free_nav_view);
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
                    number = 8;
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

    //순차 뽑아오기
    public void insertBoard()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            mydata = jsonObj.getJSONArray(FreeTAG_RESULTS);
            String num;
            if(mydata.length() == 0)
            {
                num = "1";
            }else
            {
                JSONObject c = mydata.getJSONObject(0);
                String n = c.getString(FreeTAG_NUM);
                int number = Integer.parseInt(n) + 1;
                num =  number+"";
            }

            //freenum insert
            FreeMainActivity.InCountTask ist = new FreeMainActivity.InCountTask();
            ist.execute(num);

            //내용삽입
            FreeMainActivity.InsertTask c = new FreeMainActivity.InsertTask(FreeMainActivity.this);
            c.execute(num,Basicinfo.name,com);


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // 뿌리기위함
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
            myJSON = result;
            insertBoard();
        }
    }

    //삽입
    class InsertTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        InsertTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog  = new AlertDialog.Builder(context).create();
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1")) //성공
            {
                Intent next = new Intent(FreeMainActivity.this,FreeMainActivity.class);
                startActivity(next);
            }else
            {
                result = "다시 입력하세요";
                alertDialog.setMessage(result);
                alertDialog.show();
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

            String login_url = "http://" + Basicinfo.URL + "/insertfree.php";

            try
            {
                String num = params[0];
                String id = params[1];
                String content = params[2];
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));

                String post_data = URLEncoder.encode("num","utf-8") + "=" + URLEncoder.encode(num,"utf-8") + "&"
                        +URLEncoder.encode("id","utf-8") + "=" + URLEncoder.encode(id,"utf-8") + "&"
                        + URLEncoder.encode("content","utf-8") + "=" + URLEncoder.encode(content,"utf-8") + "&"
                        + URLEncoder.encode("date","utf-8") + "=" + URLEncoder.encode(date,"utf-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = null;
                inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result="";
                String line="";

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

    //freenum 삽입하기
    class InCountTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String url1 = "http://" + Basicinfo.URL + "/updatefreenum.php";

            try
            {

                String result="";
                String line="";

                String num = params[0];

                URL url =new URL(url1);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"utf-8"));

                String post_data = URLEncoder.encode("num","utf-8") + "=" + URLEncoder.encode(num,"utf-8");


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

                final Dialog dig = new Dialog(FreeMainActivity.this);
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


    public void insertTitle()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(srJSON);
            peoples = jsonObj.getJSONArray(FREE_Cre_RESULTS);
            TitleCreator titleCreator = TitleCreator.get(this);
            titleCreator._titleParents = new ArrayList<>();

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String num = c.getString(FREE_Cre_NUM);
                TitleParent title = new TitleParent(String.format("#경북대숲 %s번째 이야기",num));
                titleCreator._titleParents.add(title);
            }


            List<TitleParent> titles = titleCreator._titleParents;
            List<ParentObject> parentObject = new ArrayList<>();

            int i=0;

            for(TitleParent title : titles)
            {
                JSONObject c = peoples.getJSONObject(i);
                String content = c.getString(FREE_Cre_CONTENT);
                String date = c.getString(FREE_Cre_DATE);
                List<Object> childList = new ArrayList<>();
                childList.add(new TitleChild(content,date));
                title.setChildObjectList(childList);
                parentObject.add(title);
                i++;
            }

            li = parentObject;


            MyAdapter adapter = new MyAdapter(this,li);
            adapter.setParentClickableViewAnimationDefaultDuration();
            adapter.setParentAndIconExpandOnClick(true);
            recyclerView.setAdapter(adapter);

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
            srJSON = result;
            insertTitle();
        }
    }


}

