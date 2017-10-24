package myhome.bookknu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
 * Created by sungw on 2017-10-22.
 */

public class LoginCut extends AppCompatActivity {

    private ListView cutListView;
    private CutAdapter adapter;
    private List<Cut> cutList;

    private String myJSON;
    private JSONArray data = null;

    private static final String TAG_CRESULTS = "result";
    private static final String TAG_CID = "id";
    private static final String TAG_CLOGIN = "login";
    private static final String TAG_CWRITING = "writing";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cutstate);
        cutListView = (ListView)findViewById(R.id.cut_statelistview);
        cutList = new ArrayList<Cut>();
        adapter = new CutAdapter(getApplicationContext(), cutList);

        LoginCut.getCut c = new LoginCut.getCut();
        c.execute();

        cutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final String name = ((Cut) adapter.getItem(position)).getCname();  //날짜

                CharSequence info[] = new CharSequence[]{"로그인 차단해제"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginCut.this);

                builder.setTitle("선택하세요!!");
                builder.setIcon(R.drawable.knu);
                builder.setItems(info, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0://차단해제
                                LoginCut.LoginRelease l = new LoginCut.LoginRelease(LoginCut.this);
                                l.execute(name,"on");
                                break;

                        }
                        dialog.dismiss();
                        Intent s = new Intent(LoginCut.this,LoginCut.class);
                        startActivity(s);
                        finish();
                    }
                });

                builder.show();
            }
        });



    }

    class getCut extends AsyncTask<Void,Void,String> //값 가져오기
    {
        public getCut() {
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
            String uri = "http://" + Basicinfo.URL + "/getCut.php";
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
            data = jsonObj.getJSONArray(TAG_CRESULTS);

            for(int i=0; i< data.length(); i++)
            {
                JSONObject c = data.getJSONObject(i);
                String cid = c.getString(TAG_CID);
                String clogin = c.getString(TAG_CLOGIN);
                String cwriting = c.getString(TAG_CWRITING);


                if(clogin.equals("off"))//state가 off 일때
                {
                    cutList.add(new Cut(cid));
                }
            }

            cutListView.setAdapter(adapter);

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // 차단
    class LoginRelease extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        LoginRelease(Context ctx)
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
                Toast.makeText(LoginCut.this,"성공",Toast.LENGTH_SHORT).show();
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


            String login_url ="http://" + Basicinfo.URL + "/locutrelease.php";

            try
            {

                String id = params[0];
                String login = params[1];

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
                        + URLEncoder.encode("login","UTF-8") + "=" + URLEncoder.encode(login,"UTF-8");

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
