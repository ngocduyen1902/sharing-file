package com.example.ngocduyen.demo1;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private Button btnDone;
    private File mPrivateRootDir;
    private File mImagesDir;
    File[] mImageFiles;
    String[] mImageFilenames;
    ListView mFileListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        btnDone = (Button)findViewById(R.id.btnDone);
        btnDone.setOnClickListener(this);

        // Bước 2: Lấy danh và hiển thị sách file chia sẻ vào listview
        mPrivateRootDir = getFilesDir();
        mImagesDir = new File(mPrivateRootDir, "images");
        mImageFiles = mImagesDir.listFiles();

        final ArrayList<String> mImageString = new ArrayList<>();
        for(int i = 0 ; i<mImageFiles.length;i++)
        {
            mImageString.add(mImageFiles[i].getName());
        }
        mFileListView = (ListView)findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mImageString);
        mFileListView.setAdapter(adapter);

        //Bước 3: Tạo URI sử dụng FileProvider
        mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long rowId) {
                File requestFile = new File( mImageFiles[position].getAbsolutePath());
                try {
                    Uri fileUri = FileProvider.getUriForFile(
                            MainActivity.this,
                            "com.example.ngocduyen.demo1.fileprovider",
                            requestFile);
                    //Bước 4: Intent
                    Intent mResultIntent = new Intent("com.example.ngocduyen.demo1.ACTION_RETURN_FILE");
                    if (fileUri != null) {
                        //Bước 5: Cấp quyền tạm thời
                        mResultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        //Bước 6: Chia sẻ file khi “Requesting App” gởi yêu cầu.

                        mResultIntent.setDataAndType(fileUri, getContentResolver().getType(fileUri));
                        MainActivity.this.setResult(Activity.RESULT_OK, mResultIntent);

                        //Bước 7: Quay lại “Requesting App”
                        finish();
                    } else {
                        mResultIntent.setDataAndType(null, "");
                        MainActivity.this.setResult(RESULT_CANCELED, mResultIntent);
                    }

                } catch (IllegalArgumentException e) {
                    Log.e("File Selector", "The selected file can't be shared: " + requestFile.toString());
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnDone:
                finish();
                break;
        }
    }
}
