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
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
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


public class MainActivity extends BaseActivity {

    ListView lvFolder, lvFile;
    android.support.design.widget.FloatingActionButton fab;
//    FloatingActionButton fab;
    ImageView imgHamburgerButton, imgSearchButton, imgRefreshInMainActivity;
    PullToRefreshView mPullToRefreshView;
    RelativeLayout relativeLayoutViewMyPhotos;

    //public Prefser prefser;
    public static SweetAlertDialog pDialog;
    ScrollView scrollView;
    private boolean backPressedToExitOnce;

    ArrayList<MyFolder> myFolders = new ArrayList<MyFolder>();

    ArrayList<MyFile> myFiles = new ArrayList<MyFile>();
    ArrayList<String> path;


    public static Context context;

    Drawer mDrawer = null;


    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
            return;
        }

        this.backPressedToExitOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                backPressedToExitOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //prefser = new Prefser(MainActivity.this);

        context = this;
//        SimpleFingerGestures mySfg = new SimpleFingerGestures();
        //REGISTERY
//        mPullToRefreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        lvFolder = (ListView) findViewById(R.id.lvFolder);
        lvFile = (ListView) findViewById(R.id.lvFile);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.floatButton);
        imgHamburgerButton = (ImageView) findViewById(R.id.imgHamburgerButton);
        imgSearchButton = (ImageView) findViewById(R.id.imgSearchButton);
        imgRefreshInMainActivity = (ImageView) findViewById(R.id.imgRefreshInMain);
        scrollView = (ScrollView) findViewById(R.id.scrView);
        relativeLayoutViewMyPhotos = (RelativeLayout)findViewById(R.id.ViewMyPhotos);


//SET DATA FOR LISTVIEW


        final FolderAdapter adapter1 = new FolderAdapter(
                MainActivity.this,
                R.layout.item_folder_listview, myFolders);
        FileAdapter adapter2 = new FileAdapter(
                MainActivity.this,
                R.layout.item_file_listview, myFiles);

        lvFolder.setAdapter(adapter1);
        lvFile.setAdapter(adapter2);

        refresh();

        relativeLayoutViewMyPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SubFolderActivity.class);
                String path = "/"+DataConstant.USER_DEFAULT.getUsername()+DataConstant.PHOTO_PATH;
                i.putExtra(DataConstant.ABSOLUTEPATH,path);
                startActivity(i);
            }
        });


//
        lvFolder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyFolder temp = adapter1.getItem(position);
                String subName = temp.getName();
                String absolutepath = temp.getPath() + "/" + temp.getName();

                Intent i = new Intent(MainActivity.this, SubFolderActivity.class);
                i.putExtra("NAME", subName);
                i.putExtra(DataConstant.ABSOLUTEPATH, absolutepath);
                startActivity(i);
                //finish();
            }
        });



        ListUtils.setDynamicHeight(lvFolder, mPullToRefreshView);
        ListUtils.setDynamicHeight(lvFile);

        //attach to listview
//        fab.attachToListView(listview);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DialogPlus dialog = DialogPlus.newDialog(MainActivity.this)
                        .setContentHolder(new ViewHolder(R.layout.custom_add_file_dialog))
                        .setExpanded(true)

                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(final DialogPlus dialog, View view) {
                                switch (view.getId()) {
                                    case R.id.imgFolderInDialog:
                                        dialog.dismiss();
                                        new MaterialDialog.Builder(MainActivity.this)
                                                .content("Enter folder name")
                                                .inputType(InputType.TYPE_CLASS_TEXT)
                                                .input("Folder name", "New Folder", new MaterialDialog.InputCallback() {
                                                    @Override
                                                    public void onInput(MaterialDialog dialog, CharSequence input) {
                                                        new CreateFolderTask(MainActivity.this, "/" + DataConstant.USER_DEFAULT.getUsername() + "/" + input)
                                                                .execute(DataConstant.storageAPI);
                                                    }
                                                })
                                                .show();
//
                                        break;
                                    case R.id.imgUploadFileInDialog:
                                        dialog.dismiss();
//                                        Toast.makeText(MainActivity.this, "Click Upload", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(MainActivity.this, FilePickerActivity.class);
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
                                        dialog.dismiss();
                                        break;
                                    case R.id.imgUploadPhotoInDialog:
                                        dialog.dismiss();
                                        FishBun.with(MainActivity.this)
                                                .setAlbumThumnaliSize(250)//you can resize album thumnail size
                                                .setActionBarColor(Color.parseColor("#FF5722")) // actionBar and StatusBar color
                                                        //        .setActionBarColor(Color.BLACK)           // only actionbar color
                                                .setPickerCount(50)//you can restrict photo count
                                                .setArrayPaths(path)//you can choice again.
                                                .setPickerSpanCount(2)
                                                .setRequestCode(11) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                                                .setCamera(true)//you can use camera
                                                .startAlbum();
                                        break;
                                    case R.id.imgSyncContact:
                                        Toast.makeText(MainActivity.this, "Click Sync Contact", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }

                        })
                        .create();
                dialog.show();

            }
        });


        //ADD ITEM TO DRAWER
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("Home");
        item1.withBadge("19").withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_red_700))
            .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                @Override
                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                    MyLog.log("home pos: " + position);
                    return true;
                }
            });

        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Offline File");
        item2.withIcon(R.drawable.icon_photo)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        Intent i = new Intent(MainActivity.this, OfflineFileActivity.class);
                        startActivity(i);
                        mDrawer.closeDrawer();
                        return true;
                    }
                });
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Setting");
        item3.withIcon(R.drawable.icon_setting);
        item3.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                Intent i = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(i);
                mDrawer.closeDrawer();
                return true;
            }
        });

        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("About");
        item4.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                mDrawer.closeDrawer();
                MaterialDialog.Builder builder = new MaterialDialog.Builder(MainActivity.this)
                        .title("About me")
                        .content("This is a sample app to demonstrate two modules (Swift and KeyStone) of OpenStack ..." + "\n \n" + "All rights are protected!")
                        .positiveText("AGREE");

                MaterialDialog dialog = builder.build();
                dialog.show();
                return true;
            }
        });
        item4.withIcon(R.drawable.icon_about);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.bg)
                .addProfiles(
                        new ProfileDrawerItem().withName(DataConstant.USER_DEFAULT.getUsername()).withEmail("mikepenz@gmail.com").withIcon(getResources().getDrawable(R.drawable.icon_minion))
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //create the drawer and remember the `Drawer` result object
        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4
                )
                .build();
        mDrawer = result;

        //SET ACTION
        imgHamburgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (result.isDrawerOpen()) result.closeDrawer();
                else result.openDrawer();
            }
        });

        imgSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SearchFileActivity.class);
                i.putExtra(DataConstant.ABSOLUTEPATH, getMyFolder().getPath()+"/"+getMyFolder().getName());
                startActivity(i);
                //finish();
            }
        });
        imgRefreshInMainActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });


