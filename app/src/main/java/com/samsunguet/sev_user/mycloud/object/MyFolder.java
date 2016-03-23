package com.samsunguet.sev_user.mycloud.object;


import com.samsunguet.sev_user.mycloud.DataConstant;

import java.util.ArrayList;

/**
 * Created by sev_user on 3/7/2016.
 */
public class MyFolder extends MyFile {
    ArrayList<MyFolder> folders;
    ArrayList<MyFile> files;
    public Boolean isFolder(){
        return true;
    }

    public MyFolder() {
        folders = new ArrayList<MyFolder>();
        files = new ArrayList<MyFile>();
    }

    public MyFolder(String name, long size) {
        super(name,size,"");
        folders = new ArrayList<MyFolder>();
        files = new ArrayList<MyFile>();
    }

    public MyFolder(String path, String name, long size, String last_modified) {
        super(path,name,size,last_modified);
        folders = new ArrayList<MyFolder>();
        files = new ArrayList<MyFile>();
    }

    public MyFolder(String path, String name, long size) {
        super(path,name,size,"");
        folders = new ArrayList<MyFolder>();
        files = new ArrayList<MyFile>();
    }

    public MyFolder(String name, String last_modified) {
        super(name,0,last_modified);
        folders = new ArrayList<MyFolder>();
        files = new ArrayList<MyFile>();
    }

    public void setFolders(ArrayList<MyFolder> folders) {
        this.folders = folders;
    }

    public void setFiles(ArrayList<MyFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "Folder path: " + path + "\nname: " +
                name + "\n";
    }

    public String showAll() {
        String out = toString();

        for (int i = 0; i < folders.size(); i++) {
            out += folders.get(i).showAll();
        }
        for (int i = 0; i < files.size(); i++) {
            out += files.get(i).toString();
        }
        return out;
    }

    public void addFolderorFile(String name, long size, String last_modified) {
        //MyLog.log("add Folder or File");
        //MyLog.log(name + size + last_modified);
        if (folders == null) folders = new ArrayList<MyFolder>();
        if (files == null) files = new ArrayList<MyFile>();
        String[] arrstr = name.split("/");
        if (arrstr.length == 1) {
            //MyLog.log("length =1");
            if (name.charAt(name.length() - 1) == '/') {
                for (int i = 0; i < folders.size(); i++) {
                    MyFolder folderitem = folders.get(i);
                    if (folderitem.getName().compareTo(arrstr[0]) == 0) {
                        folderitem.setSize(size);
                        folderitem.setLast_modified(last_modified);
                        return;
                    }
                }
                MyFolder tmp = new MyFolder(this.path + "/" + this.name , arrstr[0], size, last_modified);
                //MyLog.log("add new"+tmp.toString());
                folders.add(tmp);
            } else {
                MyFile tmp = new MyFile(this.path + "/" + this.name , arrstr[0], size, last_modified);
                //MyLog.log("add new"+tmp.toString());
                files.add(tmp);
            }
        } else {
            //MyLog.log("length !=1");
            String name2 = "";
            for (int j = 1; j < arrstr.length; j++) {
                name2 += arrstr[j];
                if(j<arrstr.length-1) name2+="/";
            }
            if (name.charAt(name.length() - 1) == '/')
                name2 += "/";
            for (int i = 0; i < folders.size(); i++) {
                MyFolder folderitem = folders.get(i);
                if (folderitem.getName().compareTo(arrstr[0]) == 0) {
                    folderitem.addFolderorFile(name2, size, last_modified);
                    return;
                }
            }
            MyFolder folder = new MyFolder(this.path  + "/" + this.name, arrstr[0], 0, "0");
            folders.add(folder);
            folder.addFolderorFile(name2, size, last_modified);
        }
    }

    
    public ArrayList<MyFolder> getSubFolders(){return folders;}
    public ArrayList<MyFile> getSubFiles(){return files;}

    public ArrayList<MyFile> getAllSubFiles(){
        ArrayList<MyFile>  results = new ArrayList<MyFile>();
        DataConstant.addListtoList(getSubFiles(), results);
        for(int i=0; i<getSubFolders().size(); i++){
            ArrayList<MyFile> file = getSubFolders().get(i).getAllSubFiles();
            DataConstant.addListtoList(file, results);
        }

        return results;
    }
    public ArrayList<MyFolder> getAllSubFolders(){
        ArrayList<MyFolder> results = new ArrayList<MyFolder>();
        DataConstant.addListtoList(getSubFolders(), results, true);
        for(int i=0; i<getSubFolders().size(); i++){
            ArrayList<MyFolder> folders = getSubFolders().get(i).getAllSubFolders();
            DataConstant.addListtoList(folders, results, true);
        }


        return results;
    }
}
