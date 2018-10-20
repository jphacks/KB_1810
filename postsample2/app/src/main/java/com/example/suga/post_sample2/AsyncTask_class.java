package com.example.suga.post_sample2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public final class AsyncTask_class extends AsyncTask<String, Void, String> {

        public Listener listener;

        // 非同期処理
        @Override
        protected String doInBackground(String... params) {


            // 取得したテキストを格納する変数
            final StringBuilder result_builder = new StringBuilder();

            HttpURLConnection con = null;
            String result = null;
           // String word = "word="+params[0];
            String word = "http://153.126.157.135/path?url=/home/yamauchi/photo/00000002.jpg";
            try {
                // URL設定
                URL url = new URL(word);
                //URL url = new URL("http://weather.livedoor.com/forecast/webservice/json/v1?city=400040");

                // HttpURLConnection
                con = (HttpURLConnection) url.openConnection();
                // request Get
                con.setRequestMethod("GET");

                // no Redirects
                //con.setInstanceFollowRedirects(false);

                // データを書き込む
                //con.setDoOutput(true);

                // 時間制限
                //con.setReadTimeout(10000);
                //con.setConnectTimeout(20000);

                // 接続
                con.connect();

                // POSTデータ送信処理
                OutputStream outStream = null;

                try {
                    outStream = con.getOutputStream();
                    outStream.write( word.getBytes("UTF-8"));
                    outStream.flush();
                    Log.d("debug","flush");
                } catch (IOException e) {
                    // POST送信エラー
                    e.printStackTrace();
                    result="POST送信エラー";
                } finally {
                    if (outStream != null) {
                        outStream.close();
                    }
                }

                //con = (HttpURLConnection) url.openConnection();
                //con.connect();

                // HTTPレスポンスコード
                final int status = con.getResponseCode();
                if (status == HttpURLConnection.HTTP_OK) {
                    // 通信に成功した
                    // テキストを取得する
                    StringBuilder result01 = new StringBuilder();
                    final InputStream in = con.getInputStream();
                    //final String encoding = con.getContentEncoding();
                    final InputStreamReader inReader = new InputStreamReader(in);
                    final BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;

                    // 1行ずつテキストを読み込む
                    while((line = bufReader.readLine()) != null) {
                        result01.append(line);
                    }
                    bufReader.close();
                    inReader.close();
                    in.close();

                    result=result01.toString();

                }
                //else{
                //  result="status="+String.valueOf(status);
                //}

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return result;
        }




    // 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (listener != null) {
            listener.onSuccess(result);
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onSuccess(String result);
    }
}