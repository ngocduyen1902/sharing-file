package com.example.ngocduyen.demo2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent mRequestFileIntent;
    private ParcelFileDescriptor mInputPFD;
    private Button btnGetFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGetFile = (Button) findViewById(R.id.btnGetFile);
        btnGetFile.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnGetFile:
                requestFile();
                break;
        }
    }
    protected void requestFile() {
        //Bước 1: Gởi 1 request yêu cầu lấy file
        mRequestFileIntent = new Intent(Intent.ACTION_PICK);
        mRequestFileIntent.setType("image/jpg");
        startActivityForResult(mRequestFileIntent, 0);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent returnIntent) {
        // Bước 2: Xử lý sau khi nhận được dữ liệu trả về - sharing app finish
        if (resultCode != RESULT_OK) {
            return;
        } else {

            // Get the file's content URI from the incoming Intent
            Uri returnUri = returnIntent.getData();
            //Lấy Loại file
            String mimeType = getContentResolver().getType(returnUri);

            Cursor returnCursor = getContentResolver().query(returnUri, null, null, null, null);
            // Lấy file name
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            // Lầy file size
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
            returnCursor.moveToFirst();

            TextView nameView = (TextView) findViewById(R.id.txtFileName);
            ImageView imgImg = (ImageView) findViewById(R.id.imgImage);

            nameView.setText("File name: " + returnCursor.getString(nameIndex));
            nameView.append("\n\nFile size: "+ returnCursor.getLong(sizeIndex));
            nameView.append("\n\nFile type: "+ mimeType);
            imgImg.setImageURI(returnUri);
        }
    }



}
