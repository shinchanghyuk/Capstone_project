package com.example.capstone_project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class StadiumSelectActivity extends AppCompatActivity implements OnMapReadyCallback {
    MapFragment mapFrag;
    GoogleMap gMap;
    ListView list;
    ArrayList<String> stadium_Data = new ArrayList<String>();
    GroundOverlayOptions videoMark;
    String stadium = "월드컵 경기장";

    String na;
    double ad,bd;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_select);

        myHelper = new MyDBHelper(this);

        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM placeTBL;", null);

        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "DB값 없음.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            na = cursor.getString(0);
            ad = cursor.getDouble(1);
            bd = cursor.getDouble(2);
            stadium_Data.add(na);
        }

        list = (ListView) findViewById(R.id.stadium_listview);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MODE_PRIVATE);

        //구글 맵에서 제공하는 지도 전용 프래그먼트를 인플레이팅
        mapFrag =  (MapFragment) getFragmentManager().findFragmentById(R.id.stadium_map);
        mapFrag.getMapAsync(this); //onMapReady가 호출

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stadium_Data);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor;
                cursor = sqlDB.rawQuery("SELECT * FROM placeTBL;", null);

                if (cursor.getCount() == 0){
                    Toast.makeText(getApplicationContext(), "DB값 없음.", Toast.LENGTH_SHORT).show();
                    return;
                }

                while (cursor.moveToNext()) {
                    na = cursor.getString(0);
                    ad = cursor.getDouble(1);
                    bd = cursor.getDouble(2);

                    if(stadium_Data.get(position).equals(na)){
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(ad,bd),15));
                        stadium = na;
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256, 126.897240),15));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT * FROM placeTBL;", null);

        if (cursor.getCount() == 0){
            Toast.makeText(getApplicationContext(), "DB값 없음.", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            na = cursor.getString(0);
            ad = cursor.getDouble(1);
            bd = cursor.getDouble(2);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(ad, bd));
            markerOptions.alpha(0.7f);
            gMap.addMarker(markerOptions); //마커 설정 후 출력
        }

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //이벤트 처리 추가
                String markerid = marker.getId();
                Intent intent = new Intent(StadiumSelectActivity.this, StadiumDetailsActivity.class);
                intent.putExtra("stadiumname", stadium);
                startActivity(intent);
                return false;
            }
        });
    }
}