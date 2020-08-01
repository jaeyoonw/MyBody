package com.example.tempapp.Login;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

public class FindingPw extends Activity {

    static public Context mContext;

//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.);
//
//    }



    static public void result_sendingpw(String result){
        loginMysql.active=false;
        //Log.e("result",result);
        if(result.contains("Message successfully sent!")) {
            Toast.makeText(mContext, "Password successfully sent!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext, "Fail to sent mail!", Toast.LENGTH_SHORT).show();
        }
    }
}
