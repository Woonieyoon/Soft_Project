package myhome.bookknu;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
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

/**
 * Created by sungw on 2017-07-31.
 */

//로그인 페이지

public class RegisterActivity extends AppCompatActivity {

    Button register;
    TextView id,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        id = (TextView)findViewById(R.id.idEdit);
        pwd = (TextView)findViewById(R.id.pwEdit);
        register = (Button)findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = id.getText().toString();
                String password = pwd.getText().toString();
                String type  = "register";

                CustomTask c = new CustomTask(RegisterActivity.this);
                c.execute(type,username,password);
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

}
