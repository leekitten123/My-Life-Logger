package com.example.hyojin.mylifelogger;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsLoggerActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    final MyDataBase eventDB = new MyDataBase(this, "EventDataBase.db", null, 1);
    final MyDataBase taskDB = new MyDataBase(this, "TaskDataBase.db", null, 1);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_logger);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void onClickTask (View view) {
        mMap.clear();

        PolylineOptions polylineOptions = new PolylineOptions() ;

        for (int i = 0 ; i < taskDB.getSizeDB() ; i++) {
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(taskDB.getLatLngToDB(i)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(taskDB.getLatLngToDB(i), 15));
            }
            mMap.addMarker(new MarkerOptions().position(taskDB.getLatLngToDB(i)).title(taskDB.getWhatDoToDB(i)).snippet(taskDB.getCategoryDateToDB(i) + " 시간: " + taskDB.getTimeToDB(i))) ;

            if (taskDB.getTimeToDB(i) == 0) {
                polylineOptions = new PolylineOptions() ;
            }
            polylineOptions.add(taskDB.getLatLngToDB(i)) ;
            mMap.addPolyline(polylineOptions);
        }
    }

    public void onClickEvent (View view) {
        mMap.clear();

        for (int i = 0 ; i < eventDB.getSizeDB() ; i++) {
            if (i == 0) {
                mMap.moveCamera(CameraUpdateFactory.newLatLng(eventDB.getLatLngToDB(i)));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventDB.getLatLngToDB(i), 15));
            }
            mMap.addMarker(new MarkerOptions().position(eventDB.getLatLngToDB(i)).title(eventDB.getWhatDoToDB(i)).snippet(eventDB.getCategoryDateToDB(i))) ;
        }
    }
}
