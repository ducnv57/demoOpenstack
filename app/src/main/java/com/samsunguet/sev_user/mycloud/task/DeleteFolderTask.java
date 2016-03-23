package com.samsunguet.sev_user.mycloud.task;

import android.os.AsyncTask;

import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;
import com.samsunguet.sev_user.mycloud.object.MyFile;
import com.samsunguet.sev_user.mycloud.object.MyFolder;

/**
 * Created by sev_user on 3/18/2016.
 */
public class DeleteFolderTask extends AsyncTask<StorageAPI, Void, Void> {

    BaseActivity context;
    MyFolder myFolder;

    public DeleteFolderTask(BaseActivity context, MyFolder folder){
        this.context = context;
        myFolder = folder;
    }

    private void delete(MyFolder folder){
        for(int i=0; i<folder.getSubFiles().size(); i++){
            MyFile file = folder.getSubFiles().get(i);
            DataConstant.storageAPI.deleteFileorEmptyfolder(file.getPath()+"/"+file.getName());
        }
        for(int i=0; i<folder.getSubFolders().size(); i++){
            MyFolder folder1 = folder.getSubFolders().get(i);
            delete(folder1);
        }
        DataConstant.storageAPI.deleteFileorEmptyfolder(folder.getPath() + "/" + folder.getName()+"/");
    }


    @Override
    protected Void doInBackground(StorageAPI... params) {
        delete(myFolder);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        context.refresh();
    }
}
