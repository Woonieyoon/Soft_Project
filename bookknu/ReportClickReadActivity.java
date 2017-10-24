package myhome.bookknu;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
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
 * Created by sungw on 2017-10-13.
 */


//신고 읽기
public class ReportClickReadActivity extends AppCompatActivity {

    private TextView date;
    private TextView name;
    private TextView title;
    private TextView content;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportclickread);

        date = (TextView)findViewById(R.id.reportshowdate);
        name = (TextView)findViewById(R.id.reportshowname);
        title = (TextView)findViewById(R.id.reportshowtitle);
        content = (TextView)findViewById(R.id.reportshowcontent);

        Intent intent = getIntent();

        String sdate = intent.getExtras().getString("date");
        String nname = intent.getExtras().getString("name"); //글쓴이
        String nsname = intent.getExtras().getString("sname"); //신고자
        String ntitle = intent.getExtras().getString("title");
        String ncontent = intent.getExtras().getString("content");

        date.setText(sdate);
        name.setText(nname);
        title.setText(ntitle);
        content.setText(ncontent);

        ReportClickReadActivity.UpdateState u = new ReportClickReadActivity.UpdateState(); //눌렀으면 on
        u.execute(nsname,nname,ntitle,ncontent,sdate,"on");

    }

    class UpdateState extends AsyncTask<String,Void,String>
    {
        public UpdateState() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1"))
            {
                Toast.makeText(ReportClickReadActivity.this,"성공", Toast.LENGTH_SHORT).show();
            }else
            {
            }

        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://" + Basicinfo.URL + "/reportread.php";

            try{

                String sid = params[0];
                String wid = params[1];
                String title = params[2];
                String content = params[3];
                String date = params[4];
                String state = params[5];

                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("sid","UTF-8") + "=" + URLEncoder.encode(sid,"UTF-8") + "&"
                        + URLEncoder.encode("wid","UTF-8") + "=" + URLEncoder.encode(wid,"UTF-8") + "&"
                        + URLEncoder.encode("title","UTF-8") + "=" + URLEncoder.encode(title,"UTF-8") + "&"
                        + URLEncoder.encode("content","UTF-8") + "=" + URLEncoder.encode(content,"UTF-8") + "&"
                        + URLEncoder.encode("date","UTF-8") + "=" + URLEncoder.encode(date,"UTF-8") + "&"
                        + URLEncoder.encode("state","UTF-8") + "=" + URLEncoder.encode(state,"UTF-8");


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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,ReportStateActivity.class));
        }

        return super.onKeyDown(keyCode, event);
    }






}
