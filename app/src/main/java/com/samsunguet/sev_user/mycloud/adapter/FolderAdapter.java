package com.samsunguet.sev_user.mycloud.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.AlertDialogWrapper;
import com.github.leonardoxh.fakesearchview.FakeSearchAdapter;
import com.samsunguet.sev_user.mycloud.BaseActivity;
import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.R;
import com.samsunguet.sev_user.mycloud.SubFolderActivity;
import com.samsunguet.sev_user.mycloud.object.MyFolder;
import com.samsunguet.sev_user.mycloud.task.DeleteFolderTask;
import com.shehabic.droppy.DroppyClickCallbackInterface;
import com.shehabic.droppy.DroppyMenuCustomItem;
import com.shehabic.droppy.DroppyMenuPopup;
import com.shehabic.droppy.animations.DroppyFadeInAnimation;

import java.util.ArrayList;

/**
 * Created by sev_user on 3/9/2016.
 */
public class FolderAdapter extends FakeSearchAdapter<MyFolder> {
    BaseActivity context = null;
    ArrayList<MyFolder> myArray = null;
    int layoutId;
    DroppyMenuPopup droppyMenu;
    LinearLayout layoutChangeNameInFolder;
    RelativeLayout layoutBookMarkInFolder;
    LinearLayout layoutDeleteFolder;

    int pos = 0;


    public FolderAdapter(BaseActivity context,
                         int layoutId,
                         ArrayList<MyFolder> arr) {
        super(arr);
        this.context = context;
        this.layoutId = layoutId;
        this.myArray = arr;
    }

    @Override
    public int getCount() {
        return myArray.size();
    }

    public void setList(ArrayList<MyFolder> folders) {
        this.myArray = folders;
    }

    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        LayoutInflater inflater =
                context.getLayoutInflater();
        convertView = inflater.inflate(layoutId, null);

        if (myArray.size() > 0 && position >= 0) {
            TextView tvFolderName = (TextView) convertView.findViewById(R.id.tvFolderName);
            TextView tvFolderDescription = (TextView) convertView.findViewById(R.id.tvFolderDescription);

            final MyFolder myFolder = myArray.get(position);
            tvFolderName.setText(myFolder.getName());
            tvFolderDescription.setText(myFolder.getLast_modified_coloful());

            final ImageView imgMoreInFolder = (ImageView) convertView.findViewById(R.id.imgMoreButtonInFolderItem);
            imgMoreInFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = position;
                    initDroppyMenu(imgMoreInFolder);
                }
            });
            LinearLayout llcontent = (LinearLayout) convertView.findViewById(R.id.llcontent);


            //open file in a new activity
            llcontent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyFolder temp = getItem(position);
                    String subName = temp.getName();
                    String absolutepath = temp.getPath() + "/" + temp.getName();

                    Intent i = new Intent(context, SubFolderActivity.class);
                    i.putExtra("NAME", subName);
                    i.putExtra(DataConstant.ABSOLUTEPATH, absolutepath);
                    context.startActivity(i);
                }
            });


            return convertView;
        }
        return null;
    }

    private void initDroppyMenu(ImageView img) {
        DroppyMenuPopup.Builder droppyBuilder = new DroppyMenuPopup.Builder(context, img);
        droppyBuilder
                .setPopupAnimation(new DroppyFadeInAnimation())
                .triggerOnAnchorClick(true);
        DroppyMenuCustomItem sBarItem = new DroppyMenuCustomItem(R.layout.context_menu_more_in_folder);
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

        layoutChangeNameInFolder = (LinearLayout) droppyMenu.getMenuView().findViewById(R.id.layout_changeNameInFolder);
        layoutChangeNameInFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);
                Toast.makeText(context, "Change Name", Toast.LENGTH_LONG).show();
            }
        });

        layoutBookMarkInFolder = (RelativeLayout) droppyMenu.getMenuView().findViewById(R.id.layout_bookMarkInFolder);
        layoutBookMarkInFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);
                Toast.makeText(context, "BookMark", Toast.LENGTH_LONG).show();
            }
        });

        layoutDeleteFolder = (LinearLayout) droppyMenu.getMenuView().findViewById(R.id.layout_deleteInFolder);
        layoutDeleteFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                droppyMenu.dismiss(true);

                new AlertDialogWrapper.Builder(context)
                        .setTitle("Do you want to delete this folder?")
                        .setMessage("All data will be lost!")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                MyFolder folder = getItem(pos);
                                (new DeleteFolderTask(context, folder)).execute(DataConstant.storageAPI);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            }
        });

    }

    @Override
    public MyFolder getItem(int position) {
        //return super.getItem(position);
        return myArray.get(position);
    }
}
