package com.samsunguet.sev_user.mycloud.task;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.LoginActivity;
import com.samsunguet.sev_user.mycloud.MainActivity;
import com.samsunguet.sev_user.mycloud.api.IdentifyAPI;

/**
 * Created by sev_user on 3/10/2016.
 */
public class LoginTask extends AsyncTask<IdentifyAPI, Void, Boolean> {

    Context mContext;
    public LoginTask(Context context){
        mContext = context;
    }
    @Override
    protected Boolean doInBackground(IdentifyAPI... params) {

        return params[0].setTokenandStorageurl();
    }

    @Override
    protected void onProgressUpdate(Void... values) {

        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean) {
            DataConstant.isLogIn = true;
            mContext.getSharedPreferences("PREFERENCE", mContext.MODE_PRIVATE).edit().putString(DataConstant.USERNAME, DataConstant.USER_DEFAULT.getUsername()).commit();
            mContext.getSharedPreferences("PREFERENCE", mContext.MODE_PRIVATE).edit().putString(DataConstant.PASSWORD, DataConstant.USER_DEFAULT.getPassword()).commit();
            Intent main = new Intent(mContext, MainActivity.class);
            mContext.startActivity(main);
            //MyLog.log("is expire: "+LoginActivity.USER_DEFAULT.getToken().isExpire());
            ((LoginActivity) mContext).finish();
        }else{
            Toast.makeText(mContext, "Login Failed", Toast.LENGTH_SHORT).show();
            CircularProgressBar circularProgressBar = ((LoginActivity)mContext).getCircularProgressBar();
            circularProgressBar.setVisibility(View.GONE);
            LinearLayout llLogin = ((LoginActivity) mContext).getLlLogin();
            for(int i=0; i<llLogin.getChildCount(); i++){
                View v = llLogin.getChildAt(i);
                v.setEnabled(true);
            }
        }

        //super.onPostExecute(aBoolean);
    }
}
