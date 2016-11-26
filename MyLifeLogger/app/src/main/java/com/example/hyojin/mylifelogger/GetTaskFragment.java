package com.example.hyojin.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GetTaskFragment extends Fragment {
    int iYear;
    int iMonth;
    int iDate;

    public GetTaskFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_get_task, container, false);

        Calendar today = Calendar.getInstance();
        iYear = today.get(Calendar.YEAR);
        iMonth = today.get(Calendar.MONTH) + 1;
        iDate = today.get(Calendar.DAY_OF_MONTH);

        TextView textViewCalenderTask = (TextView) view.findViewById(R.id.text_CalenderTask);
        textViewCalenderTask.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        iMonth -= 1;

        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerTask);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btnLocationTask = (Button) view.findViewById(R.id.btn_location_Task);
        btnLocationTask.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startLocationService();
            }
        });

        textViewCalenderTask.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;

                        TextView textViewCalenderTaskTemp = (TextView) view.findViewById(R.id.text_CalenderTask);
                        textViewCalenderTaskTemp.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");

                        iYear = year;
                        iMonth = monthOfYear - 1;
                        iDate = dayOfMonth;
                    }
                };

                new DatePickerDialog(view.getContext(), dateSetListener, iYear, iMonth, iDate).show();
            }
        });

        return view ;
    }


    private void startLocationService() {
        // 위치 관리자 객체 참조
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);

            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);

        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        Toast.makeText(getActivity().getApplicationContext(), "위치 확인이 시작되었습니다. 로그를 확인하세요." + gpsListener.latitude + "latitude", Toast.LENGTH_SHORT).show();
    }
}