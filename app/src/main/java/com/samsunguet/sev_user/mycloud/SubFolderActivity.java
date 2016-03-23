package com.samsunguet.sev_user.mycloud;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.nononsenseapps.filepicker.FilePickerActivity;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.samsunguet.sev_user.mycloud.adapter.FileAdapter;
import com.samsunguet.sev_user.mycloud.adapter.FolderAdapter;
import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.MyFile;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.task.CreateFolderTask;
import com.samsunguet.sev_user.mycloud.task.LoadFolderTask;
import com.samsunguet.sev_user.mycloud.task.UploadFileTask;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by sev_user on 3/11/2016.
 */
public class SubFolderActivity extends BaseActivity {

    ListView lvFolder, lvFile;
    android.support.design.widget.FloatingActionButton fab;
    TextView title;
    ImageView imgHamburgerButton, imgSearchButton, imgRefresh;

    ArrayList<MyFolder> myFolders = new ArrayList<MyFolder>();
    ArrayList<MyFile> myFiles = new ArrayList<MyFile>();


    public static SweetAlertDialog pDialog;

    //MyFolder temp;
    ArrayList<String> path;

    //public StorageAPI storageAPI;
    public static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_folder_activity);

        context = this;

        setMyFolderfromPath();


//REGISTERY
        lvFolder = (ListView) findViewById(R.id.lvFolderInSubFolder);
        lvFile = (ListView) findViewById(R.id.lvFileInSubFolder);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatButtonInSubFolder);
        imgHamburgerButton = (ImageView) findViewById(R.id.imgHamburgerButtonInSubFolder);
        imgSearchButton = (ImageView) findViewById(R.id.imgSearchButtonInSubFolder);
        imgRefresh = (ImageView) findViewById(R.id.imgRefreshInSubFolder);
        title = (TextView) findViewById(R.id.tvSubFolderName);


        final FolderAdapter adapter1 = new FolderAdapter(
                this,
                R.layout.item_folder_listview, myFolders);
        FileAdapter adapter2 = new FileAdapter(
                this,
                R.layout.item_file_listview, myFiles);


        lvFolder.setAdapter(adapter1);
        lvFile.setAdapter(adapter2);
        setData();


//SET ACTION
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogPlus dialog = DialogPlus.newDialog(SubFolderActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.custom_add_file_dialog))
                        .setExpanded(true)
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final DialogPlus dialog, View view) {
                                dialog.dismiss();

                                switch (view.getId()) {
                                    case R.id.imgFolderInDialog:
                                        dialog.dismiss();
                                        Toast.makeText(SubFolderActivity.this, "Click folder", Toast.LENGTH_LONG).show();
                                        new MaterialDialog.Builder(SubFolderActivity.this)
                                                .content("Enter folder name")
                                                .inputType(InputType.TYPE_CLASS_TEXT)
                                                .input("Folder name", "New Folder", new MaterialDialog.InputCallback() {
                                                    @Override
                                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                                        new CreateFolderTask(SubFolderActivity.this, "/" + getMyFolder().getPath() + "/" + getMyFolder().getName() + "/" + input)
                                                                .execute(DataConstant.storageAPI);

                                                    }
                                                })
                                                .show();
                                        break;
                                    case R.id.imgUploadFileInDialog:
//                                        Toast.makeText(MainActivity.this, "Click Upload", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SubFolderActivity.this, FilePickerActivity.class);
                                        // This works if you defined the intent filter
                                        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

                                        // Set these depending on your use case. These are the defaults.
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, true);
                                        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                                        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

                                        // Configure initial directory by specifying a String.
                                        // You could specify a String like "/storage/emulated/0/", but that can
                                        // dangerous. Always use Android's API calls to get paths to the SD-card or
                                        // internal memory.
                                        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                                        startActivityForResult(i, 1);
                                        break;
                                    case R.id.imgUploadPhotoInDialog:
                                        dialog.dismiss();
                                        FishBun.with(SubFolderActivity.this)
                                                .setAlbumThumnaliSize(250)//you can resize album thumnail size
                                                .setActionBarColor(Color.parseColor("#FF5722")) // actionBar and StatusBar color
                                                        //        .setActionBarColor(Color.BLACK)           // only actionbar color
                                                .setPickerCount(50)//you can restrict photo count
                                                .setArrayPaths(path)//you can choice again.
                                                .setPickerSpanCount(2)
                                                .setRequestCode(12) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                                                .setCamera(true)//you can use camera
                                                .startAlbum();
                                        break;
                                    case R.id.imgSyncContact:
                                        Toast.makeText(SubFolderActivity.this, "Click Sync Contact", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }

                        })
                        .create();
                dialog.show();

            }
        });

        imgHamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SubFolderActivity.this, SearchFileActivity.class);
                i.putExtra(DataConstant.ABSOLUTEPATH, getMyFolder().getPath()+"/"+getMyFolder().getName());
                startActivity(i);
                //finish();
            }
        });
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });

        lvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyFolder temp = adapter1.getItem(position);
                String subName = temp.getName();
                String absolutepath = temp.getPath() + "/" + temp.getName();


                Intent i = new Intent(SubFolderActivity.this, SubFolderActivity.class);
                i.putExtra(DataConstant.ABSOLUTEPATH, absolutepath);
                i.putExtra("NAME", subName);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                // For JellyBean and above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ClipData clip = data.getClipData();

                    if (clip != null) {
                        ArrayList<String> stringArrayList = new ArrayList<String>();
                        for (int i = 0; i < clip.getItemCount(); i++) {
                            Uri uri = clip.getItemAt(i).getUri();

                            stringArrayList.add(uri.toString().replace("file://", ""));

                        }
                        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Uploading...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        new UploadFileTask(this, stringArrayList
                                , getMyFolder().getPath()+"/" + getMyFolder().getName()).execute(DataConstant.storageAPI);
                    }
                    // For Ice Cream Sandwich
                } else {
                    ArrayList<String> paths = data.getStringArrayListExtra
                            (FilePickerActivity.EXTRA_PATHS);

                    if (paths != null) {
                        for (String path : paths) {
                            Uri uri = Uri.parse(path);
                            // Do something with the URI
                        }
                    }
                }

            } else {
                Uri uri = data.getData();
                // Do something with the URI
            }
        }
        if (requestCode == 12) {
            if (resultCode == RESULT_OK) {
                path = data.getStringArrayListExtra(Define.INTENT_PATH);
                //You can get image path(ArrayList<String>
                pDialog = new SweetAlertDialog(SubFolderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Uploading...");
                pDialog.setCancelable(false);
                pDialog.show();

                new UploadFileTask(this, path
                        , getMyFolder().getPath()+"/" + getMyFolder().getName()).execute(DataConstant.storageAPI);

            }
        }
    }

    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView, PullToRefreshView pullToRefreshView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            MyLog.log("listutils " + mListAdapter.getCount() + "");
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();

