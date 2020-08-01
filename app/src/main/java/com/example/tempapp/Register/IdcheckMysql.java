package com.example.tempapp.Register;

import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IdcheckMysql extends Thread{

    public static boolean active=false;
    Handler mHandler = new Handler();
    String userId = null;
    String url = null;
    int gettype = 0;


    public IdcheckMysql(String id, int type){
        String idchk_url = "http://52.78.99.175/id_chk.php?id=";
        userId = id;

       url = idchk_url+userId;
       gettype = 8;

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

    void show(final String result){ // 3
        mHandler.post(new Runnable(){

            @Override
            public void run() {
                Log.e("CHECK! ",result);
                try {
                    JSONObject jObject = new JSONObject(result);    //인풋값을 JSON형태로 가져옴

                    String getid =jObject.get("u_id").toString();
                    //String getname=jObject.get("Name").toString();
                    //String getage =jObject.get("Age").toString();
                    //String getphone=jObject.get("Phone").toString();
                    //String getmail =jObject.get("Email").toString();
                    //String getaddress=jObject.get("Address").toString();

                    ChkId.result_idchk(getid, userId);
                } catch (JSONException e) {     //오류가 있으면 전부 false를 넘겨줌
                    e.printStackTrace();
                    ChkId.result_idchk("false", "false");
                }
            }
        });

    }


}
