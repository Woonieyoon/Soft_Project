package com.example.sungw.knuprojcet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by sungw on 2017-06-05.
 */

public class BookInsertActivity extends AppCompatActivity {

    Button save,cancel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_write);
        save = (Button)findViewById(R.id.saveBtn);
        cancel = (Button)findViewById(R.id.cancelBtn);

        save.setOnClickListener(new View.OnClickListener() {  //저장 버튼
            @Override
            public void onClick(View v) {

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
