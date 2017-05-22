package com.example.sungw.knuprojcet;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScheduleFragment newInstance(String param1, String param2) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private String htmlTestUrl = "https://my.knu.ac.kr/stpo/comm/support/loginPortal/login.action?redirUrl=%2Fstpo%2Fstpo%2Fmain%2Fmain.action&menuParam=901";
    private AutoResizeTextView monday[] = new AutoResizeTextView[18];
    private AutoResizeTextView  tuesday[] = new AutoResizeTextView [18];
    private AutoResizeTextView  wednesday[] = new AutoResizeTextView [18];
    private AutoResizeTextView  thursday[] = new AutoResizeTextView [18];
    private AutoResizeTextView  friday[] = new AutoResizeTextView [18];
    private Schedule schedule = new Schedule();
    private int row_count;

    Document doc1;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        monday[0]=(AutoResizeTextView ) getView().findViewById(R.id.monday0);
        monday[1]=(AutoResizeTextView ) getView().findViewById(R.id.monday1);
        monday[2]=(AutoResizeTextView ) getView().findViewById(R.id.monday2);
        monday[3]=(AutoResizeTextView ) getView().findViewById(R.id.monday3);
        monday[4]=(AutoResizeTextView ) getView().findViewById(R.id.monday4);
        monday[5]=(AutoResizeTextView ) getView().findViewById(R.id.monday5);
        monday[6]=(AutoResizeTextView ) getView().findViewById(R.id.monday6);
        monday[7]=(AutoResizeTextView ) getView().findViewById(R.id.monday7);
        monday[8]=(AutoResizeTextView ) getView().findViewById(R.id.monday8);
        monday[9]=(AutoResizeTextView ) getView().findViewById(R.id.monday9);
        monday[10]=(AutoResizeTextView ) getView().findViewById(R.id.monday10);
        monday[11]=(AutoResizeTextView ) getView().findViewById(R.id.monday11);
        monday[12]=(AutoResizeTextView ) getView().findViewById(R.id.monday12);
        monday[13]=(AutoResizeTextView ) getView().findViewById(R.id.monday13);
        monday[14]=(AutoResizeTextView ) getView().findViewById(R.id.monday14);
        monday[15]=(AutoResizeTextView ) getView().findViewById(R.id.monday15);
        monday[16]=(AutoResizeTextView ) getView().findViewById(R.id.monday16);
        monday[17]=(AutoResizeTextView ) getView().findViewById(R.id.monday17);

        tuesday[0]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday0);
        tuesday[1]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday1);
        tuesday[2]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday2);
        tuesday[3]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday3);
        tuesday[4]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday4);
        tuesday[5]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday5);
        tuesday[6]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday6);
        tuesday[7]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday7);
        tuesday[8]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday8);
        tuesday[9]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday9);
        tuesday[10]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday10);
        tuesday[11]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday11);
        tuesday[12]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday12);
        tuesday[13]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday13);
        tuesday[14]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday14);
        tuesday[15]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday15);
        tuesday[16]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday16);
        tuesday[17]=(AutoResizeTextView ) getView().findViewById(R.id.tuesday17);


        wednesday[0]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday0);
        wednesday[1]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday1);
        wednesday[2]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday2);
        wednesday[3]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday3);
        wednesday[4]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday4);
        wednesday[5]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday5);
        wednesday[6]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday6);
        wednesday[7]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday7);
        wednesday[8]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday8);
        wednesday[9]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday9);
        wednesday[10]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday10);
        wednesday[11]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday11);
        wednesday[12]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday12);
        wednesday[13]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday13);
        wednesday[14]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday14);
        wednesday[15]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday15);
        wednesday[16]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday16);
        wednesday[17]=(AutoResizeTextView ) getView().findViewById(R.id.wednesday17);


        thursday[0]=(AutoResizeTextView ) getView().findViewById(R.id.thursday0);
        thursday[1]=(AutoResizeTextView ) getView().findViewById(R.id.thursday1);
        thursday[2]=(AutoResizeTextView ) getView().findViewById(R.id.thursday2);
        thursday[3]=(AutoResizeTextView ) getView().findViewById(R.id.thursday3);
        thursday[4]=(AutoResizeTextView ) getView().findViewById(R.id.thursday4);
        thursday[5]=(AutoResizeTextView ) getView().findViewById(R.id.thursday5);
        thursday[6]=(AutoResizeTextView ) getView().findViewById(R.id.thursday6);
        thursday[7]=(AutoResizeTextView ) getView().findViewById(R.id.thursday7);
        thursday[8]=(AutoResizeTextView ) getView().findViewById(R.id.thursday8);
        thursday[9]=(AutoResizeTextView ) getView().findViewById(R.id.thursday9);
        thursday[10]=(AutoResizeTextView ) getView().findViewById(R.id.thursday10);
        thursday[11]=(AutoResizeTextView ) getView().findViewById(R.id.thursday11);
        thursday[12]=(AutoResizeTextView ) getView().findViewById(R.id.thursday12);
        thursday[13]=(AutoResizeTextView ) getView().findViewById(R.id.thursday13);
        thursday[14]=(AutoResizeTextView ) getView().findViewById(R.id.thursday14);
        thursday[15]=(AutoResizeTextView ) getView().findViewById(R.id.thursday15);
        thursday[16]=(AutoResizeTextView ) getView().findViewById(R.id.thursday16);
        thursday[17]=(AutoResizeTextView ) getView().findViewById(R.id.thursday17);


        friday[0]=(AutoResizeTextView ) getView().findViewById(R.id.friday0);
        friday[1]=(AutoResizeTextView ) getView().findViewById(R.id.friday1);
        friday[2]=(AutoResizeTextView ) getView().findViewById(R.id.friday2);
        friday[3]=(AutoResizeTextView ) getView().findViewById(R.id.friday3);
        friday[4]=(AutoResizeTextView ) getView().findViewById(R.id.friday4);
        friday[5]=(AutoResizeTextView ) getView().findViewById(R.id.friday5);
        friday[6]=(AutoResizeTextView ) getView().findViewById(R.id.friday6);
        friday[7]=(AutoResizeTextView ) getView().findViewById(R.id.friday7);
        friday[8]=(AutoResizeTextView ) getView().findViewById(R.id.friday8);
        friday[9]=(AutoResizeTextView ) getView().findViewById(R.id.friday9);
        friday[10]=(AutoResizeTextView ) getView().findViewById(R.id.friday10);
        friday[11]=(AutoResizeTextView ) getView().findViewById(R.id.friday11);
        friday[12]=(AutoResizeTextView ) getView().findViewById(R.id.friday12);
        friday[13]=(AutoResizeTextView ) getView().findViewById(R.id.friday13);
        friday[14]=(AutoResizeTextView ) getView().findViewById(R.id.friday14);
        friday[15]=(AutoResizeTextView ) getView().findViewById(R.id.friday15);
        friday[16]=(AutoResizeTextView ) getView().findViewById(R.id.friday16);
        friday[17]=(AutoResizeTextView ) getView().findViewById(R.id.friday17);

        new BackgroudTask().execute();

    }

    class BackgroudTask extends AsyncTask<Void, Void ,Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {

                Connection.Response res = Jsoup.connect(htmlTestUrl)
                        .data("user.usr_id", "sungwon126")
                        .data("user.passwd", "yesterday1!")
                        .method(Connection.Method.POST)
                        .execute();
                Map<String, String> loginCookies = res.cookies();

                 doc1 = Jsoup.connect("http://my.knu.ac.kr/stpo/stpo/cour/lectReqEnq/listLectReqEnqs.action")
                        .data("lectReqEnq.search_open_yr_trm", "20171")
                        .data("lectReqEnq.search_stu_nbr", "2012097050")
                        .data("stu_nbr", "2012097050")
                        .data("open_yr_trm", "20171")
                        .data("titleName", "시간표")
                        .cookies(loginCookies).get();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }


        @Override
        protected void onPostExecute(Void s) {
            super.onPostExecute(s);

            Element table = doc1.select("table.cour_table").first();
            Elements rows = table.select("tbody tr");

            row_count=0;
            for (Element rowElement : rows) {
                Elements tdElements = rowElement.getElementsByTag("td");

                String monCourse = tdElements.get(0).text();
                String tueCourse = tdElements.get(1).text();
                String wedCourse = tdElements.get(2).text();
                String thuCourse = tdElements.get(3).text();
                String friCourse = tdElements.get(4).text();

                schedule.addSchedule(row_count, monCourse, tueCourse, wedCourse, thuCourse, friCourse);

                row_count++;

            }

            schedule.setting(monday, tuesday, wednesday, thursday, friday, getContext());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
