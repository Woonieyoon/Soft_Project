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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import java.util.Date;

/**
 * Created by sungw on 2017-08-01.
 */

public class BookInsertActivity extends AppCompatActivity {

    Button save,cancel;
    EditText title,contents;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.book_write);
        title = (EditText)findViewById(R.id.write_title);
        contents = (EditText)findViewById(R.id.write_content);
        save = (Button)findViewById(R.id.saveBtn);
        cancel = (Button)findViewById(R.id.cancelBtn);

        save.setOnClickListener(new View.OnClickListener() {  //저장 버튼
            @Override
            public void onClick(View v) {

                String user = Basicinfo.name;
                String book_title = title.getText().toString();
                String book_contents = contents.getText().toString();

                BookInsertActivity.InsertTask c = new BookInsertActivity.InsertTask(BookInsertActivity.this);
                c.execute("insert",user,book_title,book_contents);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() { //취소버튼
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

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
            alertDialog.setTitle("Login status");
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1")) //성공
            {
                Intent next = new Intent(BookInsertActivity.this,BookMainActivity.class);
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

            String type = params[0];
            String login_url = "http://" + Basicinfo.URL + "/content.php";

            if(type.equals("insert"))
            {
                try
                {

                    String id = params[1];
                    String title = params[2];
                    String content = params[3];
                    URL url =new URL(login_url);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,BookMainActivity.class));

        }

        return super.onKeyDown(keyCode, event);
    }

}
