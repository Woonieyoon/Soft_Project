package com.example.sungw.knuprojcet;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Intro extends Activity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    Thread splashTread;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);

        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(Intro.this,
                            MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    Intro.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    Intro.this.finish();
                }

            }
        };
        splashTread.start();

    }

}
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//
///**
// * Created by sungw on 2017-05-06.
// */
//
//public class Intro extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.intro);
//
//
//        new Thread()
//        {
//            public void run()
//            {
//                try
//                {
//                     Thread.sleep(2000);
//                }catch(InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//
//                finish();
//                Intent i = new Intent(Intro.this,MainActivity.class);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//            }
//
//
//
//
//        }.start();
//    }
//
//
//
//}
