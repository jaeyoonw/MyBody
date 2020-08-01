package com.example.tempapp.Login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tempapp.MenuPage;
import com.example.tempapp.R;
import com.example.tempapp.Register.SignupPage;

public class LoginActivity extends Activity implements View.OnClickListener {

    DialogInterface manageDialog = null;

    private static MediaPlayer mp;

    static public boolean login_state=false;
    static boolean pw_ok=false, id_ok=false;
    static String getpw="", getid="";
    static public Context mContext;
    public static EditText etId, etPw;
    Button btn_login, btn_regist;
    TextView findpw;

    CheckBox autologin;
    Boolean loginChecked;

    SharedPreferences setting;
    SharedPreferences.Editor editor;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // media
        mp = MediaPlayer.create(this, R.raw.touchmybody);
        mp.setLooping(true);
        mp.start();

        // 초기화
        init();

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if(setting.getBoolean("autoLogin_enabled", false)){
            etId.setText(setting.getString("ID", ""));
            etPw.setText(setting.getString("PW", ""));
            autologin.setChecked(true);
        }

        autologin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    String ID = etId.getText().toString();
                    String PW = etPw.getText().toString();

                    editor.putString("ID", ID);
                    editor.putString("PW", PW);
                    editor.putBoolean("autoLogin_enabled", true);
                    editor.commit();
                }else{
                    /**
                     * remove�� ����°��� �κл���
                     * clear�� ����°��� ��ü ���� �Դϴ�
                     */
//					editor.remove("ID");
//					editor.remove("PW");
//					editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });


    }


    // 초기화 함수
    void init() {
        etId=(EditText)findViewById(R.id.txt_id);
        etPw=(EditText)findViewById(R.id.txt_pw);
        btn_login=(Button)findViewById(R.id.btn_signin);
        btn_regist=(Button) findViewById(R.id.btn_signup);
        btn_login.setOnClickListener(this);
        btn_regist.setOnClickListener(this);
        mContext=this;
        autologin = (CheckBox) findViewById(R.id.autologinchk);
        findpw = (TextView)findViewById(R.id.find_pw);
        findpw.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signin:   // 로그인 버튼
                if(etId.getText().toString().equals("") || etPw.getText().toString().equals("")) {  // id or password 빈칸일 경우
                    Toast.makeText(this, "Typing ID and PW.", Toast.LENGTH_SHORT).show();
                }else {
                    mp.pause();  //로그인 시 노래 정지
                    super.onUserLeaveHint();
                    login_proc(login_state);
                }
                break;
            case R.id.btn_signup:   // 회원가입 버튼
                Intent intent = new Intent(this, SignupPage.class);
                startActivity(intent);
                break;
            case R.id.find_pw:  //finding pw 버튼
                FindPw();
                break;
        }
    }

    public void login_proc(boolean login) {
        if (!login) {
            String id = etId.getText().toString();
            String pw = etPw.getText().toString();
            loginMysql idchk = new loginMysql(id,pw);
            loginMysql.active = true;
            idchk.start();
        }
    }

    /* 로그인 결과 */
    static public void result_login(String result,String pw){
        loginMysql.active=false;

        if(result.equals("false"))
            Toast.makeText(mContext,"Wrong ID",Toast.LENGTH_SHORT).show();
        else{
            if(pw.equals(result)) {

                Toast.makeText(mContext, "Welcome", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MenuPage.class);

                mContext.startActivity(intent);

            }else
                Toast.makeText(mContext,"Wrong PW",Toast.LENGTH_SHORT).show();
        }
    }


    /* 비밀번호 찾기 결과 */
    public static void result_findingpw(String pw, String result1, String result2, String userId, String userEmail) {   //pw는 db에서 가져온 것
        loginMysql.active = false;
        if (result1.equals("false") || result2.equals("false")) {//DB에 등록된 id, email이 없을 때
            Log.e("result1", result1);
            Toast.makeText(mContext, "Not regist user!!", Toast.LENGTH_SHORT).show();
        } else {       //DB에 등록된 id, email이 있을 때

            if (result1.equals(userId) && result2.equals(userEmail)) {
                Toast.makeText(mContext, "당신의 패스워드는 " + pw + "입니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }


    /* 비밀번호 찾기 */
    public void FindPw() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);    // 비밀번호 찾는 창 화면 생성
        builder.setTitle("비밀번호 찾기").setMessage("Typing below information");

        final LayoutInflater inflater=getLayoutInflater();
        View layout=inflater.inflate(R.layout.activity_findingpw, null);    //activity.findingpw 레이아웃을 View 화면으로 사용
        builder.setView(layout);    //위와 동일
        final EditText id=(EditText)layout.findViewById(R.id.UserId);
        final EditText name=(EditText)layout.findViewById(R.id.UserName);
        final EditText mail=(EditText)layout.findViewById(R.id.UserMail);

        builder.setPositiveButton("Finding PW", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {        //확인 버튼
                String infoId=id.getText().toString();
                String infoName=name.getText().toString();

                String infoMail=mail.getText().toString();

                if(infoName.length()!=0&&infoMail.length()!=0&&infoMail.contains("@")&&infoId.length()!=0){     //입력한 정보를 php를 통해 DB의 정보와 비교
                    FindingPw_sql_control.findingPW(infoId,infoName,infoMail);

                }else{
                    Toast.makeText(getApplication(),"Typing all information.",Toast.LENGTH_SHORT).show();

                    FindPw();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //getid=getpw=tempName=tempAge=tempPhone=tempMail=tempAddress="";
                Toast.makeText(getApplication(),"Cancel Finding PW.",Toast.LENGTH_SHORT).show();
            }
        }).create();
        manageDialog=builder.show();

    }

}