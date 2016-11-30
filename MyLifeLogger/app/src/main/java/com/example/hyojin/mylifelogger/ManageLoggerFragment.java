package com.example.hyojin.mylifelogger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ManageLoggerFragment extends Fragment {

    MyDataBase eventDB;
    MyDataBase taskDB;

    LinearLayout scrollLinearTask;
    LinearLayout scrollLinearEvent;

    public ManageLoggerFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_logger, container, false);

        eventDB = new MyDataBase(getContext(), "EventDataBase.db", null, 1);
        taskDB = new MyDataBase(getContext(), "TaskDataBase.db", null, 1);

        scrollLinearTask = (LinearLayout) view.findViewById(R.id.scroll_LinearTask);
        scrollLinearEvent = (LinearLayout) view.findViewById(R.id.scroll_LinearEvent);

        for (int i = 0 ; i < taskDB.getSizeDB() ; i++) {
            if (taskDB.getTimeToDB(i) == 0) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText("내용: " + taskDB.getWhatDoToDB(i) + " " + taskDB.getCategoryDateToDB(i));
                textView.setTextSize(15);

                scrollLinearTask.addView(textView);
            }
        }

        for (int i = 0 ; i < eventDB.getSizeDB() ; i++) {
            if (eventDB.getTimeToDB(i) == 0) {
                TextView textView = new TextView(getActivity());
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText("내용: " + eventDB.getWhatDoToDB(i) + " " + eventDB.getCategoryDateToDB(i));
                textView.setTextSize(15);

                scrollLinearEvent.addView(textView);
            }
        }

        return view ;
    }
}
