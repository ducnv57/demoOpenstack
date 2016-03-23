package com.samsunguet.sev_user.mycloud.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.samsunguet.sev_user.mycloud.DataConstant;
import com.samsunguet.sev_user.mycloud.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sev_user on 3/16/2016.
 */
public class OfflineFileAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<File> mFiles;

    public OfflineFileAdapter(Context context, ArrayList<File> files){
        this.mContext   = context;
        this.mFiles     = files;
    }
    @Override
    public int getCount() {
        return mFiles.size();
    }

    @Override
    public File getItem(int position) {
        return mFiles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)mContext).getLayoutInflater();
        convertView = layoutInflater.inflate(R.layout.item_file_listview, null);

        TextView tvFileName = (TextView) convertView.findViewById(R.id.tvFileName);
        TextView tvFileDescription = (TextView) convertView.findViewById(R.id.tvFileDescription);

        final File myFile = mFiles.get(position);
        tvFileName.setText(myFile.getName());

        tvFileDescription.setText("Last modified: " + DataConstant.formatDate(new Date(myFile.lastModified())));
        LinearLayout llcontent = (LinearLayout) convertView.findViewById(R.id.llcontent);

        //open file in a new activity
        llcontent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataConstant.openPrivateFile(mContext, myFile);
            }
        });

        return convertView;
    }
}
