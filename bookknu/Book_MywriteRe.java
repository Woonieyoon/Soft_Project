package myhome.bookknu;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
 * Created by sungw on 2017-08-13.
 */

//mylist에서 수정눌렀을때
public class Book_MywriteRe extends AppCompatActivity {

    EditText title,content;


    Button save,cancel;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_myrewriteactivity);
        Intent intent = getIntent();
        //intent.getExtras().getString("date")

        title = (EditText)findViewById(R.id.write_title1);
        content = (EditText)findViewById(R.id.write_content1) ;

        save = (Button)findViewById(R.id.saveBtn1);
        cancel = (Button)findViewById(R.id.cancelBtn1);

        title.setText(intent.getExtras().getString("title"));
        content.setText(intent.getExtras().getString("content"));


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Book_MywriteRe.reDeleteTask st = new Book_MywriteRe.reDeleteTask(Book_MywriteRe.this);
                st.execute(Basicinfo.name,title.getText().toString(),content.getText().toString());

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(Book_MywriteRe.this,Book_Mywrite.class);
                startActivity(next);
            }
        });

    }

    //지우기
    class reDeleteTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        reDeleteTask(Context ctx)
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
                Book_MywriteRe.reInsertTask st = new Book_MywriteRe.reInsertTask(Book_MywriteRe.this);
                st.execute( Basicinfo.name, title.getText().toString() , content.getText().toString() );
            }else
            {
                Toast.makeText(Book_MywriteRe.this,"실패",Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://" + Basicinfo.URL + "/mydelete.php";

            try
            {
                String id = params[0];
                String title = params[1];
                String content = params[2];
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
                        + URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8") + "&"
                        +URLEncoder.encode("content","UTF-8") + "=" + URLEncoder.encode(content,"UTF-8");

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


    //삽입
    class reInsertTask extends AsyncTask<String, Void, String> {

        Context context;
        reInsertTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1")) //성공
            {
                Toast.makeText(Book_MywriteRe.this,"수정성공",Toast.LENGTH_SHORT).show();
                Intent next = new Intent(Book_MywriteRe.this,Book_Mywrite.class);
                startActivity(next);
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

            String login_url = "http://" + Basicinfo.URL + "/content.php";

                try
                {

                    String id = params[0];
                    String title = params[1];
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

            return null;
        }
    }



}
