package myhome.bookknu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-10-12.
 */

public class ReportAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private List<Report> reportList;

    public ReportAdapter(Context context, List<Report> reportList) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.reportList = reportList;
    }

    @Override
    public int getCount() {
        return reportList.size();
    }

    @Override
    public Object getItem(int i) {
        return reportList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        int res = 0;

        res = reportList.get(i).getaType();//현재 위치의 Type을 조사해보고

            switch(res){
                case 0://0이면 textedit                   r
                    convertView = mInflater.inflate(R.layout.report_sublist,parent,false);
                    break;
                case 1://1이면 btnicon으로 R.layout값을 넣어주고
                    //res = R.layout.btnicon;
                    convertView = mInflater.inflate(R.layout.report_onsublist,parent,false);
                    break;
            }



        res =  reportList.get(i).getaType();;

        switch(res) {
            case 0:
                TextView sname= (TextView)convertView.findViewById(R.id.swho); //신고인
                TextView wname= (TextView)convertView.findViewById(R.id.who); //작성자
                TextView title= (TextView)convertView.findViewById(R.id.report_title);//제목
                TextView date=  (TextView)convertView.findViewById(R.id.report_date); //글쓴 날짜


                sname.setText(reportList.get(i).getSname());
                wname.setText(reportList.get(i).getWname());
                title.setText(reportList.get(i).getTitle());
                date.setText(reportList.get(i).getDate());
                break;

            case 1:
                TextView ssname= (TextView)convertView.findViewById(R.id.on_swho); //신고인
                TextView wwname= (TextView)convertView.findViewById(R.id.on_who); //작성자
                TextView ttitle= (TextView)convertView.findViewById(R.id.on_report_title);//제목
                TextView ddate=  (TextView)convertView.findViewById(R.id.on_report_date); //글쓴 날짜

                ssname.setText(reportList.get(i).getSname());
                wwname.setText(reportList.get(i).getWname());
                ttitle.setText(reportList.get(i).getTitle());
                ddate.setText(reportList.get(i).getDate());
                break;


        }
//                View v = View.inflate(context,R.layout.report_sublist,null); //inflate 객체화 시킴
//        TextView sname= (TextView)v.findViewById(R.id.swho); //신고인
//        TextView wname= (TextView)v.findViewById(R.id.who); //작성자
//        TextView title= (TextView)v.findViewById(R.id.report_title);//제목
//        TextView date=  (TextView)v.findViewById(R.id.report_date); //글쓴 날짜
//
//
//        sname.setText(reportList.get(i).getSname());
//        wname.setText(reportList.get(i).getWname());
//        title.setText(reportList.get(i).getTitle());
//        date.setText(reportList.get(i).getDate());


        return convertView;

    }
}