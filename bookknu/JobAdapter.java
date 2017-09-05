package myhome.bookknu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Date;
import java.util.List;

/**
 * Created by sungw on 2017-08-24.
 */

//취업 어댑터
public class JobAdapter  extends RecyclerView.Adapter<JobAdapter.ViewHolder> {

    private List<JobListItem> listItems;
    private Context context;

    private View layout;
    private AlertDialog ad;

    public JobAdapter(List<JobListItem> listItems, Context context)
    {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_listitem,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JobListItem listItem = listItems.get(position);
        holder.j_title.setText(listItem.getJ_title());
        holder.j_content.setText(listItem.getJ_content());
        holder.j_date.setText(listItem.getJ_date());
        holder.j_id.setText(listItem.getJ_id());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CharSequence info[] = new CharSequence[] {"Message 보내기", "글읽기"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("선택하세요!!");
                builder.setIcon(R.drawable.knu);

                builder.setItems(info, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch(which)
                        {
                            case 0: //Message 보내기
                                Context ctx = context;
                                LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                layout = inflater.inflate(R.layout.message_dialog,null);
                                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(context);
                                aDlgb.setView(layout);
                                ad = aDlgb.create();
                                ad.show();

                                //저장
                                Button save = (Button)layout.findViewById(R.id.message_save); //저장버튼누를시
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText save = (EditText)layout.findViewById(R.id.message_edit);

                                        String send = Basicinfo.name;
                                        String receive = listItem.getJ_id();
                                        String message = save.getText().toString();

                                        JobAdapter.MessageTask b = new JobAdapter.MessageTask(context);
                                        b.execute(send,receive,message);
                                        ad.dismiss();

                                    }
                                });


                                //취소
                                Button cancel = (Button)layout.findViewById(R.id.message_cancel); //저장버튼누를시
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ad.dismiss();
                                    }
                                });

                                break;

                            case 1: //글읽기
                                String jdate =listItem.getJ_date();
                                String jname = listItem.getJ_id();
                                String jtitle = listItem.getJ_title();
                                String jcontent =  listItem.getJ_content();
                                Intent read = new Intent(context,JobReadingActivity.class);
                                read.putExtra("date",jdate);
                                read.putExtra("name",jname);
                                read.putExtra("title",jtitle);
                                read.putExtra("content",jcontent);
                                context.startActivity(read);
                                break;

                        }
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView j_title;
        public TextView j_content;
        public TextView j_date;
        public TextView j_id;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            j_title = (TextView)itemView.findViewById(R.id.j_title);
            j_content = (TextView)itemView.findViewById(R.id.j_content);
            j_date = (TextView)itemView.findViewById(R.id.j_date);
            j_id = (TextView)itemView.findViewById(R.id.j_id);
            linearLayout =(LinearLayout)itemView.findViewById(R.id.job_linearlayout);
        }
    }



    class MessageTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;
        MessageTask(Context ctx)
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
                Toast.makeText(context,"성공",Toast.LENGTH_SHORT).show();
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

            String login_url = "http://" + Basicinfo.URL + "/insertmessage.php";

            try
            {
                String send = params[0];
                String receive = params[1];
                String message = params[2];

                URL url =new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //httpURLConnection.setConnectTimeout(8000);
                //httpURLConnection.setReadTimeout(8000);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

                String post_data = URLEncoder.encode("send","UTF-8") + "=" + URLEncoder.encode(send,"UTF-8") + "&"
                        + URLEncoder.encode("receive","UTF-8") + "=" + URLEncoder.encode(receive,"UTF-8")  + "&"
                        + URLEncoder.encode("message","UTF-8") + "=" + URLEncoder.encode(message,"UTF-8")  + "&"
                        + URLEncoder.encode("date","UTF-8") + "=" + URLEncoder.encode(date,"UTF-8");
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
