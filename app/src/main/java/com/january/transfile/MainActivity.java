package com.january.transfile;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
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
        checkShareFile();
    }

    //callback of sendFile
    public void onSendFileReturned(boolean success) {
        if (success) {
            Toast.makeText(this, "send success", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onSendFileReturned: success");
        } else {
            Toast.makeText(this, "send failed", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onSendFileReturned: failed");
        }
    }

    // make sure the connection has been established before call this method.
    public void sendFile(Uri uri) {
        final Uri final_data_url = uri;
        Thread sending_thread = new Thread(new Runnable() {
            // sending thread
            @Override
            public void run() {
                boolean success = true;
                byte[] buf = new byte[4096];
                InputStream read_stream = null;
                main:try {
                    read_stream = getContentResolver().openInputStream(final_data_url);
                    if (read_stream == null) {
                        success = false;
                        break main;
                    }
                    while (true) {
                        int len;
                        len = read_stream.read(buf);
                        if (len <= 0) {
                            break;
                        }
                        trans.send(buf);
                    }
                    Log.d(TAG, "sending: file sended");
                } catch (Exception e) {
                    success = false;
                    Log.d(TAG, "sending: " + e.getMessage());
                }
                // return
                onSendFileReturned(success);
            }
        });
        sending_thread.start();
    }

    public void checkShareFile() {
        Uri data_uri;
        Intent intent = getIntent();
        TextView file_info = (TextView)findViewById(R.id.fileInfo);
        if (intent == null) {
            file_info.setText("no file");
            Log.d(TAG, "no intent");
            return;
        }

        data_uri = intent.getParcelableExtra(intent.EXTRA_STREAM);
        if (data_uri == null) {
            file_info.setText("no file");
            Log.d(TAG, "no data in intent");
            return;
        }

        Cursor cursor =
                getContentResolver().query(data_uri, null, null, null, null);
        int name_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(name_index);
        file_info.setText(name);
    }

    public void onButtonSendClick(View v) {
        Uri data_uri;
        Intent intent = getIntent();
        if (intent == null) {
            Log.d(TAG, "no intent");
            return;
        }

        data_uri = intent.getParcelableExtra(intent.EXTRA_STREAM);
        if (data_uri == null) {
            Log.d(TAG, "no data in intent");
            return;
        }
        sendFile(data_uri);
        Log.d(TAG, "onButtonTestClick: ");
    }
}
