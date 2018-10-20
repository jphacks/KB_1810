package com.example.suga.post_sample2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private AsyncTask_class task;
    private TextView textView;
    // wordを入れる
    private EditText editText;
    //名前と年齢を入れる
    private TextView text_name;
    private TextView text_age;
    private TextView text;


    // phpがPOSTで受け取ったwordを入れて作成するHTMLページ(適宜合わせてください)
    //String url = "http://weather.livedoor.com/forecast/webservice/json/v1?city=400040";
    String url = "http://153.126.157.135/good?name=yamauchi&age=4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.uriname);

        Button post = findViewById(R.id.post);
        Button en = findViewById(R.id.en);
        // ボタンをタップして非同期処理を開始
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String param0 = editText.getText().toString();

                if(param0.length() != 0){
                    task = new AsyncTask_class();
                    task.setListener(createListener());
                    task.execute(param0);
                }

            }
        });
        // 円グラフ
        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main3 = new Intent(MainActivity.this,
                        Main3Activity.class);
                startActivity(main3);
            }
        });
        // ブラウザを起動する
        Button browser = findViewById(R.id.browser);
        browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new post_sample().execute(new URL("http://153.126.157.135/test?tes=00000"));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });

        // 写真のアップロード
        Button Get = findViewById(R.id.get);
        Get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main2 = new Intent(MainActivity.this,
                        Main2Activity.class);
                startActivity(main2);
            }
        });

        textView = findViewById(R.id.text_view);
        text_name= findViewById(R.id.name);
        text_age= findViewById(R.id.age);
        text= findViewById(R.id.text);
    }


    @Override
    protected void onDestroy() {
        task.setListener(null);
        super.onDestroy();
    }

    private AsyncTask_class.Listener createListener() {
        return new AsyncTask_class.Listener() {

            @Override
            public void onSuccess(String result) {
                try {
                    Json(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                textView.setText(result);

            }
        };
    }

    //jsonをtextviewに入れてる
    private void Json(String result) throws JSONException {
        try {
            JSONObject json = new JSONObject(result);
            text_age.setText(json.getString("age"));
            text_name.setText(json.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
