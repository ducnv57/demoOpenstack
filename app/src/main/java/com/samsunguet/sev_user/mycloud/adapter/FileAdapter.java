package com.samsunguet.sev_user.mycloud.adapter;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat.Builder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.leonardoxh.fakesearchview.FakeSearchView;
import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.R;
import com.samsunguet.sev_user.mycloud.SubFolderActivity;
import com.samsunguet.sev_user.mycloud.log.MyLog;
import com.samsunguet.sev_user.mycloud.object.MyFile;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.task.DeleteFileTask;
import com.samsunguet.sev_user.mycloud.task.DownloadFileTask;
import com.samsunguet.sev_user.mycloud.task.ReNameTask;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuCustomItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import java.io.File;
import java.util.ArrayList;

import com.github.leonardoxh.fakesearchview.FakeSearchAdapter;

/**
 * Created by sev_user on 3/9/2016.
 */
public class FileAdapter extends FakeSearchAdapter<MyFile> {
    BaseActivity context;
    ArrayList<MyFile> myArray = null;
    int layoutId;

    private NotificationManager mNotifyManager;
    private Builder mBuilder;
    DroppyMenuPopup droppyMenu;
    int pos;
    String pathrename;

    public FileAdapter(BaseActivity context,
                       int layoutId,
                       ArrayList<MyFile> arr) {
        super(arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() >= 0 && position >= 0) {
            TextView tvFolderName = (TextView) convertView.findViewById(R.id.tvFileName);
            TextView tvFolderDescription = (TextView) convertView.findViewById(R.id.tvFileDescription);
            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIconInItemFile);

            final MyFile myFile = myArray.get(position);
            tvFolderName.setText(myFile.getName());
            tvFolderDescription.setText(myFile.getLast_modified_coloful());


            final ImageView imgMoreInFile = (ImageView) convertView.findViewById(R.id.imgMoreButtonInFileItem);
            // SET ICON
//            if(DataConstant.fileExt(myFile.getName().equals(""))){
//
//            }
            if (myFile.isFolder()) {
                imgIcon.setImageResource(R.drawable.icon_folder);
            } else {

                String ext = DataConstant.fileExt(myFile.getName());
                switch (ext) {
                    case ".jpg":
                        imgIcon.setImageResource(R.drawable.icon_jpg);
                        break;
                    case ".mp3":
                        imgIcon.setImageResource(R.drawable.ic_audio);
                        break;
                    case ".png":
                        imgIcon.setImageResource(R.drawable.icon_png);
                        break;
                    case ".pdf":
                        imgIcon.setImageResource(R.drawable.icon_file_pdf);
                        break;
                    case ".zip":
                        imgIcon.setImageResource(R.drawable.icon_zip);
                        break;
                    case ".rar":
                        imgIcon.setImageResource(R.drawable.icon_zip);
                        break;
                    case ".doc":
                        imgIcon.setImageResource(R.drawable.icon_file_doc);
                        break;
                    default:
                        imgIcon.setImageResource(R.drawable.ic_cloud);
                        break;
                }
            }
            imgMoreInFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    initDroppyMenuInMain(imgMoreInFile);
                }
            });

            LinearLayout llcontent = (LinearLayout) convertView.findViewById(R.id.llcontent);


            //open file in a new activity
            llcontent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!myFile.isFolder()){
                        MyFile temp = myArray.get(position);
                        String source = temp.getPath();
                        String name = temp.getName();
                        String des = context.getFilesDir().getPath() + DataConstant.CACHE_FOLDER;
                        String desoff = context.getFilesDir().getPath() + DataConstant.DOWNLOADED_FOLDER;
                        MyLog.log("source: " + source + "\tname: " + name + "\tdes: " + des);
                        File file = new File(des + "/" + name);
                        File offlineFile = new File(desoff+"/"+name);
                        if (file.exists())
                            DataConstant.openPrivateFile(context, file);
                        else
                            if(offlineFile.exists())
                                DataConstant.openPrivateFile(context, offlineFile);
                            else
                                new DownloadFileTask(context, source, name, des, DataConstant.DOWNLOAD_AND_OPEN).execute(DataConstant.storageAPI);
                    }else{
                        MyFolder temp = (MyFolder)getItem(position);
                        String subName = temp.getName();
                        String absolutepath = temp.getPath() + "/" + temp.getName();

                        Intent i = new Intent(context, SubFolderActivity.class);
                        i.putExtra("NAME", subName);
                        i.putExtra(DataConstant.ABSOLUTEPATH, absolutepath);
                        context.startActivity(i);
                        context.finish();
                    }

                }
            });

        }

        return convertView;

    }

    // ACTION FOR DROP_MENU IN MAIN ACTIVITY
    private void initDroppyMenuInMain(ImageView img) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, img);
        droppyBuilder
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(true);
        DroppyMenuCustomItem sBarItem = new DroppyMenuCustomItem(R.layout.context_menu_more_in_file);
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
        //DELETE
        droppyMenu.getMenuView().findViewById(R.id.layout_deleteInFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);
                MyFile temp = (MyFile) context.getLvFile().getAdapter().getItem(pos);
                String path = temp.getPath() + "/" + temp.getName();
                new DeleteFileTask(context, path).execute(DataConstant.storageAPI);
            }
        });
        //DOWNLOAD
        droppyMenu.getMenuView().findViewById(R.id.layout_downloadFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);
                MyFile temp = (MyFile) context.getLvFile().getAdapter().getItem(pos);

                String source = temp.getPath();
                String name = temp.getName();
                String des = context.getFilesDir().getPath() + DataConstant.DOWNLOADED_FOLDER;
                MyLog.log("source: " + source + "\tname: " + name + "\tdes: " + des);
                new DownloadFileTask(context, source, name, des).execute(DataConstant.storageAPI);

            }
        });
        droppyMenu.getMenuView().findViewById(R.id.layout_changeNameInFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);
                Toast.makeText(context, "Change File Name", Toast.LENGTH_LONG).show();
            }
        });

        //rename
        droppyMenu.getMenuView().findViewById(R.id.layout_changeNameInFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFile file = getItem(pos);
                pathrename = file.getPath() + "/" + file.getName();
                new MaterialDialog.Builder(context)
                        .content("Enter file name")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("File name", file.getName(), new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                (new ReNameTask(context, pathrename, input.toString())).execute(DataConstant.storageAPI);
                            }
                        })
                        .show();
            }
        });
    }


    @Override
    public int getCount() {
        //return super.getCount();
        return myArray.size();
    }

    public void setList(ArrayList<MyFile> files) {
        myArray = files;
    }

    @Override
    public MyFile getItem(int position) {
        //return super.getItem(position);
        return myArray.get(position);
    }


}
