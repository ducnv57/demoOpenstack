package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;
import com.samsunguet.sev_user.mycloud.log.MyLog;

/**
 * Created by sev_user on 3/14/2016.
 */
public class DeleteFileTask extends AsyncTask<StorageAPI, Void, Boolean> {
    BaseActivity mContext;
    String path;
    public DeleteFileTask(BaseActivity mContext,String path) {
        this.mContext = mContext;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(StorageAPI... params) {
        try{
            MyLog.log("DELETE FILE PATH: " + this.path);
            return params[0].deleteFileorEmptyfolder(this.path);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mContext.refresh();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
