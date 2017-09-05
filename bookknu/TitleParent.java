package myhome.bookknu;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.List;
import java.util.UUID;

/**
 * Created by sungw on 2017-09-02.
 */

public class TitleParent implements ParentObject {

    private List<Object> mChildrenList;
    private UUID _id;
    private String title;

    public TitleParent(String title)
    {
        this.title = title;
        _id = UUID.randomUUID();
    }

    public UUID get_id(){
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public List<Object> getChildObjectList() {
        return mChildrenList;
    }

    @Override
    public void setChildObjectList(List<Object> list) {
            mChildrenList = list;
    }
}
