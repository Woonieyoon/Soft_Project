package myhome.bookknu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.List;

/**
 * Created by sungw on 2017-09-30.
 */

//신고보기

public class ReportStateActivity extends AppCompatActivity {

    private ListView reportListView;
    private ReportAdapter adapter;
    private List<Report> reportList;

    private static final String TAG_RRESULTS = "result";
    private static final String TAG_RSID = "sid";
    private static final String TAG_RWID = "wid";
    private static final String TAG_RTITLE = "title";
    private static final String TAG_RCONTENT = "content";
    private static final String TAG_RDATE = "date";
    private static final String TAG_RSTATE = "state";

    private String myJSON;
    private JSONArray data = null;

    private View layout;
    private AlertDialog ad;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reportstate);
        reportListView = (ListView) findViewById(R.id.report_statelistview);
        reportList = new ArrayList<Report>();
        adapter = new ReportAdapter(getApplicationContext(), reportList); //원래

        ReportStateActivity.getReport g = new ReportStateActivity.getReport();
        g.execute();

        reportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String ndate = ((Report) adapter.getItem(position)).getDate();  //날짜
                final String nsname = ((Report) adapter.getItem(position)).getSname(); //신고인
                final String nwname = ((Report) adapter.getItem(position)).getWname();  //작성자
                final String ntitle = ((Report) adapter.getItem(position)).getTitle(); //제목
                final String ncontent = ((Report) adapter.getItem(position)).getContent(); //내용

                CharSequence info[] = new CharSequence[]{"자세히 보기", "로그인 차단", "글쓰기 차단"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ReportStateActivity.this);

                builder.setTitle("선택하세요!!");
                builder.setIcon(R.drawable.knu);
                builder.setItems(info, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0: //자세히 보기

                                Intent n_click = new Intent(ReportStateActivity.this,ReportClickReadActivity.class);
                                n_click.putExtra("sname",nsname);
                                n_click.putExtra("name",nwname);
                                n_click.putExtra("title",ntitle);
                                n_click.putExtra("content",ncontent);
                                n_click.putExtra("date",ndate);
                                startActivity(n_click);
                                break;

                            case 1: //로그인 차단

                                ReportStateActivity.Authority r = new ReportStateActivity.Authority(ReportStateActivity.this);
                                r.execute( "http://" + Basicinfo.URL + "/authority.php",nwname,"off");

                                break;

                            case 2: //글쓰기 차단

                                ReportStateActivity.Authority w = new ReportStateActivity.Authority(ReportStateActivity.this);
                                w.execute( "http://" + Basicinfo.URL + "/authoritywriting.php",nwname,"off");

                                break;
                        }
                        dialog.dismiss();
                    }
                });

                builder.show();

            }
        });
    }


    class getReport extends AsyncTask<Void,Void,String>
    {
        public getReport() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            myJSON = result;
            insertReportBoard();

        }

        @Override
        protected String doInBackground(Void... params) {
            String uri = "http://" + Basicinfo.URL + "/getreport.php";
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
    }

    public void insertReportBoard()
    {
        //test---------------------------------------------------------------------------------------
        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            data = jsonObj.getJSONArray(TAG_RRESULTS);

            for(int i=0; i< data.length(); i++)
            {
                JSONObject c = data.getJSONObject(i);
                String sid = c.getString(TAG_RSID);
                String wid = c.getString(TAG_RWID);
                String title = c.getString(TAG_RTITLE);
                String content = c.getString(TAG_RCONTENT);
                String date = c.getString(TAG_RDATE);
                String state = c.getString(TAG_RSTATE);


                if(state.equals("off"))//state가 off 일때
                {
                    reportList.add(new Report(0,sid,wid,title,content,date));
                }else //state가 on 일때
                {
                    reportList.add(new Report(1,sid,wid,title,content,date));
                }
            }

            reportListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    //메시지 및 로그인 차단
    class Authority extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        Authority(Context ctx)
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
                //Toast.makeText(BookClickActivity.this,"성공",Toast.LENGTH_SHORT).show();
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


            String login_url =params[0];

            try
            {

                String id = params[1];
                String ustate = params[2];

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
                        + URLEncoder.encode("ustate","UTF-8") + "=" + URLEncoder.encode(ustate,"UTF-8");

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,SettingActivity.class));
        }

        return super.onKeyDown(keyCode, event);
    }



}
