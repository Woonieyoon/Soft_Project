package myhome.bookknu;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungw on 2017-08-11.
 */

//자신이 쓴글 눌렀을때
public class Book_MywriteClick extends AppCompatActivity {

    private ListView mycommentListView1;
    private CommentListAdapter myadapter1;
    private List<Comment> mynoticeList1;
    private Button myinsert1;
    private Dialog myMainDialog1;
    View mylayout;
    AlertDialog myad;

    TextView mydate1;
    TextView myname1;
    TextView mytitle1;
    TextView mycontent1;

    Button delete,rewrite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_myclickactivity);

        delete = (Button)findViewById(R.id.writedelete);
        rewrite = (Button)findViewById(R.id.rewrite);

        mydate1 = (TextView)findViewById(R.id.showdate1);
        myname1 = (TextView)findViewById(R.id.showname1);
        mytitle1 = (TextView)findViewById(R.id.showtitle1);
        mycontent1 = (TextView)findViewById(R.id.showcontent1);

        Intent intent = getIntent();
        mydate1.setText(intent.getExtras().getString("date"));
        myname1.setText(intent.getExtras().getString("name"));
        mytitle1.setText(intent.getExtras().getString("title"));
        mycontent1.setText(intent.getExtras().getString("content"));

        mycommentListView1 = (ListView)findViewById(R.id.commentlist1);
        mynoticeList1 =  new ArrayList<Comment>();

        mynoticeList1.add(new Comment("  굿!","홍길동","2017-06-20"));
        mynoticeList1.add(new Comment("  감사합니다","홍길동","2017-06-05"));
        mynoticeList1.add(new Comment("  감사합니다","홍길동","2017-06-04"));

        myadapter1 = new CommentListAdapter(getApplicationContext(),mynoticeList1);
        mycommentListView1.setAdapter(myadapter1);

        //dialog만들기




        //댓글쓰기
        myinsert1 = (Button)findViewById(R.id.insertcomment1);
        myinsert1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context ctx = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(LAYOUT_INFLATER_SERVICE);
                mylayout = inflater.inflate(R.layout.comment_dialog,null);
                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(Book_MywriteClick.this);

                aDlgb.setView(mylayout);
                myad = aDlgb.create();
                myad.show();

                //저장
                Button save = (Button)mylayout.findViewById(R.id.commentsave); //저장버튼누를시
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText save = (EditText)mylayout.findViewById(R.id.commentedit);
                        String str = save.getText().toString();
                        mynoticeList1.add(new Comment("  "+str,"sungwon126","2017-06-20"));
                        myad.dismiss();
                    }
                });

                //취소
                Button cancel = (Button)mylayout.findViewById(R.id.commentcancel); //저장버튼누를시
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myad.dismiss();

                    }
                });

            }
        });

        //삭제 버튼 눌렸을때
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Book_MywriteClick.DeleteTask st = new Book_MywriteClick.DeleteTask(Book_MywriteClick.this);
                st.execute(myname1.getText().toString(),mytitle1.getText().toString(),mycontent1.getText().toString());

            }
        });

        //수정버튼 눌렀을때
        rewrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String retitle = mytitle1.getText().toString();    //제목
                String recontent = mycontent1.getText().toString();  //내용

                Intent re_write = new Intent(Book_MywriteClick.this,Book_MywriteRe.class);
                re_write.putExtra("title",retitle);
                re_write.putExtra("content",recontent);
                Book_MywriteClick.this.startActivity(re_write);

            }
        });


    }


    class DeleteTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        DeleteTask(Context ctx)
        {
            context=ctx;
        }


        @Override
        protected void onPreExecute() {
            alertDialog  = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Delete status");
        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("1"))
            {
                Toast.makeText(Book_MywriteClick.this,"성공",Toast.LENGTH_SHORT).show();
            }else
            {
                //result = "다시 입력하세요";
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                startActivity(new Intent(this,Book_Mywrite.class));
        }

        return super.onKeyDown(keyCode, event);
    }




}
