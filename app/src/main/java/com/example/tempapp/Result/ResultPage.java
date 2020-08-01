package com.example.tempapp.Result;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tempapp.R;

/* 결과 값 출력 화면 */

public class ResultPage extends AppCompatActivity {


    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        vp = findViewById(R.id.vp);
        //버튼 초기화
        Button btn_first = findViewById(R.id.btn_first);
        Button btn_second = findViewById(R.id.btn_second);
        Button btn_third = findViewById(R.id.btn_third);
        Button btn_bottom = findViewById(R.id.btn_send);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager())); // Adapter(View pgae의 page 관리) 연결
        vp.setCurrentItem(0); // 앱 실행 됐을 때 첫번째 페이지로 초기화

        btn_first.setOnClickListener(movePageListener);
        btn_first.setTag(0);
        btn_second.setOnClickListener(movePageListener);
        btn_second.setTag(1);
        btn_third.setOnClickListener(movePageListener);
        btn_third.setTag(2);

        btn_bottom.setOnClickListener(callSetMysql);

    }

    // mysql 전송 클래스 호출
    View.OnClickListener callSetMysql = new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          SetMysql setMysql = new SetMysql();
      }
    };

    View.OnClickListener movePageListener = new View.OnClickListener() // 버튼 클릭 시 해당페이지로 이동
    {
        @Override
        public void onClick(View v)
        {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter //adapter
    {
        public pagerAdapter(androidx.fragment.app.FragmentManager fm)
        {
            super(fm);
        }
        @Override
        public androidx.fragment.app.Fragment getItem(int position)
        {
            switch(position) //포지션 값이 들어오면 해당 Fragment class 열어줌
            {
                case 0:

                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                case 2:
                    return new ThirdFragment();
                default:
                    return null;
            }
        }
        @Override
        public int getCount() //page의 갯수
        {
            return 3;
        }
    }
}