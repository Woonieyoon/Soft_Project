package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
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

/**
 * Created by sungw on 2017-07-31.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText idField;
    private EditText pwField;
    private Button loginButton;
    private TextView registerButton;

    private String myJSON;
    JSONArray wifi_state = null;

    private String myAJSON;
    JSONArray auth_state = null;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_ID = "id";
    private static final String TAG_STATE = "ustatus";


    private static final String TAG_AID = "id";
    private static final String TAG_ALogin = "login";
    private static final String TAG_AWriting = "writing";

    private String myWSON;
    private JSONArray data = null;



    String type;
    String id,pwd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        idField = (EditText)findViewById(R.id.idEdit);  // 아이디 필드
        pwField = (EditText)findViewById(R.id.pwEdit); // 비번 필드
        loginButton = (Button)findViewById(R.id.loginB);  // 로그인 버튼


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(internet_check()) {
                    type = "login";
                    id = idField.getText().toString();
                    pwd = pwField.getText().toString();

                    LoginActivity.LoginTask c = new LoginActivity.LoginTask(LoginActivity.this);
                    c.execute(type, id, pwd);
                }else
                {
                    Toast.makeText(LoginActivity.this,"네트워크 연결 상태를 확인하세요",Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerButton = (TextView)findViewById(R.id.register);//회원가입 버튼
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });

    }



    class LoginTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        LoginTask(Context ctx)
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

            if(result.equals("1")) //login 성공
            {
                Basicinfo.name = id;
                //login 가능성 check
                LoginActivity.GetAuth g = new LoginActivity.GetAuth(LoginActivity.this);
                g.execute(id);

            }else
            {
              Toast.makeText(LoginActivity.this,"ID or 비밀번호를 다시 확인하세요!",Toast.LENGTH_SHORT).show();
            }

        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://" + Basicinfo.URL + "/login.php";

            if(type.equals("login"))
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

    class Wifi_State extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        Wifi_State(Context ctx)
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
            myJSON = result;
            checkWIFI();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://" + Basicinfo.URL + "/getwifi.php";

                try
                {

                    String id = params[0];
                    URL url =new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);

                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                    String post_data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8") ;

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

    public void checkWIFI()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myJSON);
            wifi_state = jsonObj.getJSONArray(TAG_RESULTS);
            String state;

            JSONObject c = wifi_state.getJSONObject(0);
            id = c.getString(TAG_ID);
            state = c.getString(TAG_STATE);


            if(state.equals("on"))
            {
                ConnectivityManager ma = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
                if(ma.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable())
                {
                    Intent book = new Intent(LoginActivity.this,BookMainActivity.class);
                    LoginActivity.this.startActivity(book);
                }
                else
                {
                    Toast.makeText(LoginActivity.this,"안됩니다.",Toast.LENGTH_SHORT).show();
                }


            }else
            {
                //글쓰기 possibility
                CheckPro p = new CheckPro(LoginActivity.this);
                p.execute(Basicinfo.name);
                Intent book = new Intent(LoginActivity.this,BookMainActivity.class);
                LoginActivity.this.startActivity(book);
            }

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    class GetAuth extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        GetAuth(Context ctx)
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
            myAJSON = result;
            checkAuth();
        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://" + Basicinfo.URL + "/getAuth.php";

            try
            {

                String id = params[0];
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("id","UTF-8") + "=" + URLEncoder.encode(id,"UTF-8") ;

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

    public void checkAuth()
    {
        //test---------------------------------------------------------------------------------------

        try{
            JSONObject jsonObj = new JSONObject(myAJSON);
            auth_state = jsonObj.getJSONArray(TAG_RESULTS);


            JSONObject c = auth_state.getJSONObject(0);
            String id = c.getString(TAG_AID);
            String login = c.getString(TAG_ALogin);
            String writing = c.getString(TAG_AWriting);

            if(login.equals("off")){
                Toast.makeText(LoginActivity.this,"로그인이 차단되었습니다.",Toast.LENGTH_SHORT).show();
            }else //login이 가능 - > wifi check
            {
                LoginActivity.Wifi_State s = new LoginActivity.Wifi_State(LoginActivity.this); //wifi check
                s.execute(Basicinfo.name);
            }


        }catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                final Dialog dig = new Dialog(LoginActivity.this);
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

    private boolean internet_check()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isMobileAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isAvailable();
        boolean isMobileConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ( (isWifiAvailable && isWifiConnect) || (isMobileAvailable && isMobileConnect))
                   return true;

        return false;
    }

    private boolean WIFI_check()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService (Context.CONNECTIVITY_SERVICE);

        boolean isWifiAvailable = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isAvailable();
        boolean isWifiConnect = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if ( (isWifiAvailable && isWifiConnect))
            return true;

        return false;
    }

    // 글쓰기 여부
    class CheckPro extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        CheckPro(Context ctx)
        {
            context=ctx;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {


            myWSON = result;
            try
            {
                JSONObject jsonObj = new JSONObject(myWSON);
                data = jsonObj.getJSONArray(TAG_RESULTS);

                for(int i=0; i< data.length(); i++)
                {
                    JSONObject c = data.getJSONObject(i);
                    String cid = c.getString(TAG_AID);
                    String clogin = c.getString(TAG_ALogin);
                    String cwriting = c.getString(TAG_AWriting);

                    Basicinfo.po_writing = cwriting;

                }

            Toast.makeText(LoginActivity.this,Basicinfo.po_writing,Toast.LENGTH_SHORT).show();



            }catch (JSONException e)
            {
                e.printStackTrace();
            }


        }


        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {


            String login_url ="http://" + Basicinfo.URL + "/writepossibility.php";

            try
            {

                String id = params[0];
                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

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
