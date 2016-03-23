package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;
import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.MyFolder;

/**
 * Created by sev_user on 3/10/2016.
 */
public class LoadFolderTask extends AsyncTask<StorageAPI, Void, MyFolder> {

    BaseActivity context;
    String path;


    public LoadFolderTask(BaseActivity context, String path){
        this.context = context;
        this.path    = path;
    }
    @Override
    protected MyFolder doInBackground(StorageAPI... params) {
        MyLog.log("entered loadfolder task");
        return params[0].getFolderandFileList("/" + DataConstant.USER_DEFAULT.getUsername());
    }

    @Override
    protected void onPostExecute(MyFolder myFolders) {
        //super.onPostExecute(myFolders);
        DataConstant.ROOT_FOLDER = myFolders;
        context.setMyFolder(DataConstant.getFolderfromRoot(path));
        context.setData();

    }
}
