package com.january.transfile;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity" ;
    DataTrans trans = new DataTrans();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendFile() {
        Uri data_uri;
        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, "sendFile: no data to send");
            return;
        }

        data_uri = intent.getParcelableExtra(intent.EXTRA_STREAM);
        if (data_uri == null) {
            Log.d(TAG, "sendFile: no data in intent");
            return;
        }

        final Uri final_data_url = data_uri;
        Thread sending_thread = new Thread(new Runnable() {
            // sending thread
            @Override
            public void run() {
                try {
                    byte[] buf = new byte[4096];
                    if (!trans.isConected()) {
                        trans.connect("127.0.0.1", 19999);
                    }
                    InputStream read_stream = getContentResolver().openInputStream(final_data_url);
                    while (true) {
                        int len = read_stream.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        trans.send(buf);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "sending: error send file");
                }
                Log.d(TAG, "sending: file sended");
            }
        });
        sending_thread.start();

    }

    public void onButtonTestClick(View v) {
        sendFile();
        Log.d(TAG, "onButtonTestClick: ");
    }
}
