package com.example.sungw.knuprojcet;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String htmlTestUrl="https://my.knu.ac.kr/stpo/comm/support/loginPortal/login.action?redirUrl=%2Fstpo%2Fstpo%2Fmain%2Fmain.action&menuParam=901"; // 로그인 유알엘
    private EditText idField;
    private EditText pwField;
    private Button loginButton;
    private boolean loginCheck = false; // 로그인 확인
    String mId;
    String mPw;
    ProgressDialog asyncDialog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        idField = (EditText)findViewById(R.id.editText);  // 아이디 필드
        pwField = (EditText)findViewById(R.id.editText2); // 비번 필드
        loginButton = (Button)findViewById(R.id.button);  // 로그인 버튼

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mId = idField.getText().toString();
                mPw = pwField.getText().toString();


                LoginCheckTask t = new LoginCheckTask();
                Toast.makeText(MainActivity.this,"Click",Toast.LENGTH_SHORT).show();
                t.execute();
            }
        });

    }


    private class LoginCheckTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog = new ProgressDialog(MainActivity.this);
            asyncDialog.setMessage("로딩중입니다..");
            Toast.makeText(MainActivity.this,"Loading",Toast.LENGTH_SHORT).show();
            asyncDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try
            {
                Connection.Response res= Jsoup.connect(htmlTestUrl)
                        .data("user.usr_id",mId)
                        .data("user.passwd",mPw)
                        .method(Connection.Method.POST)
                        .execute();
                Map<String, String> loginCookies=res.cookies();

                Document doc = Jsoup.connect("http://my.knu.ac.kr/stpo/stpo/main/main.action").cookies(loginCookies).get();
                if(doc.toString().contains("//게시물")) {

                    loginCheck = !loginCheck;

                    Document doc2 = Jsoup.connect("http://my.knu.ac.kr/stpo/stpo/stud/infoMngt/basisMngt/list.action").cookies(loginCookies).get();

                }

            }catch (Exception e)
            {
               e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            asyncDialog.dismiss();

            if(loginCheck)
            {
                Toast.makeText(MainActivity.this, "로그인성공.", Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(MainActivity.this,  "id, password을 다시 확인하세요.", Toast.LENGTH_SHORT).show();

        }
    }

}
