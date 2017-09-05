package myhome.bookknu;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

/**
 * Created by sungw on 2017-09-03.
 */

public class TitleParentViewHolder extends ParentViewHolder {

    public TextView _textView;
    public ImageButton _imageButton;

    public TitleParentViewHolder(View itemView)
    {
        super(itemView);
        _textView = (TextView)itemView.findViewById(R.id.parent_title);
        _imageButton = (ImageButton)itemView.findViewById(R.id.expandArrow);
    }

}
