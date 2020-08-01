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

public class FirstFragment extends Fragment {

    //YouTubePlayer View youtubeView;
    Button button;
    //YouTubePlayer.OnInitializedListener listener;*/

    static public boolean PHP_state = false;
    static float[] x, y;
    static TextView text1, text2;

    static PersonalData pd;
    static PHPConnection phpConnection;
    static private float result_E;

    public FirstFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        init();
        super.onCreate(savedInstanceState);
    }


    public void init() {
        // 변수 초기화
        x = new float[2];
        y = new float[2];
        pd = new PersonalData();

        PHP_proc(PHP_state);
    }


    public void PHP_proc(boolean bool) {
        if (!bool) {
            phpConnection = new PHPConnection();
            phpConnection.active = true;
            phpConnection.start();
        }
    }

    /* PHP 처리 결과 */
    static public void result_PHP(PersonalData p) {
        //phpConnection.active=false;
        pd = p;
        x = pd.getEar_X(); Log.e("X >> ", String.valueOf(x[1])+" "+String.valueOf(x[0]));
        y = pd.getEar_Y(); Log.e("Y >> ", String.valueOf(y[1])+" "+String.valueOf(y[0]));
        cal_balance(x, y);
    }

    static public String cal_balance(float[] x, float[] y) {
        result_E = (float)Math.atan2(Math.abs(y[1]-y[0]),Math.abs(x[1]-x[0])) * ( 180 /(float)Math.PI);

        String tempB;
        if (result_E > 10){
            tempB = "비정상";
        }
        else
            tempB = "정상";

        text1.setText(String.format("얼굴 각도는 %.2f도 입니다.", result_E));
        text2.setText(String.format("%s입니다.",tempB));

        return tempB;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_first, container, false);

        TextView first_text1 = layout.findViewById(R.id.first1);
        first_text1.setText("<얼굴비대칭 관련 영상>");
        Button button1 = layout.findViewById(R.id.btn_first_1);
        Button button2 = layout.findViewById(R.id.btn_first_2);

        text1 = layout.findViewById(R.id.first_text1);
        text2 = layout.findViewById(R.id.first_text2);

        button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=24zf7viOpCE"));
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=NcW7Vgc62cA"));
                startActivity(intent);
            }
        });


        return layout;

    }

    static public float getResult_E() {
        return result_E;
    }

}
