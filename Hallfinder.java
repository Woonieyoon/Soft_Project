package kr.co.company.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

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

public class Hallfinder extends AppCompatActivity {
    Button button1,button2;
    final Context context = this;
    private String roomID;
    private String roomPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hallfinder);
        button1 = (Button)findViewById(R.id.button1); //표 던지기
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //다음 페이지로 화면을 전환.
                //화면을 전환할때 필요한 클래스 인텐트
                Intent intent = new Intent(Hallfinder.this, SubActivity.class);

                //화면 전환하기
                startActivity(intent);
            }
        });

        button2 = (Button)findViewById(R.id.button2); //방만들기
        button2.setOnClickListener(new View.OnClickListener(){ /////////////////////////////////////////////////////////////////심사의장 눌렀을때
            @Override
            public void onClick(View v) {
                LayoutInflater inflater=getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.dialog_add, null);
                //멤버의 세부내역 입력 Dialog 생성 및 보이기
                AlertDialog.Builder bulider= new AlertDialog.Builder(context); //AlertDialog.Builder 객체 생성
                bulider.setTitle("심사의 장"); //Dialog 제목
                bulider.setView(dialogView); //위에서 inflater가 만든 dialogView 객체 세팅 (Customize)/////////////////////////////다이얼로그 띄우기

                final EditText idText = (EditText)dialogView.findViewById(R.id.idText);
                final EditText passwordText = (EditText)dialogView.findViewById(R.id.passwordText);

                bulider.setPositiveButton("완료", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub

                                roomID = idText.getText().toString();
                                roomPassword = passwordText.getText().toString();

                                if(roomID.equals("") || roomPassword.equals("")){
                                    //토스트
                                    Toast.makeText(getApplicationContext(), "빈칸없이 입력하세요", Toast.LENGTH_LONG).show();
                                    return ;

                                }

                                Hallfinder.InsertRegister h = new Hallfinder.InsertRegister();
                                h.execute(roomID,roomPassword,"11");


                            }
                        });
                bulider.show();
            }
        });
    }


    class InsertRegister extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("1"))
            {
                Toast.makeText(Hallfinder.this,"성공",Toast.LENGTH_SHORT).show();

            }else
            {
                Toast.makeText(Hallfinder.this,"ㅅㅍ",Toast.LENGTH_SHORT).show();

            }
        }

        @Override
        protected String doInBackground(String... params) {
            String login_url ="http://172.30.1.15/testregister.php";   //왐프서버(무료 php, mysql 지원해주는 서버)

            try
            {

                String roid = params[0];
                String ropwd = params[1];
                String rodate = params[2];

                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("roid","UTF-8") + "=" + URLEncoder.encode(roid,"UTF-8") + "&"
                        + URLEncoder.encode("ropwd","UTF-8") + "=" + URLEncoder.encode(ropwd,"UTF-8") + "&"
                        + URLEncoder.encode("rodate","UTF-8") + "=" + URLEncoder.encode(rodate,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

//전송 php로 변수값 던지기
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
