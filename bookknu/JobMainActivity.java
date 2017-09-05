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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
 * Created by sungw on 2017-08-24.
 */
//취업 메인 페이지
public class JobMainActivity  extends AppCompatActivity{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<JobListItem> listItems;

    private String myJSON;
    JSONArray peoples = null;

    private Button click;

    private static final String JOBTAG_RESULTS = "result";
    private static final String JOBTAG_ID = "id";
    private static final String JOBTAG_TITLE = "title";
    private static final String JOBTAG_CONTENT = "content";
    private static final String JOBTAG_DATE = "date";

    String s;
    int i=0;
    int number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_main);

        mToolbar = (Toolbar)findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.job_drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView)findViewById(R.id.job_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        click = (Button)findViewById(R.id.job_write_button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent write = new Intent(JobMainActivity.this,JobInsertActivity.class);
                startActivity(write);

            }
        });

        JobMainActivity.JOBGetDataJSON b = new JobMainActivity.JOBGetDataJSON();
        b.execute("http://" + Basicinfo.URL + "/getjobcontent.php");

        NavigationView n = (NavigationView)findViewById(R.id.job_nav_view);
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

    public void insertBoard()
    {
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(JOBTAG_RESULTS);

            for(int i=0; i< peoples.length(); i++)
            {
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(JOBTAG_ID);

                String title = c.getString(JOBTAG_TITLE);
                String content = c.getString(JOBTAG_CONTENT);
                String date = c.getString(JOBTAG_DATE);

                String sumdate;

                    int ch = Integer.parseInt(date);
                    int year = ch / 10000;
                    int month = (ch % 10000) / 100;
                    int day = (ch % 10000) % 100;
                    sumdate = year+"년 "+month+"월 "+day+"일";
                    JobListItem listItem = new JobListItem(title,content,id,sumdate);
                    listItems.add(listItem);

            }

            adapter = new JobAdapter(listItems,this);
            recyclerView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //잡게시판 데이터 뿌리기위함
    class JOBGetDataJSON extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String uri = params[0];

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();


                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(),"iso-8859-1"));

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
            //Toast.makeText(JobMainActivity.this,s,Toast.LENGTH_SHORT).show();

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

                final Dialog dig = new Dialog(JobMainActivity.this);
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
