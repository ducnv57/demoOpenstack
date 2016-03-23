package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.samsunguet.sev_user.mycloud.adapter.OfflineFileAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sev_user on 3/16/2016.
 */
public class OfflineFileActivity extends Activity {
    ImageView imvBack;
    ListView lvFile;
    File mDirectory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offlinefile_activity);

        imvBack = (ImageView) findViewById(R.id.imgBackInOF);
        lvFile = (ListView) findViewById(R.id.lvFileOF);

        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //read lvFile for list
        ArrayList<File> myfiles = new ArrayList<File>();
        String link = this.getFilesDir().toString() + DataConstant.DOWNLOADED_FOLDER;
        mDirectory = new File(link);
//        MyLog.log("link: " + link + " folder size: " + mDirectory.list().length);

        if (mDirectory.exists()) {
            File[] files = mDirectory.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) {
                    myfiles.add(file);
                }
            }
        }

        OfflineFileAdapter adapter = new OfflineFileAdapter(this, myfiles);
        lvFile.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        if (!DataConstant.checkAutherial(this)) {

        }
        super.onResume();
    }
}
