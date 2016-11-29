package com.example.hyojin.mylifelogger;

import android.app.DatePickerDialog;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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

import java.util.ArrayList;
import java.util.Calendar;

public class GetTaskFragment extends Fragment {
    /** 오늘 년, 월, 일 **/
    int iYear = Calendar.getInstance().get(Calendar.YEAR);
    int iMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int iDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    /** 위도, 경도 **/
    static Double latitude = 0.0;
    static Double longitude = 0.0;

    /** 위치정보를 보여주는 뷰 **/
    TextView textTaskLatitude;
    TextView textTaskLongitude;

    /** 스톱워치에 사용할 변수 **/
    statusStopWatch cur_Status = statusStopWatch.Init;
    long myBaseTime;
    long myPauseTime;
    long resetNow;
    String currentTime;

    /** 스톱워치에 사용할 뷰 **/
    TextView textPrintTime;
    Button btnTimeStatus;
    Button btnTimeRecord;

    /** 스톱워치에서 위치와 시간을 받을 임시 변수 **/
    ArrayList<Double> tempLatitude = new ArrayList<>();
    ArrayList<Double> tempLongitude = new ArrayList<>();
    ArrayList<Integer> tempTime = new ArrayList<>();

    /** 스톱워치에 사용할 핸들러 **/
    Handler myTimer = new Handler() {
        public void handleMessage(Message msg) {
            textPrintTime.setText(getTimeOut());
            myTimer.sendEmptyMessage(0);
        }
    };

    /** Spinner에서 가르키는 항목 **/
    static int taskCategory = 0;

    /** 이벤트를 저장하는 데이터베이스 **/
    MyDataBase taskDB;

    /** 기본 생성자 **/
    public GetTaskFragment() {
    }

    /** GetTaskFragment 생성 **/
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_get_task, container, false);

        taskDB = new MyDataBase(getActivity(), "TaskDataBase.db", null, 1);

        /** 초기 달력 출력 **/
        TextView textViewCalenderTask = (TextView) view.findViewById(R.id.text_CalenderTask);
        textViewCalenderTask.setText(iYear + "년 " + iMonth + "월 " + iDate + "일");
        iMonth -= 1;

        /** 카테고리 를 받는 이벤트 리스너 **/
        Spinner spinner = (Spinner) view.findViewById(R.id.spinnerTask);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskCategory = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        /** 위치정보를 보여주는 뷰 **/
        textTaskLatitude = (TextView) view.findViewById(R.id.text_TaskLatitude);
        textTaskLongitude = (TextView) view.findViewById(R.id.text_TaskLongitude);

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

                        currentTime = getTimeOut();
                        Toast.makeText(getActivity().getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
                        startLocationService();
                        setDataToArrayList(latitude, longitude, currentTime);

                        btnTimeStatus.setText("중지");
                        btnTimeRecord.setText("기록");
                        btnTimeRecord.setEnabled(true);
                        cur_Status = statusStopWatch.Run;
                        break;

                    case Run:
                        myTimer.removeMessages(0);
                        myPauseTime = SystemClock.elapsedRealtime();
                        resetNow = SystemClock.elapsedRealtime();

                        btnTimeStatus.setText("시작");
                        btnTimeRecord.setEnabled(false);
                        cur_Status = statusStopWatch.Pause;
                        break;

                    case Pause:
                        long now = SystemClock.elapsedRealtime();
                        myTimer.sendEmptyMessage(0);
                        myBaseTime += (now - myPauseTime);

                        currentTime = getTimeOut();
                        Toast.makeText(getActivity().getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
                        startLocationService();
                        setDataToArrayList(latitude, longitude, currentTime);

                        btnTimeStatus.setText("중지");
                        btnTimeRecord.setEnabled(true);
                        cur_Status = statusStopWatch.Run;
                        break;
                }
            }
        });

        /** 스톱워치를 기록하는 리스너 **/
        btnTimeRecord = (Button) view.findViewById(R.id.btn_TimeRecord);
        btnTimeRecord.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                currentTime = getTimeOut();
                Toast.makeText(getActivity().getApplicationContext(), currentTime, Toast.LENGTH_SHORT).show();
                startLocationService();
                setDataToArrayList(latitude, longitude, currentTime);
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

        /** DB에 값 저장하기 **/
        Button btnSaveTask = (Button) view.findViewById(R.id.btn_SaveTask);
        btnSaveTask.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int realDateNum = (iYear * 10000) + (iMonth * 100) + 100 + iDate;
                EditText editWhatDoTask = (EditText) view.findViewById(R.id.whatdoTask);

                currentTime = getTimeResetOut();
                startLocationService();
                setDataToArrayList(latitude, longitude, currentTime);

                for (int i = 0; i < tempLatitude.size(); i++) {
                    taskDB.insertData(tempLatitude.get(i), tempLongitude.get(i), taskCategory, realDateNum, tempTime.get(i), editWhatDoTask.getText().toString());
                    Log.d("SQL", tempLatitude.get(i) + " " + tempLongitude.get(i) + " " + taskCategory + " " + realDateNum + " " + tempTime.get(i) + " " + editWhatDoTask.getText().toString()) ;
                }

                setClearToArrayList();

                editWhatDoTask.setText("");
                textPrintTime.setText("00:00:00") ;
                cur_Status = statusStopWatch.Init ;
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

        latitude = gpsListener.latitude;
        longitude = gpsListener.longitude;

        textTaskLatitude.setText("Latitude: " + latitude);
        textTaskLongitude.setText("Longitude: " + longitude);
    }

    /** 스톱워치의 현재 정보 **/
    public enum statusStopWatch {
        Init, Run, Pause
    }

    /** 현재시간을 계속 구하는 메소드 **/
    public String getTimeOut() {
        long now = SystemClock.elapsedRealtime();
        long outTime = now - myBaseTime;

        return String.format("%02d:%02d:%02d", (outTime / 1000) / 60, (outTime / 1000) % 60, (outTime % 1000) / 10);
    }

    public String getTimeResetOut() {
        long outTime = resetNow - myBaseTime;

        return String.format("%02d:%02d:%02d", (outTime / 1000) / 60, (outTime / 1000) % 60, (outTime % 1000) / 10);
    }

    /** 시간정보를 숫자로 구하는 메소드 **/
    public int getTimeToInt(String string) {
        return Integer.parseInt(string.replace(":", ""));
    }

    /** 위도, 경도, 시간을 임시 변수에 순차적으로 저장 **/
    public void setDataToArrayList(Double latitude, Double longitude, String time) {
        tempLatitude.add(latitude);
        tempLongitude.add(longitude);
        tempTime.add(getTimeToInt(time));
    }

    /** 임시 변수를 초기화 **/
    public void setClearToArrayList() {
        tempLatitude.clear();
        tempLongitude.clear();
        tempTime.clear();
    }
}