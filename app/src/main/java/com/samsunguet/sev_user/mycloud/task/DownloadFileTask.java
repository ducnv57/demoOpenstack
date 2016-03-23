package com.samsunguet.sev_user.mycloud.task;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.R;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;

import java.io.File;

/**
 * Created by sev_user on 3/15/2016.
 */
public class DownloadFileTask extends AsyncTask<StorageAPI, Integer, Boolean> {
    String source;
    String name;
    String des;
    BaseActivity mContext;
    int type = DataConstant.DOWNLOAD_ONLY;
    int notifyID;
    public static int NOTIFY_ID = 0;

    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(type == DataConstant.DOWNLOAD_AND_OPEN)
            mContext.showprogressingdialog("Loading...");
        else{
            notifyID = NOTIFY_ID++;
            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(mContext);
            mBuilder.setContentTitle("File Download")
                    .setContentText(name+" is downloading...")
                    .setSmallIcon(R.drawable.ic_cloud_download_black_18dp);
            mNotifyManager.notify(notifyID, mBuilder.build());
        }

    }

    public DownloadFileTask(BaseActivity context, String source, String name, String des) {
        this.mContext = context;
        this.source = source;
        this.name = name;
        this.des = des;
        //super();
    }

    public DownloadFileTask(BaseActivity context, String source, String name, String des, int type) {
        this.mContext = context;
        this.source = source;
        this.name = name;
        this.des = des;
        this.type = type;
        //super();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
//        mBuilder.setProgress(100, progress[0], false);
//        // Displays the progress bar on notification
//        mNotifyManager.notify(0, mBuilder.build());

    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
//        mBuilder.setContentText("Download complete")
//                // Removes the progress bar
//                .setProgress(0, 0, false);
//        //mNotifyManager.notify(1, mBuilder.build());

        if(type == DataConstant.DOWNLOAD_ONLY) {
//            mContext.showresultdialog("DownLoad Successful!");
            mNotifyManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder = new NotificationCompat.Builder(mContext);
            mBuilder.setContentTitle("File Download")
                    .setContentText(name+" is successful...")
                    .setSmallIcon(R.drawable.ic_cloud_download_black_18dp);
            Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            v.vibrate(500);
            mNotifyManager.notify(notifyID, mBuilder.build());
        }
        if(type == DataConstant.DOWNLOAD_AND_OPEN){
            mContext.showdialog(false);
            DataConstant.openPrivateFile(mContext, new File(des + "/" + name));

        }

    }

    @Override
    protected Boolean doInBackground(StorageAPI... params) {
        return params[0].downloadFile(source, name, des);
    }
}
