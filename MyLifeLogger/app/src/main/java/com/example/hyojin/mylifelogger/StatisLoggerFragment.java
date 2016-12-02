package com.example.hyojin.mylifelogger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;

public class StatisLoggerFragment extends Fragment {

    PieChart pieChart ;
    MyPieChart myPieChart ;

    MyDataBase eventDB;
    MyDataBase taskDB;

    int[] yData = {0, 0, 0, 0, 0, 0} ;

    public StatisLoggerFragment() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statis_logger, container, false);

        pieChart = (PieChart) view.findViewById(R.id.pie_Chart) ;
        myPieChart = new MyPieChart(pieChart) ;

        eventDB = new MyDataBase(getContext(), "EventDataBase.db", null, 1);
        taskDB = new MyDataBase(getContext(), "TaskDataBase.db", null, 1);

        Description desc = new Description();
        desc.setText("Category Stats");
        pieChart.setDescription(desc);

        getDataCategory(eventDB);
        getDataCategory(taskDB);

        myPieChart.setyData(yData);
        myPieChart.addData();

        return view ;
    }

    void getDataCategory(MyDataBase myDataBase) {
        for (int i = 0; i < myDataBase.getSizeDB(); i++) {
            if (myDataBase.getTimeToDB(i) == 0) {
                switch (myDataBase.getCategoryToDB(i)) {
                    case "게임":
                        yData[0]++; break;
                    case "식사":
                        yData[1]++; break;
                    case "이동":
                        yData[2]++; break;
                    case "공부":
                        yData[3]++; break;
                    case "일":
                        yData[4]++; break;
                    case "그외":
                        yData[5]++; break;
                }
            }
        }
    }
}
