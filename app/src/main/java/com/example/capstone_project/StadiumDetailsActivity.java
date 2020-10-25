package com.example.capstone_project;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class StadiumDetailsActivity extends AppCompatActivity {

    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_ADD = "address";
    private static final String TAG_TIME = "time";
    private static final String TAG_CHARGE = "charge";
    private static final String TAG_TEL = "tel";

    JSONArray st = null;

    ArrayList<HashMap<String, String>> stadiumList;

    ListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stadium_details);

        list = (ListView) findViewById(R.id.listView);
        stadiumList = new ArrayList<HashMap<String, String>>();
        getData("http://192.168.219.108/PHP_connection.php"); //ip주소 자기 ip주소로 바꿔서 넣어야 됨
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            st = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < st.length(); i++) {
                JSONObject c = st.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);
                String time = c.getString(TAG_TIME);
                String charge = c.getString(TAG_CHARGE);
                String tel = c.getString(TAG_TEL);

                HashMap<String, String> sts = new HashMap<String, String>();

                sts.put(TAG_NAME, name);
                sts.put(TAG_ADD, address);
                sts.put(TAG_TIME, time);
                sts.put(TAG_CHARGE, charge);
                sts.put(TAG_TEL, tel);

                stadiumList.add(sts);
            }

            ListAdapter adapter = new SimpleAdapter(
                    StadiumDetailsActivity.this, stadiumList, R.layout.list_item,
                    new String[]{TAG_NAME, TAG_ADD, TAG_TIME, TAG_CHARGE, TAG_TEL},
                    new int[]{R.id.name, R.id.address, R.id.time, R.id.charge, R.id.tel}
            );

            list.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}

        /*
        TextView stadium_address = (TextView) findViewById(R.id.stadium_address);
        TextView stadium_time = (TextView) findViewById(R.id.stadium_time);
        TextView stadium_special = (TextView) findViewById(R.id.stadium_special);
        TextView stadium_charge = (TextView) findViewById(R.id.stadium_charge);
        TextView stadium_tele = (TextView) findViewById(R.id.stadium_tele);

         */






/*
    TextView stadium_nameTv = findViewById(R.id.stadium_nameTv);
    ImageView stadium_image = (ImageView) findViewById(R.id.stadium_imageView);

    Intent intent = getIntent();
    String stadium = intent.getExtras().getString("stadium");
    String stadiumName = intent.getExtras().getString("stadiumname");


 */








        //stadium_nameTv.setText(stadiumName);
/*
        if(stadium.equals("m0")){
            //월드컵
            stadium_address.setText("서울특별시 마포구 성산2동 월드컵로 240");
            stadium_time.setText("06:00 ~ 24:00");
            stadium_special.setText("잔디");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-2128-2002");
            stadium_image.setImageResource(R.drawable.a);
        } else if(stadium.equals("m1")){
            //난지천
            stadium_address.setText("서울특별시 마포구 상암동");
            stadium_time.setText("18:00 ~ 22:00");
            stadium_special.setText("인조잔디");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-3153-9874");
            stadium_image.setImageResource(R.drawable.b);
        }else if(stadium.equals("m2")){
            //한강시민공원
            stadium_address.setText("서울특별시 영등포구 여의도동");
            stadium_time.setText("06:00 ~ 20:00");
            stadium_special.setText("흙");
            stadium_charge.setText("유료");
            stadium_tele.setText("02-782-2898");
            stadium_image.setImageResource(R.drawable.c);
        }else if(stadium.equals("m3")){
            //의왕
            stadium_address.setText("경기도 의왕시 포일동 155-2");
            stadium_time.setText("06:00 ~ 24:00");
            stadium_special.setText("잔디");
            stadium_charge.setText("유료");
            stadium_tele.setText("031-426-1300");
            stadium_image.setImageResource(R.drawable.d);
        }else if(stadium.equals("m4")){
            //자유공원
            stadium_address.setText("경기도 안양시 동안구 호계동 1112");
            stadium_time.setText("08:00 ~ 24:00");
            stadium_special.setText("인조잔디");
            stadium_charge.setText("유료");
            stadium_tele.setText("031-8045-2413");
            stadium_image.setImageResource(R.drawable.e);
        }

 */
