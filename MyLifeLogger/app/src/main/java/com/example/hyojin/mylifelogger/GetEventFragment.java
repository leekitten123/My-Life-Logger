package com.example.hyojin.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    int iYear;
    int iMonth;
    int iDate;

    static Double latitude = 0.0 ;
    static Double longitude = 0.0 ;

    static int eventCategory = 0 ;

    MyDataBaseEvent eventDB ;

    public GetEventFragment() {}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_get_event, container, false);

        eventDB = new MyDataBaseEvent(getActivity()) ;

        Calendar today = Calendar.getInstance();
        iYear = today.get(Calendar.YEAR);
        iMonth = today.get(Calendar.MONTH) + 1;
        iDate = today.get(Calendar.DAY_OF_MONTH);

        TextView textViewCalenderEvent = (TextView) view.findViewById(R.id.text_CalenderEvent);
        textViewCalenderEvent.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        iMonth -= 1;

        /** 카테고리 를 받는 이벤트 리스너 **/
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerEvent);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventCategory = position ;
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** 위치 정보를 받는 이벤트 리스너 **/
        Button btnLocationEvent = (Button) view.findViewById(R.id.btn_location_event);
        btnLocationEvent.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startLocationService();

                TextView textEventLatitude = (TextView) view.findViewById(R.id.text_EventLatitude) ;
                TextView textEventLongitude = (TextView) view.findViewById(R.id.text_EventLongitude) ;
                textEventLatitude.setText("Latitude: " + latitude);
                textEventLongitude.setText("Lonitude: " + longitude);
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

        Button btnSaveEvent = (Button) view.findViewById(R.id.btn_SaveEvent) ;
        btnSaveEvent.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int realDateNum = (iYear * 10000) + (iMonth * 100) + 100 + iDate ;
                EditText editWhatDoEvent = (EditText) view.findViewById(R.id.whatDoEvent) ;

                eventDB.insertData(latitude, longitude, eventCategory, realDateNum, editWhatDoEvent.getText().toString());
                editWhatDoEvent.setText("");
            }
        });

        return view;
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

        latitude = gpsListener.latitude ;
        longitude = gpsListener.longitude ;

        Toast.makeText(getActivity().getApplicationContext(), "위치 확인이 되었습니다.", Toast.LENGTH_SHORT).show();
    }
}

