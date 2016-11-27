package com.example.hyojin.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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
    /** 오늘 년, 월, 일 **/
    int iYear = Calendar.getInstance().get(Calendar.YEAR) ;
    int iMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int iDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) ;

    /** 스톱워치에 사용할 변수 **/
    statusStopWatch cur_Status = statusStopWatch.Init ;
    int myCount = 1 ;
    long myBaseTime ;
    long myPauseTime ;

    /** 스톱워치에 사용할 뷰 **/
    TextView textPrintTime ;
    Button btnTimeStatus ;
    Button btnTimeRecord ;

    /** 스톱워치에 사용할 핸들러 **/
    Handler myTimer = new Handler(){
        public void handleMessage(Message msg){
            textPrintTime.setText(getTimeOut());
            myTimer.sendEmptyMessage(0);
        }
    };

    /** 기본 생성자 **/
    public GetTaskFragment() {}

    /** GetTaskFragment 생성 **/
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_get_task, container, false);

        /** 초기 달력 출력 **/
        TextView textViewCalenderTask = (TextView) view.findViewById(R.id.text_CalenderTask);
        textViewCalenderTask.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        iMonth -= 1;

        /** 카테고리 를 받는 이벤트 리스너 **/
        Spinner spinner = (Spinner)view.findViewById(R.id.spinnerTask);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /** 위치 정보를 받는 이벤트 리스너 **/
        Button btnLocationTask = (Button) view.findViewById(R.id.btn_location_Task);
        btnLocationTask.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                startLocationService();
            }
        });

        /** 스톱워치를 작동시키는 리스너 **/
        textPrintTime = (TextView) view.findViewById(R.id.text_PrintTime);
        btnTimeStatus = (Button) view.findViewById(R.id.btn_TimeStatus);
        btnTimeStatus.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                switch (cur_Status) {
                    case Init:
                        myBaseTime = SystemClock.elapsedRealtime();
                        System.out.println(myBaseTime);
                        myTimer.sendEmptyMessage(0);
                        btnTimeStatus.setText("멈춤");
                        //myBtnRec.setEnabled(true); //기록버튼 활성
                        cur_Status = statusStopWatch.Run;
                        break;

                    case Run:
                        myTimer.removeMessages(0);
                        myPauseTime = SystemClock.elapsedRealtime();
                        btnTimeStatus.setText("시작");
                        //myBtnRec.setText("리셋");
                        cur_Status = statusStopWatch.Pause;
                        break;

                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now - myPauseTime);
                        btnTimeStatus.setText("멈춤");
                        //myBtnRec.setText("기록");
                        cur_Status = statusStopWatch.Run;
                        break;
                }
            }
        });

        btnTimeRecord = (Button) view.findViewById(R.id.btn_TimeRecord) ;
        btnTimeRecord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                switch(cur_Status){
                    case Run:
                        String str = String.format("%d. %s\n",myCount,getTimeOut());
                        Toast.makeText(getActivity().getApplicationContext(), str, Toast.LENGTH_SHORT).show() ;
                        //myRec.setText(str);
                        myCount++ ;
                        break;

                    case Pause:
                        myTimer.removeMessages(0) ;
                        btnTimeStatus.setText("시작") ;
                        btnTimeRecord.setText("초기화") ;
                        textPrintTime.setText("00:00:00") ;
                        cur_Status = statusStopWatch.Init ;
                        myCount = 1 ;
                        //myRec.setText("");
                        btnTimeRecord.setEnabled(false);
                        break;
                }
            }
        });

        /** 날짜 정보를 받는 이벤트 리스너 **/
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

        return view;
    }

    /** 위치 정보를 가져오는 메서드 **/
    private void startLocationService() {
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        GPSListener gpsListener = new GPSListener();

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        Toast.makeText(getActivity().getApplicationContext(), "위치 확인", Toast.LENGTH_SHORT).show();
    }

    /** 스톱워치의 현재 정보 **/
    public enum statusStopWatch {Init, Run, Pause}

    /** 현재시간을 계속 구해서 출력하는 메소드 **/
    public String getTimeOut(){
        long now = SystemClock.elapsedRealtime() ;
        long outTime = now - myBaseTime ;
        String easy_outTime = String.format("%02d:%02d:%02d", (outTime/1000)/60, (outTime/1000)%60, (outTime%1000)/10) ;

        return easy_outTime ;
    }
}