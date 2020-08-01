package com.example.tempapp.Login;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2015-11-13.
 */
public class loginMysql extends Thread {

    public static boolean active=false;


    Handler mHandler;
    String userId=null,userPw=null,url=null;
    String login_url = "http://52.78.99.175/login.php?id=";

    public loginMysql(String id,String pw){
        mHandler = new Handler();
        userId=id;          //로그인 창에 입력한 id
        userPw=pw;          //로그인 창에 입력한 password
        url = login_url+userId;

    }

    /**
     * Calls the <code>run()</code> method of the Runnable object the receiver
     * holds. If no Runnable is set, does nothing.
     *
     * @see Thread#start
     */

    @Override
    public void run() {
        super.run();

        if(active){
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

    void show(final String result){
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                try {
                    JSONObject jObject = new JSONObject(result);    //인풋값을 JSON형태로 가져옴

                    String getpw =jObject.get("u_pw").toString();
                    LoginActivity.result_login(getpw, userPw);
                } catch (JSONException e) {
                    e.printStackTrace();
                    LoginActivity.result_login("false", "false");
                }
            }
        });
    }

}
