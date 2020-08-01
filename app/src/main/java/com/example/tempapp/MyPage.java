package com.example.tempapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPage extends Activity {
    
    private ArrayList<HashMap<String, String>> Data = new ArrayList<HashMap<String, String>>();
    private HashMap<String, String> InputData1 = new HashMap<>();
    private HashMap<String, String> InputData2 = new HashMap<>();
    private HashMap<String, String> InputData3 = new HashMap<>();
    private HashMap<String, String> InputData4 = new HashMap<>();
    private HashMap<String, String> InputData5 = new HashMap<>();
    private HashMap<String, String> InputData6 = new HashMap<>();

    private ListView listView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        listView = findViewById(R.id.List_view);
        
        //데이터 초기화
        InputData1.put("title", "안면비대칭 자가교정운동");
        InputData1.put("name", "얼굴 비대칭 교정 영상1");
        Data.add(InputData1);
        
        InputData2.put("title", "완전쉬운 1분 안면교정 스트레칭");
        InputData2.put("name", "얼굴 비대칭 교정 영상2");
        Data.add(InputData2);

        InputData3.put("title", "어깨 높이차이 교정");
        InputData3.put("name", "어깨 비대칭 교정 영상1");
        Data.add(InputData3);


        InputData4.put("title", "올라간 어깨 1분30초만에 교정하기");
        InputData4.put("name", "어깨 비대칭 교정 영상2");
        Data.add(InputData4);

        InputData5.put("title", "골반 비대칭을 잡아주는 호주물리치료사의 교정루틴");
        InputData5.put("name", "골반 비대칭 교정영상1");
        Data.add(InputData5);

        InputData6.put("title", "골반교정 Upgrade편");
        InputData6.put("name", "골반 비대칭 교정 영상2");
        Data.add(InputData6);
        
        //simpleAdapter 생성
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, Data, android.R.layout.simple_list_item_2, new String[]{"title", "name"}, new int[]{android.R.id.text1, android.R.id.text2});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo1.class);
                    startActivity(intent);
                }else if (position == 1) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo2.class);
                    startActivity(intent);
                }else if (position == 2) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo3.class);
                    startActivity(intent);
                }else if (position == 3) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo4.class);
                    startActivity(intent);
                }else if (position == 4) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo5.class);
                    startActivity(intent);
                }else if (position == 5) {
                    Intent intent = new Intent(getApplicationContext(), YoutubeVideo6.class);
                    startActivity(intent);
                }
            }
        });
        
       
        
    }
}
