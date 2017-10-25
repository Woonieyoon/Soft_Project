package kr.co.company.myapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jeonsoohwan on 2017. 9. 17..
 */
public class SubListAdapter extends BaseAdapter {

    private Context context;
    private List<Sub> subList;

    public SubListAdapter(Context context, List<Sub> subList) {
        this.context = context;
        this.subList = subList;
    }

    @Override
    public int getCount() {
        return subList.size();
    }

    @Override
    public Object getItem(int i) {
        return subList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.sub, null);
        TextView idText = (TextView) v.findViewById(R.id.vote_title);
        TextView passwordText = (TextView) v.findViewById(R.id.vote_date);

        idText.setText(subList.get(i).getRoomID());
        passwordText.setText(subList.get(i).getRoomPassword());

        v.setTag(subList.get(i).getRoomID());
        return v;
    }
}