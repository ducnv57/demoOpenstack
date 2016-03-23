package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.samsunguet.sev_user.mycloud.IntroApp.example.introApp.Intro;
import com.samsunguet.sev_user.mycloud.api.HttpConstant;
import com.samsunguet.sev_user.mycloud.api.IdentifyAPI;
import com.samsunguet.sev_user.mycloud.api.StorageAPI;
import com.samsunguet.sev_user.mycloud.object.Tenant;
import com.samsunguet.sev_user.mycloud.object.User;
import com.samsunguet.sev_user.mycloud.task.LoadFolderTask;
import com.samsunguet.sev_user.mycloud.task.LoginTask;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by MeHuong on 3/5/2016.
 */
public class LoginActivity extends Activity {
    FancyButton btnSignIn,btnSignUp;
    EditText edtUserName, edtPassword;

    CircularProgressBar circularProgressBar;
    LinearLayout llLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        DataConstant.USER_DEFAULT = new User();
        DataConstant.storageAPI = new StorageAPI(DataConstant.USER_DEFAULT);
//        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, null, null, null);
//
//        while (cursor.moveToNext()) {
//            String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//            String name      = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//
//            System.out.println(contactId + "   " + name);
//        }
//        cursor.close();

        btnSignIn = (FancyButton) findViewById(R.id.btnSignIn);
        edtUserName = (EditText) findViewById(R.id.edtUserNameInLogIn);
        edtPassword = (EditText) findViewById(R.id.edtPasswordInLogIn);
        llLogin = (LinearLayout) findViewById(R.id.ll_login);
        btnSignUp = (FancyButton) findViewById(R.id.btnSignUp);

        circularProgressBar = (CircularProgressBar) findViewById(R.id.CircularProgressbar);
        circularProgressBar.setColor(getResources().getColor(R.color.colorPrimary));
        circularProgressBar.setBackgroundColor(getResources().getColor(R.color.white));


        String username = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString(DataConstant.USERNAME, "");
        String password = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getString(DataConstant.PASSWORD, "");

        edtUserName.setText(username);
        edtPassword.setText(password);

        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", false);
        //boolean isLogin = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isLogIn", false);
        //CHECK IS FIRST RUN TO SHOW INTRO APP   & SHOW LOGIN
        if (isFirstRun == false) {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", true).commit();
            Intent intro = new Intent(getBaseContext(), Intro.class);
            startActivity(intro);
            finish();
        }
//        } else if (!isLogin) {
//            Intent login = new Intent(getBaseContext(), LoginActivity.class);
//            startActivity(login);
//            finish();
//        }
        if (username.compareTo("") != 0 && password.compareTo("") != 0 && DataConstant.autoLogIn) {
            login();
            DataConstant.autoLogIn = false;
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(llLogin, "Oops! This funtion has not been supported in this version! Thanks you ^^", Snackbar.LENGTH_INDEFINITE).setActionTextColor(Color.parseColor("#FF5722")).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    private void login() {


        String username = edtUserName.getText().toString();
        String password = edtPassword.getText().toString();

        if (username.compareTo("demo") == 0) {

            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogIn", true).commit();
            DataConstant.USER_DEFAULT.setName(username);

            //getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isLogIn", true).commit();
            DataConstant.isLogIn = true;
            Intent main = new Intent(getBaseContext(), MainActivity.class);
            startActivity(main);
            finish();
        } else {
            if (username.compareTo("") == 0 || password.compareTo("") == 0) {
                Toast.makeText(LoginActivity.this, "username or password is empty", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < llLogin.getChildCount(); i++) {
                    View v = llLogin.getChildAt(i);
                    v.setEnabled(false);
                }
                int animationDuration = HttpConstant.TIME_CONNECT_LIMIT; // 2500ms = 2,5s
                CircularProgressBar circularProgressBar = getCircularProgressBar();
                circularProgressBar.setProgress(0);
                circularProgressBar.setProgressWithAnimation(100, animationDuration); // Default duration = 1500ms
                circularProgressBar.setVisibility(View.VISIBLE);

                Tenant tenant = new Tenant("s2uet");
                User user = DataConstant.USER_DEFAULT;
                user.setName(username);
                user.setPassword(password);
                user.setTenant(tenant);

                IdentifyAPI requestLogin = new IdentifyAPI(HttpConstant.URL_DEFAULT + ":5000/v2.0/tokens", user);

                new LoginTask(LoginActivity.this).execute(requestLogin);


            }
        }
//        llLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(edtUserName.getWindowToken(), 0);
//            }
//        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    public boolean onKey(View v, int keyCode, KeyEvent event) {
        switch (v.getId()) {
            case R.id.edtUserNameInLogIn:
                if (keyCode == 13)//59 is shift's keycode
                    edtPassword.requestFocus();
                break;
            case R.id.edtPasswordInLogIn:
                if (keyCode == 13)//59 is shift's keycode
                    login();
                break;
            default:
                break;
        }
        return true;
    }

    public CircularProgressBar getCircularProgressBar() {
        return circularProgressBar;
    }

    public LinearLayout getLlLogin() {
        return llLogin;
    }


    @Override
    protected void onResume() {
        DataConstant.clearCacheFile(this);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        DataConstant.clearCacheFile(this);
        super.onDestroy();
    }
}



