package com.example.tempapp.Result;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class SetMysql  {

    String userId = com.example.tempapp.Login.LoginActivity.etId.getText().toString();      // 사용자 id

    public static boolean active=false;
    float ear, shd, wst;

    String url = "http://52.78.99.175/setUserData.php";

    public SetMysql() {

        // 결과값 각 fragment에서 각도 값 가져오기
        ear = FirstFragment.getResult_E();
        shd = SecondFragment.getResult_S();
        wst = ThirdFragment.getResult_W();
        Log.e("CHECK>> ", ear+" " +shd+" "+wst);

        InsertData insert = new InsertData();
        insert.execute(url, userId, String.valueOf(ear), String.valueOf(shd), String.valueOf(wst));
    }

}


class InsertData extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String ID = (String)params[1];
        float ear = Float.valueOf(params[2]);
        float shd = Float.valueOf(params[3]);
        float wst = Float.valueOf(params[4]);

        String serverURL = (String)params[0];
        String postParameters = "Id=" + ID + "&Ear=" + ear + "&Shd=" + shd + "&Wst=" + wst;

        try {

            URL url = new URL(serverURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.connect();


            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(postParameters.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();


            int responseStatusCode = httpURLConnection.getResponseCode();

            InputStream inputStream;
            if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
            else{
                inputStream = httpURLConnection.getErrorStream();
            }


            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = bufferedReader.readLine()) != null){
                sb.append(line);
            }


            bufferedReader.close();


            return sb.toString();


        } catch (Exception e) {
            return new String("Error: " + e.getMessage());
        }

    }
}

