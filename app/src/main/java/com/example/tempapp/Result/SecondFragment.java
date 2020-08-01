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

public class SecondFragment extends Fragment {

    static float[] x, y;
    static TextView text1, text2;
    static PersonalData pd;
    static PHPConnection phpConnection = FirstFragment.phpConnection;
    static private float result_S;

    public SecondFragment() { }

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


    static public void result_PHP(PersonalData p) {
        pd  = p;
        x = pd.getShoulder_X(); Log.e("XX >> ", String.valueOf(x[1])+" "+String.valueOf(x[0]));
        y = pd.getShoulder_Y(); Log.e("YY >> ", String.valueOf(y[1])+" "+String.valueOf(y[0]));
        cal_balance(x, y);
    }

    /* 각도 계산 */
    static public String cal_balance(float[] x, float[] y) {
        result_S = (float)Math.atan2(Math.abs(y[1]-y[0]),Math.abs(x[1]-x[0])) * ( 180 /(float)Math.PI);

        String tempB;
        if (result_S >10){
            tempB = "비정상";
        }
        else
            tempB = "정상";

        text1.setText(String.format("어깨 각도는 %.2f도 입니다.", result_S));
        text2.setText(String.format("%s입니다.",tempB));

        return tempB;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_second, container, false);

        TextView second_text2 = layout.findViewById(R.id.second1);
        second_text2.setText("<어깨비대칭 관련 영상>:");
        Button button1 = layout.findViewById(R.id.btn_second_1);
        Button button2 = layout.findViewById(R.id.btn_second_2);

        text1 = layout.findViewById(R.id.second_text1);
        text2 = layout.findViewById(R.id.second_text2);

        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=graejFwg8tQ"));
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=n4T4bRINdpE"));
                startActivity(intent);
            }
        });

        return layout;

    }

    static public float getResult_S() {
        return result_S;
    }
}
