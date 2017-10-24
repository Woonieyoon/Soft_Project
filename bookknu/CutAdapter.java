package myhome.bookknu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sungw on 2017-10-23.
 */

public class CutAdapter extends BaseAdapter {

    private Context context;
    LayoutInflater mInflater;
    private List<Cut> cutList;

    public CutAdapter(Context context, List<Cut> cutList) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context;
        this.cutList = cutList;
    }

    @Override
    public int getCount() {
        return cutList.size();
    }

    @Override
    public Object getItem(int i) {
        return cutList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        int res = 0;
        convertView = mInflater.inflate(R.layout.cut_sublist,parent,false);


        TextView name= (TextView)convertView.findViewById(R.id.cut_member); //신고인
        name.setText(cutList.get(i).getCname());

        return convertView;

    }
}
