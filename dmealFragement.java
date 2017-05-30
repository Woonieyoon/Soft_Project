package com.example.sungw.knuprojcet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sungw on 2017-05-28.
 */

public class dmealFragement extends Fragment {


    String Url;
    String today;
    StringBuilder a;
    String menu,menu1,menu2;

    TextView tx1,tx2,tx3;
    int count=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dmeal, container, false);
        tx1 = (TextView)v.findViewById(R.id.textd1);
        tx2 = (TextView)v.findViewById(R.id.textd2);
        tx3= (TextView)v.findViewById(R.id.textd3);
        new dmealFragement.BackgroudTask1().execute();

        return v;
    }

    public smealFragement newInstance(String text) {

        Url = text;
        smealFragement f = new smealFragement();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    public void getUrl(String text)
    {
        Url = text;
    }

    class BackgroudTask1 extends AsyncTask<Void, Void ,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Document doc = null;
            try {
                doc = Jsoup.parse(new URL(Url).openStream(),"UTF-8",Url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            today="";
            if(doc.select("issued").size() > 0)
            {
                Elements day = doc.select("issued");
                today = day.get(0).toString();
                today = today.replace("<issued>","");
                today = today.replace("</issued>","");

                Elements entrys = doc.select("entry");

                for(Element datas : entrys) {

                    String when = datas.attr("type").toString();
                    if (when.equals("breakfast")) {
                        when = "아침";

                    }else if(when.equals("breakfast_limited")) {
                        when = "아침특식";


                    }else if (when.equals("dinner")) {
                        when = "저녁";

                    }else if(when.equals("dinner_limited")){
                        when = "저녁특식";

                    }else if(when.equals("lunch")){
                        when = "점심";

                    }else if(when.equals("lunch_limited"))
                    {
                        when = "점심특식";

                    }
                    Elements data = datas.select("data");

                    if(count==0) {
                        menu = data.get(0).toString();
                        menu = menu.replace("<data>&lt;![CDATA[", "");
                        menu = menu.replace("<data>", "");
                        menu = menu.replace("]]&gt;</data>", "");
                        menu = menu.replace("</data>", "");

                        menu = menu.replace("&lt;br /&gt; ", ", ");
                        menu = menu.replace("&lt;br /&gt;", "   ");
                        menu = menu.replace("&amp;", ", ");
                        menu = menu.replace("&lt;", "");
                        menu = menu.replace("&gt;", "");
                    }else if(count==1)
                    {
                        menu1 = data.get(0).toString();
                        menu1 = menu1.replace("<data>&lt;![CDATA[", "");
                        menu1 = menu1.replace("<data>", "");
                        menu1 = menu1.replace("]]&gt;</data>", "");
                        menu1 = menu1.replace("</data>", "");

                        menu1 = menu1.replace("&lt;br /&gt; ", ", ");
                        menu1 = menu1.replace("&lt;br /&gt;", "   ");
                        menu1 = menu1.replace("&amp;", ", ");
                        menu1 = menu1.replace("&lt;", "");
                        menu1 = menu1.replace("&gt;", "");
                    }else
                    {
                        menu2 = data.get(0).toString();
                        menu2 = menu2.replace("<data>&lt;![CDATA[", "");
                        menu2 = menu2.replace("<data>", "");
                        menu2 = menu2.replace("]]&gt;</data>", "");
                        menu2 = menu2.replace("</data>", "");

                        menu2 = menu2.replace("&lt;br /&gt; ", ", ");
                        menu2 = menu2.replace("&lt;br /&gt;", "   ");
                        menu2 = menu2.replace("&amp;", ", ");
                        menu2 = menu2.replace("&lt;", "");
                        menu2 = menu2.replace("&gt;", "");

                    }

                    count++;

                }

            }
            else
            {
            }

            return null;

        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            tx1.setText(menu);
            tx2.setText(menu1);
            tx3.setText(menu2);

        }
    }

}
