package com.january.transfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity" ;
    DataTrans trans = new DataTrans();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "starting");
    }

    public void onButtonTestClick(View v) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    trans.test();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Log.d(TAG, "onButtonTestClick: ");
}
}
