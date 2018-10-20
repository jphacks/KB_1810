package com.example.suga.post_sample2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {


    private static final int RESULT_PICK_IMAGEFILE = 1001;
    private TextView textView;
    private ImageView imageView;
    private ImageView imageView2;
    private Button btn1;
    public Bitmap img;
    private final int TARGET_WIDTH = 300;
    private final int TARGET_HEIGHT = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        textView = findViewById(R.id.text_view);

        imageView = findViewById(R.id.image_view);
        imageView2 = (ImageView)findViewById(R.id.imageView2);
        Button button = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(intent, RESULT_PICK_IMAGEFILE);

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();}
            }
        });
    }


      // your intent here


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == RESULT_PICK_IMAGEFILE && resultCode == Activity.RESULT_OK) {

            if(resultData.getData() != null){
                ParcelFileDescriptor pfDescriptor = null;
                try{
                    Uri uri = resultData.getData();
                    textView.setText(
                            String.format(Locale.US, "Uri:　%s",uri.toString()));
                    pfDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                    if(pfDescriptor != null){
                        FileDescriptor fileDescriptor = pfDescriptor.getFileDescriptor();

                        Bitmap bmp = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        imageView.setImageBitmap(bmp);
                        new PostBmpAsyncHttpRequest(this).execute(new Param("http://153.126.157.135/good", bmp));
                        pfDescriptor.close();
                        imageView.setImageBitmap(bmp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try{
                        if(pfDescriptor != null){
                            pfDescriptor.close();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


            }
        }
    }


}