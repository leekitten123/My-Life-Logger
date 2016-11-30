package com.example.hyojin.mylifelogger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;

public class ManageLoggerFragment extends Fragment {

    final MyDataBase eventDB = new MyDataBase(getContext(), "EventDataBase.db", null, 1);
    final MyDataBase taskDB = new MyDataBase(getContext(), "TaskDataBase.db", null, 1);

//    ScrollView scrollViewTask = (ScrollView) getActivity().findViewById(R.id.scroll_Task);
    ScrollView scrollViewEvent;

    public ManageLoggerFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_logger, container, false);

        scrollViewEvent = (ScrollView) view.findViewById(R.id.scroll_Event);
/*
        for (int i = 0 ; i < taskDB.getSizeDB() ; i++) {

        }

        for (int i = 0 ; i < eventDB.getSizeDB() ; i++) {

        }*/

        EditText edit = new EditText(view.getContext());
        edit.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        scrollViewEvent.addView(edit);


        return view ;
    }
}
