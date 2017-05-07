package com.example.sungw.knuprojcet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

//회원가입 Spinner 부분 string값 불러오기
public class RegisterActivity extends AppCompatActivity {

    private ArrayAdapter adapter;
    //ArrayAdapter는 배열과 어댑터 뷰를 연결하는 클래스

    private Spinner spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = (Spinner) findViewById(R.id.majorSpinner);
        adapter = ArrayAdapter.createFromResource(this,R.array.major,android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



    }
}
