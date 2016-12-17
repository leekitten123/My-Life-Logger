package com.example.hyojin.mylifelogger;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

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

        int numTask = 0 ;
        int numEvent = 0 ;

        for (int i = 0 ; i < taskDB.getSizeDB() - 1 ; i++) {
            if (taskDB.getTimeToDB(i) >= taskDB.getTimeToDB(i + 1) ||  (i == taskDB.getSizeDB() - 2)) {
                if (i == taskDB.getSizeDB() - 2) {
                    i++ ;
                }

                numTask++ ;

                LinearLayout linearLayout = new LinearLayout(getActivity()) ;
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                TextView textView1 = new TextView(getActivity());
                textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView1.setText(numTask + ". " + taskDB.getWhatDoToDB(i));
                textView1.setTextSize(20);

                TextView textView2 = new TextView(getActivity());
                textView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView2.setText(taskDB.getSnippet(i, true));
                textView2.setTextSize(15);

                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                Bitmap bm = null;

                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), taskDB.getURIToDB(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(bm);

                linearLayout.addView(textView1);
                linearLayout.addView(textView2);
                linearLayout.addView(imageView);

                scrollLinearTask.addView(linearLayout);
            }
        }

        for (int i = 0 ; i < eventDB.getSizeDB() ; i++) {
            if (eventDB.getTimeToDB(i) == 0) {
                numEvent++ ;

                LinearLayout linearLayout = new LinearLayout(getActivity()) ;
                linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                TextView textView1 = new TextView(getActivity());
                textView1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView1.setText(numEvent + ". " + eventDB.getWhatDoToDB(i));
                textView1.setTextSize(20);

                TextView textView2 = new TextView(getActivity());
                textView2.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView2.setText(eventDB.getSnippet(i, false));
                textView2.setTextSize(15);

                ImageView imageView = new ImageView(getActivity());
                imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

                Bitmap bm = null;

                try {
                    bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), eventDB.getURIToDB(i));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageView.setImageBitmap(bm);

                linearLayout.addView(textView1);
                linearLayout.addView(textView2);
                linearLayout.addView(imageView);

                scrollLinearEvent.addView(linearLayout);
            }
        }

        return view ;
    }
}
