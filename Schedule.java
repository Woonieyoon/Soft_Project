package com.example.sungw.knuprojcet;

import android.content.Context;
import android.widget.TextView;

/**
 * Created by sungw on 2017-05-20.
 */

public class Schedule {

    private String monday[] = new String[27];
    private String tuesday[] = new String[27];
    private String wednesday[] = new String[27];
    private String thursday[] = new String[27];
    private String friday[] = new String[27];

    public Schedule()
    {
        for(int i=0;i<18;i++)
        {
            monday[i]="";
            tuesday[i]="";
            wednesday[i]="";
            thursday[i]="";
            friday[i]="";
        }
    }

    public void addSchedule(int num, String a, String b , String c, String d, String e )
    {
        monday[num]=a;
        tuesday[num]=b;
        wednesday[num]=c;
        thursday[num]=d;
        friday[num]=e;
    }


    public void setting(AutoResizeTextView[] monday, AutoResizeTextView[] tuesday, AutoResizeTextView[] wednesday, AutoResizeTextView[] thursday, AutoResizeTextView[] friday, Context context)
    {
        int maxLength=0;
        String maxString ="";

        for(int i=0;i<18;i++)
        {
            if(this.monday[i].length()>maxLength)
            {
                maxLength = this.monday[i].length();
                maxString = this.monday[i];
            }

            if(this.tuesday[i].length()>maxLength)
            {
                maxLength = this.tuesday[i].length();
                maxString = this.tuesday[i];
            }

            if(this.wednesday[i].length()>maxLength)
            {
                maxLength = this.wednesday[i].length();
                maxString = this.wednesday[i];
            }

            if(this.thursday[i].length()>maxLength)
            {
                maxLength = this.thursday[i].length();
                maxString = this.thursday[i];
            }

            if(this.friday[i].length()>maxLength)
            {
                maxLength = this.friday[i].length();
                maxString = this.friday[i];
            }
        }

        for(int i=0;i<18;i++)
        {
            if (!this.monday[i].equals("")) {
                monday[i].setText(this.monday[i]);
                monday[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else
            {
                monday[i].setText(maxString);
            }

            if (!this.tuesday[i].equals("")) {
                tuesday[i].setText(this.tuesday[i]);
                tuesday[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else
            {
                tuesday[i].setText(maxString);
            }

            if (!this.wednesday[i].equals("")) {
                wednesday[i].setText(this.wednesday[i]);
                wednesday[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else
            {
                wednesday[i].setText(maxString);
            }

            if (!this.thursday[i].equals("")) {
               thursday[i].setText(this.thursday[i]);
               thursday[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else
            {
                thursday[i].setText(maxString);
            }


            if (!this.friday[i].equals("")) {
                friday[i].setText(this.friday[i]);
                friday[i].setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            }
            else
            {
                friday[i].setText(maxString);
            }

            monday[i].resizeText();
            tuesday[i].resizeText();
            wednesday[i].resizeText();
            thursday[i].resizeText();
            friday[i].resizeText();
        }
    }

}
