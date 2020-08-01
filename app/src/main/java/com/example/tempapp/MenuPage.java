package com.example.tempapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tempapp.Graph_2.GraphActivity;
import com.example.tempapp.Pic.ImagePage;
import com.example.tempapp.Result.ResultPage;

/* 로그인 후 뜨는 메인화면 */

public class MenuPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }


    public void onClick_ms(View view) {
        Intent intent = new Intent(this, ImagePage.class);
        startActivity(intent);
    }

    public void onClick_rs(View view) {
        Intent intent = new Intent(this, ResultPage.class);
        startActivity(intent);
    }

    public void onClick_gh(View view) {
        Intent intent = new Intent(this, GraphActivity.class);
        startActivity(intent);
    }

    public void onClick_my(View view) {
        Intent intent = new Intent(this,  MyPage.class);
        startActivity(intent);
    }
}
