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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class GetEventFragment extends Fragment {
    /** 오늘 년, 월, 일 **/
    int iYear = Calendar.getInstance().get(Calendar.YEAR) ;
    int iMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int iDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;

    /** 위도, 경도 **/
    static Double latitude = 0.0 ;
    static Double longitude = 0.0 ;

    /** 위치정보를 보여주는 뷰 **/
    TextView textEventLatitude ;
    TextView textEventLongitude ;

    /** Spinner에서 가르키는 항목 **/
    static int eventCategory = 0 ;

    /** 이벤트를 저장하는 데이터베이스 **/
    MyDataBase eventDB ;

    /** 기본 생성자 **/
    public GetEventFragment() {}

    /** GetEventFragment 생성 **/
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_get_event, container, false);

        eventDB = new MyDataBase(getActivity(), "EventDataBase.db", null, 1);

        /** 초기 달력 출력 **/
        TextView textViewCalenderEvent = (TextView) view.findViewById(R.id.text_CalenderEvent);
        textViewCalenderEvent.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        iMonth -= 1;

        /** 카테고리 를 받는 이벤트 리스너 **/
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerEvent);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventCategory = position ;
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        /** 위치정보를 보여주는 뷰 **/
        textEventLatitude = (TextView) view.findViewById(R.id.text_EventLatitude) ;
        textEventLongitude = (TextView) view.findViewById(R.id.text_EventLongitude) ;

        /** 위치 정보를 받는 이벤트 리스너 **/
        Button btnLocationEvent = (Button) view.findViewById(R.id.btn_location_event);
        btnLocationEvent.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startLocationService();
            }
        });

        /** 날짜 정보를 받는 이벤트 리스너 **/
        textViewCalenderEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear += 1;

                        TextView textViewCalenderEventTemp = (TextView) view.findViewById(R.id.text_CalenderEvent);
                        textViewCalenderEventTemp.setText(year + "년 " + monthOfYear + "월 " + dayOfMonth + "일");

                        iYear = year;
                        iMonth = monthOfYear - 1;
                        iDate = dayOfMonth;
                    }
                };

                new DatePickerDialog(view.getContext(), dateSetListener, iYear, iMonth, iDate).show();
            }
        });

        /** DB에 값 저장하기 **/
        Button btnSaveEvent = (Button) view.findViewById(R.id.btn_SaveEvent) ;
        btnSaveEvent.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int realDateNum = (iYear * 10000) + (iMonth * 100) + 100 + iDate ;
                EditText editWhatDoEvent = (EditText) view.findViewById(R.id.whatDoEvent) ;

                eventDB.insertData(latitude, longitude, eventCategory, realDateNum, 0, editWhatDoEvent.getText().toString());
                editWhatDoEvent.setText("");
            }
        });

        return view;
    }

    /** 위치 정보를 가져오는 메소드 **/
    private void startLocationService() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        latitude = gpsListener.latitude ;
        longitude = gpsListener.longitude ;

        textEventLatitude.setText("Latitude: " + latitude);
        textEventLongitude.setText("Longitude: " + longitude);
    }
}

