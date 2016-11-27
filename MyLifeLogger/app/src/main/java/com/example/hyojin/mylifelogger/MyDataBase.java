package com.example.hyojin.mylifelogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
}
