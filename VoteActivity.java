package kr.co.company.myapp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class VoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);
        FloatingActionButton fab;
        final Context context = this;
        fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show(); 밑에 완료되었습니다 뜨는거
                    switch (v.getId()) {
                        case R.id.fab:
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                            // 제목셋팅
                            alertDialogBuilder.setTitle("프로그램 종료");

                            // AlertDialog 셋팅
                            alertDialogBuilder
                                    .setMessage("프로그램을 종료할 것입니까?")
                                    .setCancelable(false)
                                    .setPositiveButton("종료",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 프로그램을 종료한다
                                                    VoteActivity.this.finish();
                                                }
                                            })
                                    .setNegativeButton("취소",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // 다이얼로그를 취소한다
                                                    dialog.cancel();
                                                }
                                            });

                            // 다이얼로그 생성
                            AlertDialog alertDialog = alertDialogBuilder.create();

                            // 다이얼로그 보여주기
                            alertDialog.show();
                            break;

                        default:
                            break;
                    }


            }
        });
    }
}
