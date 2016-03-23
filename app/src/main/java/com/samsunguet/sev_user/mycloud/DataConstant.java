package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.samsunguet.sev_user.mycloud.api.StorageAPI;
import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.MyFile;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.object.User;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sev_user on 3/10/2016.
 */
public class DataConstant {
    public static boolean isLogIn               = false;
    public static boolean autoLogIn             = true;
    public static User USER_DEFAULT = null;
    public static StorageAPI storageAPI = null;
    public static MyFolder ROOT_FOLDER = null;

    public static final String DOWNLOADED_FOLDER = "/OfflineFile";
    public static final String CACHE_FOLDER = "/cachefile";
    public static final String PHOTO_PATH = "/My Photos";


    //sharepreference file
    public static final String PREFERENCE       = "PREFERENCE";

    public static final String ABSOLUTEPATH     = "absolutepath";

    //setting data
    public static final String CONTACTSYNC      = "contactsync";
    public static final String PHOTOSYNC        = "photosync";

    //login file
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String TOKEN = "token";

    public static final int DOWNLOAD_ONLY = 0;
    public static final int DOWNLOAD_AND_OPEN = 1;

    public static final String SERACHKEY = "searchkey";

    public static Date getDatefromString(String datestr){
        SimpleDateFormat format;
        if(datestr.charAt(datestr.length()-1)=='Z')
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        else
            format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try{

            Date ep = format.parse(datestr);
            return ep;

        }catch (Exception e){
            MyLog.log(e.toString());
            return new Date(System.currentTimeMillis());
        }
    }
    public static String formatDate(Date date){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        return df.format(date);
    }

    public static String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return "";
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }
    public static MyFolder getFolderfromRoot(String path){
        String strarr[] = path.split("/");
        MyFolder result = ROOT_FOLDER;
        for(int i=1; i<strarr.length; i++){
            for(int j=0; j<result.getSubFolders().size(); j++){
                if(result.getSubFolders().get(j).getName().compareTo(strarr[i])==0){
                    result = result.getSubFolders().get(j);
                    break;
                }
            }
        }
        return result;
    }

    public static boolean checkAutherial(Activity activity){
        MyLog.log("Entered checkautherial: "+USER_DEFAULT.getUsername());
        if(USER_DEFAULT.getUsername().equals("demo"))
            if(isLogIn)return true;
            else return false;
        if(USER_DEFAULT.getToken().isExpire() || !isLogIn){
            isLogIn = false;
            autoLogIn = false;
            USER_DEFAULT = null;
            ROOT_FOLDER = null;
            storageAPI = null;

            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);
            activity.finish();
            return false;
        }
        return true;
    }

    public static void openPrivateFile(Context mContext, File myFile){
        MimeTypeMap myMime = MimeTypeMap.getSingleton();
        if(myMime == null) MyLog.log("can not get mymime");
        Intent newIntent = new Intent(Intent.ACTION_VIEW);

        String mimeType = myMime.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(myFile.getAbsolutePath()));
        if(mimeType==null) MyLog.log("cannot get file type");
        else MyLog.log(mimeType);
        newIntent.setDataAndType(Uri.fromFile(myFile), mimeType);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        Uri contentUri = FileProvider.getUriForFile(mContext,
                "com.samsunguet.sev_user.mycloud.OfflineFileActivity", myFile);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_VIEW);
        shareIntent.setType(mimeType);
        shareIntent.setData(contentUri);
        //shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try {
            mContext.startActivity(shareIntent);
            //mContext.startActivity(Intent.createChooser(shareIntent, "Open with"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "No handler for this type of file.", Toast.LENGTH_LONG).show();
        }
    }

    public static void clearCacheFile(Context context){
        File file = new File(context.getFilesDir()+CACHE_FOLDER);
        if(!file.exists()||!file.isDirectory()) return;
        File[] listfile = file.listFiles();
        for(File f:listfile){
            if(!f.isDirectory()) f.delete();
        }
    }

    public static void addListtoList(ArrayList<MyFile> sourcelist, ArrayList<MyFile> deslist){
        for(int i=0;i <sourcelist.size(); i++)
            deslist.add(sourcelist.get(i));
    }
    public static void addListtoList(ArrayList<MyFolder> sourcelist, ArrayList<MyFolder> deslist, boolean folder){
        for(int i=0;i <sourcelist.size(); i++)
            deslist.add(sourcelist.get(i));
    }
}
