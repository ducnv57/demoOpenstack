package com.samsunguet.sev_user.mycloud;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.melnykov.fab.FloatingActionButton;
import com.rey.material.widget.Switch;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.task.UploadFileTask;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuCustomItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by MeHuong on 3/7/2016.
 */
public class SettingActivity extends BaseActivity {


    FloatingActionButton fab;
    ImageView imgAvatar, imgCover, imgBackInSetting;
    TextView tvUserName;
    RelativeLayout layoutSignOut;
    LinearLayout layoutChangeAvatar, layoutChangeCover;
    DroppyMenuPopup droppyMenu;
    Switch swContactSync, swPhotoSync;
    boolean isContactSync, isPhotoSync;
    public static final String CAMERA_IMAGE_BUCKET_NAME =
            Environment.getExternalStorageDirectory().toString()
                    + "/DCIM/Camera";
    public static final String CAMERA_IMAGE_BUCKET_ID =
            getBucketId(CAMERA_IMAGE_BUCKET_NAME);
    ArrayList<String> stringArrayList = new ArrayList<String>();

    /**
     * Matches code in MediaProvider.computeBucketValues. Should be a common
     * function.
     */
    public static String getBucketId(String path) {
        return String.valueOf(path.toLowerCase().hashCode());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        //REGISTERY
        fab = (FloatingActionButton) findViewById(R.id.fab);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatarInSetting);
        imgCover = (ImageView) findViewById(R.id.imgCoverInSetting);
        tvUserName = (TextView) findViewById(R.id.tvUserNameInSetting);
        imgBackInSetting = (ImageView) findViewById(R.id.imgBackInSetting);
        layoutSignOut = (RelativeLayout) findViewById(R.id.setting3);
        swContactSync = (Switch) findViewById(R.id.switch_contactsync);
        swPhotoSync = (Switch) findViewById(R.id.switch_photosync);

        isContactSync = getSharedPreferences(DataConstant.PREFERENCE, MODE_PRIVATE)
                .getBoolean(DataConstant.CONTACTSYNC, true);
        isPhotoSync = getSharedPreferences(DataConstant.PREFERENCE, MODE_PRIVATE)
                .getBoolean(DataConstant.PHOTOSYNC, true);
        swContactSync.setChecked(isContactSync);
        swPhotoSync.setChecked(false);

        imgBackInSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDroppyMenu(fab);

            }
        });
        layoutSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(SettingActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("You must be logged in again!")
                        .setConfirmText("Log out")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Toast.makeText(SettingActivity.this, "You are log out!", Toast.LENGTH_SHORT).show();
                                DataConstant.isLogIn = false;
                                finish();
                            }
                        }).show();
            }
        });
        // ACTION EDIT PROFILE

        //switch change status
        swContactSync.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                getSharedPreferences(DataConstant.PREFERENCE, MODE_PRIVATE)
                        .edit().putBoolean(DataConstant.CONTACTSYNC, checked).commit();

            }
        });
        swPhotoSync.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                getSharedPreferences(DataConstant.PREFERENCE, MODE_PRIVATE)
                        .edit().putBoolean(DataConstant.PHOTOSYNC, checked).commit();
                if (checked == true) {
                    stringArrayList = getCameraImages(SettingActivity.this);
                    new AlertDialogWrapper.Builder(SettingActivity.this)
                            .setTitle("Infomation")
                            .setMessage("All photos will be sync to your cloud?")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String path = "/"+ DataConstant.USER_DEFAULT.getUsername()+ DataConstant.PHOTO_PATH;

                                    new UploadFileTask(SettingActivity.this, stringArrayList
                                            , path).execute(DataConstant.storageAPI);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    swPhotoSync.setChecked(false);
                                }
                            }).show();
                }
            }
        });

    }

    private void initDroppyMenu(FloatingActionButton btn) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(SettingActivity.this, fab);
        droppyBuilder
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(true);
        DroppyMenuCustomItem sBarItem = new DroppyMenuCustomItem(R.layout.context_menu_edit_profile);
        droppyBuilder.addMenuItem(sBarItem);

// Set Callback handler
        droppyBuilder.setOnClick(new DroppyClickCallbackInterface() {
            @Override
            public void call(View v, int id) {
                switch (id) {

                }
            }
        });

        droppyMenu = droppyBuilder.build();
        droppyMenu.show();


        //SET ACTION
        layoutChangeAvatar = (LinearLayout) droppyMenu.getMenuView().findViewById(R.id.layout_changeAvatar);
        layoutChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Change Avatar", Toast.LENGTH_LONG).show();
            }
        });

        layoutChangeCover = (LinearLayout) droppyMenu.getMenuView().findViewById(R.id.layout_changeCover);
        layoutChangeCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SettingActivity.this, "Change Cover", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        if (!DataConstant.checkAutherial(this)) {

        }
        super.onResume();

        //super.onResume();
    }

    public static ArrayList<String> getCameraImages(Context context) {
        final String[] projection = {MediaStore.Images.Media.DATA};
        final String selection = MediaStore.Images.Media.BUCKET_ID + " = ?";
        final String[] selectionArgs = {CAMERA_IMAGE_BUCKET_ID};
        final Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        ArrayList<String> result = new ArrayList<String>(cursor.getCount());
        if (cursor.moveToFirst()) {
            final int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            do {
                final String data = cursor.getString(dataColumn);
                result.add(data);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    @Override
    public void setMyFolder(MyFolder folder) {
        super.setMyFolder(folder);
    }

    @Override
    public MyFolder getMyFolder() {
        return super.getMyFolder();
    }

    @Override
    public void setData() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public void showresultdialog(String message) {

    }

    @Override
    public void showprogressingdialog(String message) {

    }

    @Override
    public void showdialog(boolean show) {

    }

    @Override
    public ListView getLvFile() {
        return null;
    }

    @Override
    public ListView getLvFolder() {
        return null;
    }
}
