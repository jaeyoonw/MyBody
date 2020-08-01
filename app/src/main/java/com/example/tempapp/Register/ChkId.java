package com.example.tempapp.Register;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tempapp.R;
import com.example.tempapp.Login.loginMysql;

public class ChkId extends AppCompatActivity implements View.OnClickListener{
    Button chkId,useId;
    Button btn_id;
    EditText id;
    static TextView description;
    String chkid;
    static boolean chkok = false;
    static public Context mContext;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_idchk);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idchk);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        init();
    }

    void init(){
        id = (EditText)findViewById(R.id.new_id);
        chkId=(Button)findViewById(R.id.chkId);
        useId=(Button)findViewById(R.id.useId);
        chkId.setOnClickListener(this);
        useId.setOnClickListener(this);
        btn_id=(Button)findViewById(R.id.btn_id);
        description = (TextView)findViewById(R.id.description);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.chkId:            //CHECK버튼을 눌렀을 때
                chkid=id.getText().toString();

                if(chkid.length()!=0){
                    Idcheck_sql_control.idChk(chkid);   // 1

                }else{
                    Toast.makeText(getApplication(),"Typing User ID",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.useId:            //USE버튼을 눌렀을 때
                if(chkok){
//                    SignupPage.result_id(id.getText().toString());
//                    Intent intent = new Intent(this, SignupPage.class);
//                    startActivity(intent);
                    Intent intent = new Intent();
                    intent.putExtra("ok",true);
                    intent.putExtra("id",chkid);            //chkid에 유저가 입력한 것이 들어가고 그것을 id에 값을 넣고 전달
                    setResult(RESULT_OK, intent);                 //SignupPage.class로 넘김
                    finish();
                } else {
                    setResult(RESULT_CANCELED);
                }

                break;
        }
    }

//    static public void chkidresult(String result){  //id check
//
//        if(result.contains("false")&&!result.contains("{")){
//            Log.e("chkidresult","false");
//            description.setText("Possible to use.");
//            chkok=true;
//        }else{
//            Log.e("chkidresult","true");
//            description.setText("Duplicate ID");
//            chkok=false;
//        }
//
//    }

    static public void result_idchk(String result,String id){ //result는 db에서 가져온 아이디, id는 사용자가 입력한 아이디
        loginMysql.active=false;

        if(result.equals("false")) {                    //DB에 입력한 아이디가 없을 경우
            description.setText("Possible to use.");
            chkok=true;
        }
        else{
            if(id.equals(result)) {                     //DB에 입력한 아이디가 있을 경우
                description.setText("Duplicate ID");
                chkok=false;
            }
        }
    }
}
