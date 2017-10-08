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
 * Created by sungw on 2017-09-17.
 */

public class ClubAdapter extends RecyclerView.Adapter<ClubAdapter.ViewHolder> {

    private List<ClubListItem> listItems;
    private Context context;

    private View layout;
    private AlertDialog ad;

    public ClubAdapter(List<ClubListItem> listItems, Context context)
    {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public ClubAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.club_listitem,parent,false);
        return new ClubAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ClubAdapter.ViewHolder holder, int position) {
        final ClubListItem listItem = listItems.get(position);
        holder.c_title.setText(listItem.getC_title());
        holder.c_content.setText(listItem.getC_content());
        holder.c_date.setText(listItem.getC_date());
        holder.c_id.setText(listItem.getC_id());

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
                                layout = inflater.inflate(R.layout.messageclub_dialog,null);
                                final AlertDialog.Builder aDlgb = new AlertDialog.Builder(context);
                                aDlgb.setView(layout);
                                ad = aDlgb.create();
                                ad.show();

                                //저장
                                Button save = (Button)layout.findViewById(R.id.clubmessage_save); //저장버튼누를시
                                save.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        EditText save = (EditText)layout.findViewById(R.id.clubmessage_edit);

                                        String send = Basicinfo.name;
                                        String receive = listItem.getC_id();
                                        String message = save.getText().toString();

                                        Toast.makeText(context,send,Toast.LENGTH_SHORT).show();

                                        ClubAdapter.MessageTask b = new ClubAdapter.MessageTask();
                                        b.execute(send,receive,message);


                                        ad.dismiss();
                                    }
                                });

                                //취소
                                Button cancel = (Button)layout.findViewById(R.id.clubmessage_cancel); //저장버튼누를시
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ad.dismiss();
                                    }
                                });

                                break;

                            case 1:
                                //글읽기
                                String cdate =listItem.getC_date();
                                String cname = listItem.getC_id();
                                String ctitle = listItem.getC_title();
                                String ccontent =  listItem.getC_content();
                                Intent read = new Intent(context,ClubReadingActivity.class);
                                read.putExtra("date",cdate);
                                read.putExtra("name",cname);
                                read.putExtra("title",ctitle);
                                read.putExtra("content",ccontent);
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
        public TextView c_title;
        public TextView c_content;
        public TextView c_date;
        public TextView c_id;
        public LinearLayout linearLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            c_title = (TextView)itemView.findViewById(R.id.c_title);
            c_content = (TextView)itemView.findViewById(R.id.c_content);
            c_date = (TextView)itemView.findViewById(R.id.c_date);
            c_id = (TextView)itemView.findViewById(R.id.c_id);
            linearLayout =(LinearLayout)itemView.findViewById(R.id.club_linearlayout);
        }
    }



    public class MessageTask extends AsyncTask<String, Void, String> {

        AlertDialog alertDialog;
        Context context;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String result) {



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