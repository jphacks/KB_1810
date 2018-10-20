package com.example.suga.post_sample2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PostBmpAsyncHttpRequest extends AsyncTask<Param, Void, String> {
    private Activity mActivity;

    public PostBmpAsyncHttpRequest(Activity activity) {
        mActivity = activity;
    }

    @Override
    protected String doInBackground(Param... params) {
        Param param = params[0];
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();
        try {
            // 画像をjpeg形式でstreamに保存
            ByteArrayOutputStream jpg = new ByteArrayOutputStream();
            param.bmp.compress(Bitmap.CompressFormat.JPEG, 100, jpg);


            URL url = new URL(param.uri);
            connection = (HttpURLConnection) url.openConnection();
            //connection.setConnectTimeout(3000);//接続タイムアウトを設定する。
            //connection.setReadTimeout(3000);//レスポンスデータ読み取りタイムアウトを設定する。
            connection.setRequestMethod("POST");//HTTPのメソッドをPOSTに設定する。
            //connection.setRequestMethod("GET");//HTTPのメソッドをPOSTに設定する。
            //ヘッダーを設定する
            connection.setRequestProperty("User-Agent", "Android");
            connection.setRequestProperty("Content-Type","application/octet-stream");
            connection.setDoInput(true);//リクエストのボディ送信を許可する
            connection.setDoOutput(true);//レスポンスのボディ受信を許可する
            //connection.setFixedLengthStreamingMode(length);
            //connection.setUseCaches(false);//キャッシュを使用しない
            connection.connect();

            // データを投げる
            OutputStream ou = connection.getOutputStream();
            //OutputStream out = new BufferedOutputStream(ou);
            //out.write(jpg.toByteArray());
            //out.flush();


            String postStr = "http://153.126.157.135/test?tes=00000";//POSTするデータ
            PrintStream ps = new PrintStream(ou);
            ps.print(jpg.toByteArray());//データをPOSTする



            int responseCode = connection.getResponseCode();
            if(responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP responseCode: " + responseCode);
            }


            // データを受け取る
            InputStream is = connection.getInputStream();

            String i=null;
            InputStreamReader	ir1 = new InputStreamReader(is);
            BufferedReader	br1 = new BufferedReader(ir1);

            String line = "";
            while ((line = br1.readLine()) != null)
                sb.append(line);
            ir1.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            connection.disconnect();
        }
        return sb.toString();
    }

    public void onPostExecute(String string) {
        // 戻り値をViewにセット
        TextView textView = (TextView) mActivity.findViewById(R.id.text_view);
        textView.setText(string);
    }
}