package com.example.tempapp.Graph_2;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetMysql extends Thread {

    String userId = com.example.tempapp.Login.LoginActivity.etId.getText().toString();      // 사용자 id

    public static boolean active=false;

    static private ArrayList<DataCollection> mArrayList;


    Handler mHandler;
    String url = "http://52.78.99.175/getUserData.php?Id=";

    public GetMysql() {
        mHandler = new Handler();
        url += userId;
        mArrayList = new ArrayList<>();
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
                            jsonHtml.append(line);
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
        mHandler.post(new Runnable() {

            @Override
            public void run() {

                String TAG_JSON = "bow_db";
                String TAG_D = "Date";
                String TAG_E = "earR";
                String TAG_S = "shdR";
                String TAG_W = "wstR";

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        String date = item.getString(TAG_D);
                        float ear = Float.parseFloat(item.getString(TAG_E));
                        float shd = Float.parseFloat(item.getString(TAG_S));
                        float wst = Float.parseFloat(item.getString(TAG_W));

                        DataCollection data = new DataCollection();

                        data.setDate(date);
                        data.setEar(ear);
                        data.setShd(shd);
                        data.setWst(wst);

                        mArrayList.add(data);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERR: ", "showResult : ", e);
                }
            }
        });
    }

    public ArrayList<DataCollection> return_dc() { return mArrayList; }

}