//        mPullToRefreshView.setRefreshStyle(PullToRefreshView.STYLE_SUN);
//        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPullToRefreshView.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        refresh();
//                        mPullToRefreshView.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });

        //create root folder
        new CreateFolderTask(this, "/"+ DataConstant.USER_DEFAULT.getUsername()).execute(DataConstant.storageAPI);
    }

    //SET ACTION PULL TO REFRESH


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
                        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Uploading...");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        new UploadFileTask(this, stringArrayList
                                , "/" + DataConstant.USER_DEFAULT.getUsername()).execute(DataConstant.storageAPI);
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
        //CHOOSE IMAGE FROM LIBRARY
        if (requestCode == 11) {
            if (resultCode == RESULT_OK) {
                path = data.getStringArrayListExtra(Define.INTENT_PATH);
                //You can get image path(ArrayList<String>
                pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Uploading...");
                pDialog.setCancelable(false);
                pDialog.show();

                new UploadFileTask(this, path
                        , "/" + DataConstant.USER_DEFAULT.getUsername()).execute(DataConstant.storageAPI);

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


    public PullToRefreshView getmPullToRefreshView() {
        return mPullToRefreshView;
    }

    public ListView getLvFile() {
        return lvFile;
    }

    public ListView getLvFolder() {
        return lvFolder;
    }

    public void setData() {

        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        pDialog.dismissWithAnimation();
        FolderAdapter adapter1 = (FolderAdapter) getLvFolder().getAdapter();
        ArrayList<MyFolder> folders2 = getMyFolder().getSubFolders();

        String photopath = new String(DataConstant.PHOTO_PATH);
        String photo = photopath.replace("/","");
        MyLog.log("My photo folder: "+photo);

        MyLog.log("My photo folder: "+DataConstant.PHOTO_PATH);
        ArrayList<MyFolder> folders = new ArrayList<MyFolder>();
        for(int i=0; i<folders2.size(); i++){
            if(!folders2.get(i).getName().equals(photo)) folders.add(folders2.get(i));
        }
        adapter1.setList(folders);
        adapter1.notifyDataSetChanged();

        FileAdapter fileAdapter = (FileAdapter) getLvFile().getAdapter();
        fileAdapter.setList(getMyFolder().getSubFiles());
        fileAdapter.notifyDataSetChanged();
        pDialog.dismissWithAnimation();
        ListUtils.setDynamicHeight(lvFolder, mPullToRefreshView);
        ListUtils.setDynamicHeight(lvFile);

    }

    public MainActivity getMainActivity() {
        return this;
    }

    @Override
    protected void onResume() {
        if (!DataConstant.checkAutherial(this)) {

        }else {
            mDrawer.setSelectionAtPosition(1);
            setData();
        }
        super.onResume();
    }

    public void refresh(){
        new LoadFolderTask(this,"").execute(DataConstant.storageAPI);
    }

    @Override
    public void showresultdialog(String message) {
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

    @Override
    public void showprogressingdialog(String message) {
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(message);
        pDialog.setCancelable(false);
        pDialog.show();
    }


    @Override
    public void showdialog(boolean show) {
        if(!show) pDialog.hide();
    }

    @Override
    protected void onDestroy() {
        DataConstant.clearCacheFile(this);
        super.onDestroy();
    }
}
