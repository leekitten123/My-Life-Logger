package com.example.hyojin.mylifelogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

public class MyDataBase extends SQLiteOpenHelper {

    /** 클래스 선언 시 생성자 **/
    public MyDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    /** 데이터베이스 테이블 만들기 **/
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE database (_id INTEGER PRIMARY KEY AUTOINCREMENT, latitude REAL , longitude REAL , category INTEGER , date INTEGER , time INTEGER , whatDo TEXT);");
    }

    /** 데이터베이스를 새로 생성 **/
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS database");
        onCreate(db);
    }

    /** 데이터베이스에 정보 추가 **/
    public void insertData (Double latitude, Double longitude, int category, int date, int time, String whatDo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO database VALUES(NULL, " + latitude + ", " + longitude + ", " + category + ", " + date + ", " + time + ", '" + whatDo + "');");
        db.close();
    }

    public LatLng getLatLngToDB (int num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        for (int i = 0 ; i <= num ; i++) {
            cursor.moveToNext() ;
        }

        Double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
        Double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));

        cursor.close();
        db.close();

        return new LatLng(latitude, longitude);
    }

    public String getWhatDoToDB (int num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        for (int i = 0 ; i <= num ; i++) {
            cursor.moveToNext() ;
        }

        String whatdo = cursor.getString(cursor.getColumnIndex("whatDo"));

        cursor.close();
        db.close();

        return whatdo ;

    }

    public String getCategoryDateToDB (int num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        for (int i = 0 ; i <= num ; i++) {
            cursor.moveToNext() ;
        }

        int category = cursor.getInt(cursor.getColumnIndex("category"));
        int date = cursor.getInt(cursor.getColumnIndex("date"));
        SpinnerCategory spinnerCategory = new SpinnerCategory() ;

        cursor.close();
        db.close();

        return  new String("카테고리: " + spinnerCategory.CategoryToString(category) + " 날짜: "+ date) ;

    }

    public int getTimeToDB (int num) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        for (int i = 0 ; i <= num ; i++) {
            cursor.moveToNext() ;
        }

        int time = cursor.getInt(cursor.getColumnIndex("time"));

        cursor.close();
        db.close();

        return time ;

    }


    public int getSizeDB() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM database", null);

        int size = cursor.getCount() ;

        cursor.close();
        db.close();

        return size ;
    }


}