//            ViewGroup.LayoutParams params1 = pullToRefreshView.getLayoutParams();
//            params1.height = params.height;
//            pullToRefreshView.setLayoutParams(params1);
//            pullToRefreshView.requestLayout();

        }

        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            MyLog.log("listutils " + mListAdapter.getCount() + "");
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(mListView.getWidth(), View.MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();


        }

    }

    public void setData() {

        SweetAlertDialog pDialog = new SweetAlertDialog(SubFolderActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        //storageAPI = new StorageAPI(DataConstant.USER_DEFAULT);
        MyLog.log("Sub folder name: " + DataConstant.USER_DEFAULT.getUsername() + "/" + getIntent().getStringExtra("NAME"));
        FolderAdapter adapter1 = (FolderAdapter) getLvFolder().getAdapter();
        adapter1.setList(getMyFolder().getSubFolders());
        adapter1.notifyDataSetChanged();

        FileAdapter fileAdapter = (FileAdapter) getLvFile().getAdapter();
        fileAdapter.setList(getMyFolder().getSubFiles());
        fileAdapter.notifyDataSetChanged();
        pDialog.dismissWithAnimation();

        //set title
        title.setText(getMyFolder().getName());
        ListUtils.setDynamicHeight(lvFile);
        ListUtils.setDynamicHeight(lvFile);

    }

    public void setMyFolderfromPath(){
        String abpath = getIntent().getStringExtra(DataConstant.ABSOLUTEPATH);
        setMyFolder(DataConstant.getFolderfromRoot(abpath));
    }
    public ListView getLvFile() {
        return lvFile;
    }


    public ListView getLvFolder() {
        return lvFolder;
    }

    public void refresh(){
        new LoadFolderTask(this, getMyFolder().getPath()+"/"+getMyFolder().getName()).execute(DataConstant.storageAPI);
    }

    @Override
    protected void onResume() {

        if (!DataConstant.checkAutherial(this)) {

        }else {
            MyFolder folder = getMyFolder();
            if(getMyFolder()!= DataConstant.getFolderfromRoot(folder.getPath() + "/" + folder.getName())){
                setMyFolderfromPath();
                setData();
            }
        }
        super.onResume();
    }

    @Override
    public void showresultdialog(String messeage) {
        pDialog.dismissWithAnimation();
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(messeage);
        pDialog.setConfirmText("OK");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismissWithAnimation();

            }
        });
        pDialog.show();

    }

    public void showprogressingdialog(String message) {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }
    @Override
    public void showdialog(boolean show) {
        if(!show) pDialog.hide();
    }
}




