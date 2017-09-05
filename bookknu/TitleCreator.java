package myhome.bookknu;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungw on 2017-09-03.
 */

public class TitleCreator {

    private String myJSON;
    private JSONArray peoples = null;


    static TitleCreator _titleCreator;
    static List<TitleParent> _titleParents;

    Context c;

    public TitleCreator(Context context)
    {

    }

    public static TitleCreator get(Context context)
    {
        if(_titleCreator == null)
            _titleCreator = new TitleCreator(context);


        return _titleCreator;
    }

    public List<TitleParent> getAll() {
        return _titleParents;
    }

}
