package com.example.sungw.knuprojcet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;

/**
 * Created by sungw on 2017-05-28.
 */

//상주 대구 식단표
public class MealActivity extends AppCompatActivity {

    String campus;
    ArrayAdapter adapter;
    Spinner sp;
    Button start;
    String Url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_bar);

        setContentView(R.layout.mealspinner);
        sp = (Spinner)findViewById(R.id.mealSpinner);
        RadioGroup mealgroup = (RadioGroup)findViewById(R.id.mealGroup);
        start = (Button)findViewById(R.id.search_button);

        mealgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int i) {

                RadioButton dbutton = (RadioButton)findViewById(i);
                campus = dbutton.getText().toString();

                if(campus.equals("대구"))
                {
                    adapter = ArrayAdapter.createFromResource(MealActivity.this,R.array.daegu, android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);

                }else if(campus.equals("상주"))
                {
                    adapter = ArrayAdapter.createFromResource(MealActivity.this,R.array.sangju, android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(adapter);
                }

            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(campus.equals("대구"))
                {
                    String name = sp.getSelectedItem().toString();
                    if(name.equals("BTL"))
                    {
                        dmealFragement s = new dmealFragement();
                        s.getUrl("http://dorm.knu.ac.kr/xml/food.php?get_mode=3"); //BLT

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction tr = fragmentManager.beginTransaction();
                        tr.replace(R.id.fragment,s);
                        tr.commit();

                    }
                }else if(campus.equals("상주"))
                {
                    String name = sp.getSelectedItem().toString();
                    if(name.equals("기숙사"))
                    {
                            smealFragement s = new smealFragement();
                            s.getUrl("http://dorm.knu.ac.kr/xml/food.php?get_mode=1");

                            FragmentManager fragmentManager = getSupportFragmentManager();
                            FragmentTransaction tr = fragmentManager.beginTransaction();
                            tr.replace(R.id.fragment,s);
                            tr.commit();


                    }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.memo:
                startActivity(new Intent(this, MemoActivity.class));
                return true;
            case R.id.board:
                startActivity(new Intent(this, boardActivity.class));
                return true;
            case R.id.schedule:
                startActivity(new Intent(this, ScheduleActivity.class));
                return true;
            case R.id.meal:
                startActivity(new Intent(this, MealActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}


