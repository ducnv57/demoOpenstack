package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;

/**
 * Created by sev_user on 3/18/2016.
 */
public class ReNameTask extends AsyncTask<StorageAPI, Void, Boolean> {

    BaseActivity context;
    String path;
    String newname;
    public ReNameTask(BaseActivity context, String path, String newname){
        this.path = path;
        this.newname = newname;
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(StorageAPI... params) {
//        boolean result = params[0].copyFile(path, newname);
//        params[0].deleteFileorEmptyfolder(path);
        return null;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        context.refresh();
    }
}
