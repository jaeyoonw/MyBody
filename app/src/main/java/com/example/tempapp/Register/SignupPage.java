package com.example.tempapp.Register;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tempapp.Login.LoginActivity;
import com.example.tempapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/* 회원가입 */

public class SignupPage extends Activity {
    private EditText editTextId;
    private EditText editTextPw;
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextEmail;
    private EditText editTextPhone;

    static private Button buttonId;
    static private boolean con;
    private Button buttonPw;

    final int code_chkid = 1000;
    static boolean id_ok = false;
    static String getid = "";


    Context mContext = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        buttonId = (Button) findViewById(R.id.btn_id);
        buttonId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(SignupPage.this, ChkId.class);
                startActivityForResult(intent2, code_chkid);
            }
        });

        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw = (EditText) findViewById(R.id.new_pw);
        editTextName = (EditText) findViewById(R.id.new_name);
        editTextAge = (EditText) findViewById(R.id.new_age);
        editTextEmail = (EditText) findViewById(R.id.new_email);
        editTextPhone = (EditText) findViewById(R.id.new_phone);



    }
    public void insert(View view) { // submit 버튼 onclick listener, Sign Up 버튼 누르면 실행
        String Id = buttonId.getText().toString();      //중요
        String Pw = editTextPw.getText().toString();
        String Name = editTextName.getText().toString();
        String Age = editTextAge.getText().toString();
        String Email = editTextEmail.getText().toString();
        String Phone = editTextPhone.getText().toString();


        if(buttonId.getText().toString().equals("") || editTextPw.getText().toString().equals("") ||
                editTextName.getText().toString().equals("") || editTextAge.getText().toString().equals("") ||
                editTextEmail.getText().toString().equals("") || editTextPhone.getText().toString().equals("")) {  // id or password 빈칸일 경우
            Toast.makeText(mContext, "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(mContext, "회원가입 완료", Toast.LENGTH_SHORT).show();
            insertoToDatabase(Id, Pw, Name, Age, Phone, Email);
            Intent intent = new Intent(SignupPage.this, LoginActivity.class);
            startActivity(intent);

        }


    }
    private void insertoToDatabase(String Id, String Pw, String Name,String Age,String Phone,String Email) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Name = (String) params[2];
                    String Age = (String) params[3];
                    String Phone = (String) params[4];
                    String Email = (String) params[5];


                    String link = "http://52.78.99.175/post.php";
                    String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");
                    data += "&" + URLEncoder.encode("Age", "UTF-8") + "=" + URLEncoder.encode(Age, "UTF-8");
                    data += "&" + URLEncoder.encode("Phone", "UTF-8") + "=" + URLEncoder.encode(Phone, "UTF-8");
                    data += "&" + URLEncoder.encode("Email", "UTF-8") + "=" + URLEncoder.encode(Email, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw, Name, Age, Phone, Email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){   // ChkId의 신호를 받아 실행할 작업
                case code_chkid:
                    if(data.getExtras().getBoolean("ok")){
                        getid=data.getExtras().getString("id");
                        Log.e(getid, getid);
                        id_ok=true;

                        buttonId.setText(getid);

                    }
                    //manageDialog.dismiss();
                    //regist();
                    break;
            }
        }

    }

    /*public void buttonUpdate(Button btn){
        btn.setText(getid);
    }
     static void result_id(String id) {
        if (con == true && id != "")
            buttonId.setText(id);
    }*/
}