package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by sev_user on 3/14/2016.
 */
public class UploadFileTask extends AsyncTask<StorageAPI, Void, Boolean> {
    BaseActivity mContext;
    ArrayList<String> source;
    String des;

    public UploadFileTask(BaseActivity context, ArrayList<String> source, String des) {
        mContext = context;
        this.source = source;
        this.des = des;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        mContext.showresultdialog("UpLoad Successful " + source.size() + " files");
        mContext.refresh();

    }

    @Override
    protected Boolean doInBackground(StorageAPI... params) {
        try {
            return params[0].uploadFile(source, des);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return false;

    }


}
