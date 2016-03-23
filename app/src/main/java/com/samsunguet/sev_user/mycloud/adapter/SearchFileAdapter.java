package com.samsunguet.sev_user.mycloud.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.samsunguet.sev_user.mycloud.object.MyFile;

import java.util.List;

/**
 * Created by sev_user on 3/22/2016.
 */
public class SearchFileAdapter extends ArrayAdapter<MyFile> {
    public SearchFileAdapter(Context context, int resource, List<MyFile> objects) {
        super(context, resource, objects);
    }
}
