package com.example.hyojin.mylifelogger;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xml.sax.Parser;

import java.util.Calendar;

public class ManageGoalFragment extends Fragment {
    /** From 년, 월, 일 **/
    int fromYear = Calendar.getInstance().get(Calendar.YEAR);
    int fromMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int fromDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int fromCalender = makeFullCalender(fromYear, fromMonth, fromDate) ;

    /** To 년, 월, 일 **/
    int toYear = Calendar.getInstance().get(Calendar.YEAR);
    int toMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int toDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    int toCalender = makeFullCalender(toYear, toMonth, toDate) ;

    /**  **/
    TextView textViewFromTime ;
    TextView textViewToTime ;

    /**  **/
    Button btnSettingGoals ;

    /**  **/
    TextView textView_game1 ;
    TextView textView_eat1 ;
    TextView textView_move1 ;
    TextView textView_study1 ;
    TextView textView_work1 ;
    TextView textView_other1 ;

    /**  **/
    TextView textView_game2 ;
    TextView textView_eat2 ;
    TextView textView_move2 ;
    TextView textView_study2 ;
    TextView textView_work2 ;
    TextView textView_other2 ;

    /**  **/
    TextView textView_game3 ;
    TextView textView_eat3 ;
    TextView textView_move3 ;
    TextView textView_study3 ;
    TextView textView_work3 ;
    TextView textView_other3 ;

    /**  **/
    int goalGame = 0;
    int goalEat = 0;
    int goalMove = 0;
    int goalStudy = 0;
    int goalWork = 0;
    int goalOther = 0;

    /**  **/
    MyDataBase taskDB;

