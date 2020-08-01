package com.example.tempapp.Login;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FindingPwMysql extends Thread{
    public static boolean active=false;
    Handler mHandler=new Handler();

    String userId = null;
    String userEmail = null;
    String userPw = null;

    String url=null;
    int gettype=0;

    public FindingPwMysql(String pw, String mail) { //for update, sending pw

        String findingpw="https://52.78.99.175/find_pw.php?id=";     //your server IP
        String msg=pw;  //msg = password
        String mailuser="&mail="+mail; // mail

        url=findingpw+msg+mailuser;
        gettype=4;
    }

    public FindingPwMysql(String id, String name, String mail) { //for update, finding pw

        String findingpw = "http://52.78.99.175/find_pw.php?id=";
        userId=id;
        userEmail = mail;

        String nameuser="&name="+name;
        String mailuser="&mail="+mail;

        url=findingpw+userId+nameuser+mailuser;
        gettype=1;
    }



    @Override
    public void run() {
        super.run();


        if(active){
            Log.e("gettype",gettype+","+url);
            StringBuilder jsonHtml = new StringBuilder();

            try {
                URL phpUrl = new URL(url);

                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            show(jsonHtml.toString());
        }

    }

    public void show(final String result){
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                switch (gettype) {
                    case 1:
                        try {
                            JSONObject jObject = new JSONObject(result);    //인풋값을 JSON형태로 가져옴

                            String getid = jObject.get("u_id").toString();       //take id from db
                            String getmail = jObject.get("u_mail").toString();  //take mail from db

                            String getpw = jObject.get("u_pw").toString(); //take pw from db

                            LoginActivity.result_findingpw(getpw, getid, getmail, userId, userEmail);


                        } catch (JSONException e) {     //오류가 있으면 전부 false를 넘겨줌
                            e.printStackTrace();
                            LoginActivity.result_findingpw("false", "false", "false", "false", "false");
                        }

                    case 4:
//                        try {
//                            JSONObject jObject = new JSONObject(result);    //인풋값을 JSON형태로 가져옴
//                            String getpw = jObject.get("u_pw").toString();       //take pw from db
//
//                            LoginActivity.result_sendingpw(getpw);
//
//                        }catch(JSONException e){
//                            e.printStackTrace();;
//                            LoginActivity.result_sendingpw("false");
//                        }
                }
            }
        });

    }

}
