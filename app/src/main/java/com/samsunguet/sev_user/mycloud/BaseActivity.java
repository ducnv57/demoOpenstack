package com.samsunguet.sev_user.mycloud;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.samsunguet.sev_user.mycloud.object.MyFolder;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sev_user on 3/17/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public static SweetAlertDialog pDialog;

    MyFolder myFolder = new MyFolder();

    public void setMyFolder(MyFolder folder){
        this.myFolder = folder;
    }
    public MyFolder getMyFolder(){
        return myFolder;
    }

    abstract public void setData();
    abstract public void refresh();
    public void showresultdialog(String message){
        pDialog.dismissWithAnimation();
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(message);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();

            }
        });
        pDialog.show();
    }
    public void showprogressingdialog(String message){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    public void showdialog(boolean show){
        if(!show) pDialog.hide();
    }
    abstract public ListView getLvFile();
    abstract public ListView getLvFolder();
}
