package com.samsunguet.sev_user.mycloud.object;

import com.github.leonardoxh.fakesearchview.SearchItem;
import com.samsunguet.sev_user.mycloud.DataConstant;

import java.util.Date;
import java.util.Locale;

/**
 * Created by sev_user on 3/7/2016.
 */
public class MyFile implements SearchItem {
    String path;        // no / at the end.  example  /longdt/hello
    String name;
    long size;
    String last_modified;
    public Boolean isFolder(){
        return false;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public String getLast_modified_coloful() {
        Date date = DataConstant.getDatefromString(last_modified);
        return "Last modified: " + DataConstant.formatDate(date);
    }

    public void setLast_modified(String last_modified) {

        this.last_modified = last_modified.split(".")[0];
    }

    public MyFile(String name, String last_modified) {
        this.name = name;
        this.last_modified = formatDate(last_modified);
    }

    public MyFile(String name, long size, String last_modified) {
        this.name = name;
        this.size = size;
        this.last_modified = formatDate(last_modified);
    }

    public MyFile(String path, String name, long size, String last_modified) {
        this.name = name;
        this.size = size;
        this.last_modified = formatDate(last_modified);
        this.path = path;
    }

    public MyFile() {
    }


    public String toString() {
        return "path: " + path + "\nname: " + name + "\nsize: "
                + size + "\nlast modified:" + last_modified + "\n";
    }

    private String formatDate(String date) {
        if (date.length() > 19) {
            return date.substring(0, 19);
        }
        return date;
    }

    @Override
    public boolean match(CharSequence constraint) {
        return name.toLowerCase(Locale.US)
                .startsWith(constraint.toString().toLowerCase(Locale.US));
    }
}
