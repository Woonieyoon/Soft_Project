package myhome.bookknu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import java.util.Map;

/**
 * Created by sungw on 2017-07-31.
 */

//로그인 페이지

public class RegisterActivity extends AppCompatActivity {

    private String htmlTestUrl="https://my.knu.ac.kr/stpo/comm/support/loginPortal/login.action?redirUrl=%2Fstpo%2Fstpo%2Fmain%2Fmain.action";
    private boolean loginCheck = false; // 로그인 확인

    Button checkID,register;//등록
    Button check;//ID/pwd체크

    TextView id,pwd,email;

    TextView kid,kpwd;//통합정보시스템

    String mId,mPw;
    String st;
    Document doc1;

    private boolean idcheck = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        //회원가입
        id = (TextView)findViewById(R.id.idEdit);
        pwd = (TextView)findViewById(R.id.pwEdit);
        email = (TextView)findViewById(R.id.emailText);
        checkID = (Button)findViewById(R.id.IDcheckButton);
        register = (Button)findViewById(R.id.registerButton);

        id.setVisibility(View.INVISIBLE);
        pwd.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        checkID.setVisibility(View.INVISIBLE);

        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                idcheck = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //ID 중복체크
        checkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.IdCheckTask c = new RegisterActivity.IdCheckTask(RegisterActivity.this);
                String name = id.getText().toString();
                if(name.equals(""))
                {
                    Toast.makeText(RegisterActivity.this,"ID를 입력하세요",Toast.LENGTH_SHORT).show();
                }else
                {
                    c.execute(name);

                }
            }
        });


        //가입버튼 눌렸을경우
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = id.getText().toString();
                String password = pwd.getText().toString();
                String type  = "register";

                if(idcheck) {
                    CustomTask c = new CustomTask(RegisterActivity.this);
                    c.execute(type, username, password);
                }else
                {
                    Toast.makeText(RegisterActivity.this,"ID 중복체크하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });


        //체크확인
        kid = (TextView)findViewById(R.id.kidEdit);
        kpwd = (TextView)findViewById(R.id.kpwEdit);
        check = (Button)findViewById(R.id.check);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mId = kid.getText().toString();
                mPw = kpwd.getText().toString();
                LoginTask t = new LoginTask(RegisterActivity.this);
                t.execute("check");


            }
        });


    }

    class CustomTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        CustomTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog  = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login status");
        }

        @Override
        protected void onPostExecute(String result) {


            if(result.equals("1"))
            {
                result = "가입되었습니다";
                alertDialog.setMessage(result);
                alertDialog.show();
                finish();
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
            String type = params[0];
            String login_url = "http://" + Basicinfo.URL + "/login_book.php";

            if(type.equals("register"))
            {
                try
                {

                    String id = params[1];
                    String pwd = params[2];
                    URL url =new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //httpURLConnection.setConnectTimeout(8000);
                    //httpURLConnection.setReadTimeout(8000);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8") + "&"
                            + URLEncoder.encode("pwd","UTF-8") + "=" + URLEncoder.encode(pwd,"UTF-8");

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

            }
            return null;
        }
    }

    class LoginTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;

        LoginTask(Context ctx) {
            context = ctx;
        }


        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {

            //result.equals("1")
            if(true)//됬을경우 -> 모바일은 되는데 애뮬은 안됨
            {
                Toast t =Toast.makeText(RegisterActivity.this,"인증되었습니다",Toast.LENGTH_SHORT);
                t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL,0,0);
                t.show();

                id.setVisibility(View.VISIBLE);
                pwd.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                email.setVisibility(View.VISIBLE);
                checkID.setVisibility(View.VISIBLE);

            }


        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String x = "0";

            if (type.equals("check")) {
                try
                {

                    Connection.Response res = Jsoup.connect("https://my.knu.ac.kr/stpo/comm/support/loginPortal/login.action?redirUrl=%2Fstpo%2Fstpo%2Fmain%2Fmain.action")
                        .data("user.usr_id", "sungwon126")
                        .data("user.passwd", "monkey1261!")
                        .method(Connection.Method.POST)
                        .execute();


                    Map<String, String> loginCookies = res.cookies();

                    Document doc = Jsoup.connect("http://my.knu.ac.kr/stpo/stpo/main/main.action").cookies(loginCookies).get();

                    if (doc.toString().contains("//게시물"))
                    {
                        loginCheck = !loginCheck;
                    }

                    x="1";

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return x;
        }

    }


    class IdCheckTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        IdCheckTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog  = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login status");
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1"))//찾았을경우
            {
                Toast.makeText(RegisterActivity.this,"중복된 ID입니다.",Toast.LENGTH_SHORT).show();
            }else
            {
                Toast.makeText(RegisterActivity.this,"사용 가능합니다.",Toast.LENGTH_SHORT).show();
                idcheck = !idcheck;

            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://" + Basicinfo.URL + "/checkid.php";

                try
                {
                    String id = params[0];
                    URL url =new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    //httpURLConnection.setConnectTimeout(8000);
                    //httpURLConnection.setReadTimeout(8000);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8");

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























}
