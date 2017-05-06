package com.example.sungw.knuprojcet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by sungw on 2017-05-06.
 */

public class Intro extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intro);


        new Thread()
        {
            public void run()
            {
                try
                {
                     Thread.sleep(2000);
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }

                finish();
                Intent i = new Intent(Intro.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }




        }.start();
    }



}
