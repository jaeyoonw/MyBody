package com.example.tempapp.Result;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.tempapp.R;

public class ThirdFragment extends Fragment {

    static float[] x, y;
    static TextView txt1, txt2;
    static PersonalData pd;
    static PHPConnection phpConnection = FirstFragment.phpConnection;
    static private float result_W;

    public ThirdFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        init();
        super.onCreate(savedInstanceState);
    }


    public void init() {
        x = new float[2];
        y = new float[2];
        pd = new PersonalData();
    }

    /* 결과 */
    static public void result_PHP() {
        pd = phpConnection.return_pd();
        x = pd.getWaist_X(); Log.e("XXX >> ", String.valueOf(x[1])+" "+String.valueOf(x[0]));
        y = pd.getWaist_Y(); Log.e("YYY >> ", String.valueOf(y[1])+" "+String.valueOf(y[0]));
        cal_balance(x, y);
    }

    /* 각도 계산 */
    static public String cal_balance(float[] x, float[] y) {
        result_W = (float)Math.atan2(Math.abs(y[1]-y[0]),Math.abs(x[1]-x[0])) * ( 180 /(float)Math.PI);

        String tempB;
        if (result_W > 7){
            tempB = "비정상";
        } else if (result_W > 3 && result_W <= 7) {
            tempB = "위험";
        } else
            tempB = "정상";


        txt1.setText(String.format("골반 각도는 %.2f도 입니다.", result_W));
        txt2.setText(String.format("%s입니다.",tempB));

        return tempB;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_third, container, false);

        TextView third_text1 = layout.findViewById(R.id.third1);
        third_text1.setText("<골반비대칭 관련 영상>:");
        Button button1 = layout.findViewById(R.id.btn_third_1);
        Button button2 = layout.findViewById(R.id.btn_third_2);

        txt1 = (TextView) layout.findViewById(R.id.third_text1);
        txt2 = (TextView) layout.findViewById(R.id.third_text2);

        result_PHP();   //txt1,2 null-point exception 에러로 위치 여기로  바꿈..



        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=lB11h1fAUok"));
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=nQlbDogfCpE"));
                startActivity(intent);
            }
        });

        return layout;
    }

    static public float getResult_W() {
        return result_W;
    }
}
