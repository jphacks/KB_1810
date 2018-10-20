package com.example.suga.post_sample2;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class post_sample extends AsyncTask<URL, Void, Void> {




    protected Void doInBackground(URL... urls) {
        String result=null;
        final URL url = urls[0];
        HttpURLConnection con = null;
        try {




            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("User-Agent", "@IT java-tips URLConnection");// ヘッダを設定
            con.setRequestProperty("Accept-Language", "ja");// ヘッダを設定


            OutputStream os = con.getOutputStream();
            String postStr = "http://153.126.157.135/test?tes=00000";//POSTするデータ
            PrintStream ps = new PrintStream(os);
            ps.print(postStr);//データをPOSTする
            ps.close();



            //con.setChunkedStreamingMode(0);
            //con.connect();

            // POSTデータ送信処理
            OutputStream out = null;
            try {
                out = con.getOutputStream();
                out.write("POST DATA".getBytes("UTF-8"));
                out.flush();
            } catch (IOException e) {
                // POST送信エラー
                e.printStackTrace();
            } finally {
                if (out != null) {
                    out.close();
                }
            }




            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {


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

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    return null;
}

}