    public ManageGoalFragment() {}

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_manage_goal, container, false);

        /**  **/
        textViewFromTime = (TextView) view.findViewById(R.id.text_FromTime);
        textViewFromTime.setText(fromCalender + "");
        textViewToTime = (TextView) view.findViewById(R.id.text_ToTime);
        textViewToTime.setText(toCalender + "");

        /** From 날짜 정보를 받는 이벤트 리스너 **/
        textViewFromTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {
                        fromYear = year;
                        fromMonth = monthOfYear + 1;
                        fromDate = dayOfMonth;
                        fromCalender = makeFullCalender(fromYear, fromMonth, fromDate) ;

                        textViewFromTime.setText(fromCalender +  "");
                    }
                };

                new DatePickerDialog(view.getContext(), dateSetListener, fromYear, fromMonth, fromDate).show();
            }
        });

        /** To 날짜 정보를 받는 이벤트 리스너 **/
        textViewToTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker v, int year, int monthOfYear, int dayOfMonth) {
                        toYear = year;
                        toMonth = monthOfYear + 1;
                        toDate = dayOfMonth;
                        toCalender = makeFullCalender(toYear, toMonth, toDate) ;

                        textViewToTime.setText(toCalender + "");
                    }
                };

                new DatePickerDialog(view.getContext(), dateSetListener, toYear, toMonth, toDate).show();
            }
        });

        /**  **/
        textView_game1 = (TextView) view.findViewById(R.id.text_game1) ;
        textView_eat1 = (TextView) view.findViewById(R.id.text_eat1) ;
        textView_move1 = (TextView) view.findViewById(R.id.text_move1) ;
        textView_study1 = (TextView) view.findViewById(R.id.text_study1) ;
        textView_work1 = (TextView) view.findViewById(R.id.text_work1) ;
        textView_other1 = (TextView) view.findViewById(R.id.text_other1) ;

        /** **/
        btnSettingGoals = (Button) view.findViewById(R.id.btn_SettingGoals);
        btnSettingGoals.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final View dialogView = inflater.inflate(R.layout.dialog_update_goal, null);

                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("업데이트");
                dialog.setView(dialogView);

                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText_game = (EditText) dialogView.findViewById(R.id.edit_game) ;
                        EditText editText_eat = (EditText) dialogView.findViewById(R.id.edit_eat) ;
                        EditText editText_move = (EditText) dialogView.findViewById(R.id.edit_move) ;
                        EditText editText_study = (EditText) dialogView.findViewById(R.id.edit_study) ;
                        EditText editText_work = (EditText) dialogView.findViewById(R.id.edit_work) ;
                        EditText editText_other = (EditText) dialogView.findViewById(R.id.edit_other) ;

                        goalGame = Integer.parseInt(editText_game.getText().toString()) ;
                        goalEat = Integer.parseInt(editText_eat.getText().toString()) ;
                        goalMove = Integer.parseInt(editText_move.getText().toString()) ;
                        goalStudy = Integer.parseInt(editText_study.getText().toString()) ;
                        goalWork = Integer.parseInt(editText_work.getText().toString()) ;
                        goalOther = Integer.parseInt(editText_other.getText().toString()) ;

                        textView_game1.setText(goalGame + " s") ;
                        textView_eat1.setText(goalEat + " s") ;
                        textView_move1.setText(goalMove + " s") ;
                        textView_study1.setText(goalStudy + " s") ;
                        textView_work1.setText(goalWork + " s") ;
                        textView_other1.setText(goalOther + " s") ;
                    }
                });

                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        /**  **/
        taskDB = new MyDataBase(getContext(), "TaskDataBase.db", null, 1);

        /**  **/
        textView_game2 = (TextView) view.findViewById(R.id.text_game2) ;
        textView_eat2 = (TextView) view.findViewById(R.id.text_eat2) ;
        textView_move2 = (TextView) view.findViewById(R.id.text_move2) ;
        textView_study2 = (TextView) view.findViewById(R.id.text_study2) ;
        textView_work2 = (TextView) view.findViewById(R.id.text_work2) ;
        textView_other2 = (TextView) view.findViewById(R.id.text_other2) ;

        /**  **/
        textView_game3 = (TextView) view.findViewById(R.id.text_game3) ;
        textView_eat3 = (TextView) view.findViewById(R.id.text_eat3) ;
        textView_move3 = (TextView) view.findViewById(R.id.text_move3) ;
        textView_study3 = (TextView) view.findViewById(R.id.text_study3) ;
        textView_work3 = (TextView) view.findViewById(R.id.text_work3) ;
        textView_other3 = (TextView) view.findViewById(R.id.text_other3) ;

        /**  **/
        Button btnWatchGoal = (Button) view.findViewById(R.id.btn_WatchGoal) ;
        btnWatchGoal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                /**  **/
                int checkTimeGame = 0 ;
                int checkTimeEat = 0 ;
                int checkTimeMove = 0 ;
                int checkTimeStudy = 0 ;
                int checkTimeWork = 0 ;
                int checkTimeOther = 0 ;

                for (int i = 0 ; i < taskDB.getSizeDB() - 1 ; i++) {
                    if ((taskDB.getTimeToDB(i) >= taskDB.getTimeToDB(i + 1)) ||  (i == taskDB.getSizeDB() - 2)) {
                        if (i == taskDB.getSizeDB() - 2) {
                            i++;
                        }

                        if (taskDB.getDateToDB(i) >= fromCalender && taskDB.getDateToDB(i) <= toCalender){
                            switch (taskDB.getCategoryToDB(i)) {
                                case "게임" :
                                    checkTimeGame += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                                case "식사" :
                                    checkTimeEat += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                                case "이동" :
                                    checkTimeMove += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                                case "공부" :
                                    checkTimeStudy += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                                case "일" :
                                    checkTimeWork += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                                case "그외" :
                                    checkTimeOther += makeTimeToSeconds(taskDB.getTimeToDB(i)) ;
                                    break ;
                            }
                        }
                    }
                }

                /**  **/
                textView_game2.setText(checkTimeGame + " s") ;
                textView_eat2.setText(checkTimeEat + " s") ;
                textView_move2.setText(checkTimeMove + " s") ;
                textView_study2.setText(checkTimeStudy + " s") ;
                textView_work2.setText(checkTimeWork + " s") ;
                textView_other2.setText(checkTimeOther + " s") ;

                /**  **/
                textView_game3.setText(checkTimeGame*100/goalGame + " %") ;
                textView_eat3.setText(checkTimeEat*100/goalEat + " %") ;
                textView_move3.setText(checkTimeMove*100/goalMove + " %") ;
                textView_study3.setText(checkTimeStudy*100/goalStudy + " %") ;
                textView_work3.setText(checkTimeWork*100/goalWork + " %") ;
                textView_other3.setText(checkTimeOther*100/goalOther + " %") ;
            }
        });
        return view;
    }

    int makeFullCalender(int year, int month, int date) {
        return (year * 10000) + (month * 100) + date;
    }

    int makeTimeToSeconds (int time) {
        int tempTime = time / 100 ;

        return (60 * (tempTime / 100)) + (tempTime % 100) ;
    }
}
