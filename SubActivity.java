package kr.co.company.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeonsoohwan on 2017. 9. 7..
 */


//표던지기 클릭 activity
public class SubActivity extends AppCompatActivity {

    private ListView subListView;
    private SubListAdapter adapter;
    private List<Sub> subList;
    private String roomid = "roomid";
    private String roompwd = "roompwd";
    private String roomdate = "roomdate";

    private String jon;
    private JSONArray js=null;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.serb);

        subListView =(ListView)findViewById(R.id.subListView);
        subList = new ArrayList<Sub>();
        adapter = new SubListAdapter(getApplicationContext(), subList);
        subListView.setAdapter(adapter);

        subListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SubActivity.this, VoteActivity.class);
                intent.putExtra("highrush", subListView.getItemAtPosition(position).toString());
                startActivity(intent);
            }
        });
        new BackgroundTask().execute();
    }

    class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;
        @Override
        protected void onPreExecute(){
        }

        @Override
        protected String doInBackground(Void... params) {

            String uri = "http://172.30.1.15/votetest.php";
            BufferedReader bufferedReader = null;

            try{
                URL url = new URL(uri);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                StringBuilder sb = new StringBuilder();

                bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String json;
                while((json=bufferedReader.readLine())!=null)
                {
                    sb.append(json+"\n");
                }

                return sb.toString().trim();




            }catch(Exception e)
            {
                return null;
            }

        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result){
                    jon = result;
                    try{
                        JSONObject jsonObject = new JSONObject(jon);
                        js = jsonObject.getJSONArray("result");

                        for(int i=0;i<js.length();i++)
                        {
                            JSONObject c = js.getJSONObject(i);
                            String a=c.getString("roid");
                            String b=c.getString("ropwd");
                            String d=c.getString("rodate");

                            subList.add(new Sub(a,b));


                        }

                        subListView.setAdapter(adapter);


                    }catch(JSONException e)
                    {

                    }
        }
    }
}
