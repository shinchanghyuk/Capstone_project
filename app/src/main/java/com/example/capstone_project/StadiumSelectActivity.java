package com.example.capstone_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.HashSet;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class StadiumSelectActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlacesListener{
    private MapFragment mapFrag;
    private GoogleMap gMap;
    private Button stadium_btn;
    ListView list;
    ArrayList<String> stadium_Data = new ArrayList<String>();
    GroundOverlayOptions videoMark;
    String stadium;
    List<Marker> previous_marker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_select);

        previous_marker = new ArrayList<Marker>();

        stadium_btn = (Button)findViewById(R.id.stadium_btn);

        stadium_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlaceInformation(new LatLng(37.568256, 126.897240));
            }
        });


        stadium_Data.add("월드컵 경기장");
        stadium_Data.add("난지천 공원 축구장");
        stadium_Data.add("한강시민공원 축구장");
        stadium_Data.add("의왕 축구장");
        stadium_Data.add("자유공원 축구장");

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
                if(stadium_Data.get(position)=="월드컵 경기장"){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256, 126.897240),15));
                    stadium = "월드컵경기장";
                } else if(stadium_Data.get(position)=="한강시민공원 축구장"){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5342321, 126.9150377),15));
                    stadium = "한강시민공원 축구장";
                } else if(stadium_Data.get(position)=="난지천 공원 축구장"){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5741465, 126.8816503),15));
                    stadium = "난지천 공원 축구장";
                } else if(stadium_Data.get(position)=="의왕 축구장"){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.3882791,126.9861281),15));
                    stadium = "의왕 축구장";
                }else if(stadium_Data.get(position)=="자유공원 축구장"){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.3808392,126.9613703),15));
                    stadium = "자유공원 축구장";
                }
            }
        });
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    //String markerSnippet = getCurrentAddress(latLng); //현재위치

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet("테스트");
                    Marker item = gMap.addMarker(markerOptions);
                    previous_marker.add(item);

                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }

    public void showPlaceInformation(LatLng location)
    {
        gMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(StadiumSelectActivity.this)
                .key("AIzaSyCYcO3aYjvpwhVMI-5RdW3de9xcThjSoeI")
                .latlng(37.568256, 126.897240)//위치
                .radius(2000) //2000 미터 내에서 검색
                .type(PlaceType.STADIUM) //구장
                .build()
                .execute();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.568256, 126.897240),15));
        gMap.getUiSettings().setZoomControlsEnabled(true);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(new LatLng(37.568256, 126.897240));//월드컵
        markerOptions.alpha(0.7f);
        gMap.addMarker(markerOptions); //마커 설정 후 출력

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(new LatLng(37.5741465, 126.8816503));//한강 시민
        markerOptions1.alpha(0.7f);
        gMap.addMarker(markerOptions1); //마커 설정 후 출력

        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(new LatLng(37.5342321, 126.9150377));//난지천
        markerOptions2.alpha(0.7f);
        gMap.addMarker(markerOptions2); //마커 설정 후 출력

        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(new LatLng(37.3882791,126.9861281));//의왕 축구장
        markerOptions3.alpha(0.7f);
        gMap.addMarker(markerOptions3); //마커 설정 후 출력

        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.position(new LatLng(37.3808392,126.9613703));//자유 공원 축구장
        markerOptions4.alpha(0.7f);
        gMap.addMarker(markerOptions4); //마커 설정 후 출력

        gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //이벤트 처리 추가
                String markerid = marker.getId();
                Intent intent = new Intent(StadiumSelectActivity.this, StadiumDetailsActivity.class);
                intent.putExtra("stadium",markerid);
                intent.putExtra("stadiumname", stadium);
                startActivity(intent);
                return false;
            }
        });
    }
}