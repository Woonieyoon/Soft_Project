package com.example.sungw.knuprojcet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//회원가입
public class RegisterActivity extends AppCompatActivity {

    Button register;
    TextView id,pwd;


      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

          id = (TextView)findViewById(R.id.idEdit);
          pwd = (TextView)findViewById(R.id.pwEdit);
          register = (Button)findViewById(R.id.registerButton);

          register.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  String joinid = id.getText().toString();
                  String joinpwd = pwd.getText().toString();
                  try {
                      String result  = new CustomTask().execute(joinid,joinpwd,"join").get();
                      if(result.equals("id")) {
                          Toast.makeText(RegisterActivity.this,"이미 존재하는 아이디입니다.",Toast.LENGTH_SHORT).show();
                          id.setText("");
                          pwd.setText("");
                      } else if(result.equals("ok")) {
                          id.setText("");
                          pwd.setText("");
                          Toast.makeText(RegisterActivity.this,"회원가입을 축하합니다.",Toast.LENGTH_SHORT).show();
                          finish();
                      }
                  }catch (Exception e) {}

              }
          });
    }


    class CustomTask extends AsyncTask<String, Void, String> {
        String sendMsg, receiveMsg;
        @Override
        protected String doInBackground(String... strings) {
            try {
                String str;
                URL url = new URL("http://192.168.0.6:8080/project/data.jsp");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
                sendMsg = "id="+strings[0]+"&pwd="+strings[1]+"&type="+strings[2];
                osw.write(sendMsg);
                osw.flush();
                if(conn.getResponseCode() == conn.HTTP_OK) {
                    InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(tmp);
                    StringBuffer buffer = new StringBuffer();
                    while ((str = reader.readLine()) != null) {
                        buffer.append(str);
                    }
                    receiveMsg = buffer.toString();

                } else {
                    Log.i("통신 결과", conn.getResponseCode()+"에러");
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return receiveMsg;
        }
    }








}
