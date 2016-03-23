package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;

/**
 * Created by sev_user on 3/16/2016.
 */
public class CreateFolderTask extends AsyncTask<StorageAPI, Integer, Boolean> {
    BaseActivity mContext;
    String path;

    public CreateFolderTask(BaseActivity context,String path) {
        this.mContext = context;
        this.path = path;
    }

    @Override
    protected Boolean doInBackground(StorageAPI... params) {
        return params[0].createFolder(this.path);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        mContext.refresh();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }
}
