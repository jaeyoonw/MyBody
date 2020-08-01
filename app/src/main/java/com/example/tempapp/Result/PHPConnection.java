package com.example.tempapp.Result;

import android.os.Handler;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2015-11-13.
 */
public class PHPConnection extends Thread {

    //public static ArrayList<PersonalData> mArrayList;
    public static boolean active=false;

    String userId = com.example.tempapp.Login.LoginActivity.etId.getText().toString();      // 사용자 id

    static PersonalData pd = new PersonalData();
    Handler mHandler;
    String url = "http://52.78.99.175/data.php?Id=";

    public PHPConnection() {
        mHandler = new Handler();
        url += userId;
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
                String TAG_X = "x";
                String TAG_Y = "y";

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

                    for (int i=0; i<jsonArray.length(); i++) {
                        JSONObject item = jsonArray.getJSONObject(i);

                        String x = item.getString(TAG_X);
                        String y = item.getString(TAG_Y);

                        if (i < 2) {
                            pd.setEar_X(Float.parseFloat(x), i);
                            pd.setEar_Y(Float.parseFloat(y), i);
                        } else if (i < 4) {
                            pd.setShoulder_X(Float.parseFloat(x), i-2);
                            pd.setShoulder_Y(Float.parseFloat(y), i-2);
                        } else if (i < 6) {
                            pd.setWaist_X(Float.parseFloat(x), i-4);
                            pd.setWaist_Y(Float.parseFloat(y), i-4);
                        }

                    }

                    FirstFragment.result_PHP(pd);
                    SecondFragment.result_PHP(pd);
                    //ThirdFragment.result_PHP(pd);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("ERR: ", "showResult : ", e);
                }

            }
        });
    }

    public PersonalData return_pd() { return pd; }

}
