package com.example.tempapp.Pic;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/* 사진정보 PHP 전송 */
public class PHPRequest_pic {
    private String url;

    public PHPRequest_pic(String url) { this.url = url; }

    public void PHPTest(final String data1, final String data2) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ProcImage proc = new ProcImage();
        proc.execute(url, data1, data2);
    }
}


class ProcImage extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {

        String url = (String) params[0];
        String id = (String) params[1];
        String image = (String) params[2];

        String postParameters = "Id=" + id + "&Image=" +  image;

        try {

            URL server_url =  new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection)server_url.openConnection();

            httpURLConnection.setReadTimeout(30*1000);
            httpURLConnection.setConnectTimeout(30*1000);
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

            Log.e("CHECK",sb.toString());
            return sb.toString();
        }
        catch (Exception e) {
            Log.e("PHPRequest ERR! ",e.toString());
            return null;
        }

    }
}