package com.samsunguet.sev_user.mycloud;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.leonardoxh.fakesearchview.FakeSearchView;
import com.samsunguet.sev_user.mycloud.adapter.FileAdapter;
import com.samsunguet.sev_user.mycloud.object.MyFile;
import com.samsunguet.sev_user.mycloud.object.MyFolder;

import java.util.ArrayList;

/**
 * Created by sev_user on 3/22/2016.
 */
public class SearchFileActivity extends BaseActivity implements FakeSearchView.OnSearchListener {
    ListView lvFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_file_activity);
        lvFile = (ListView) findViewById(R.id.lvFileInSearch);

        setMyFolderfromPath();
//        android.support.v7.app.ActionBar bar = getSupportActionBar();
//        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#93E9FA"));
//        bar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        final ArrayList<MyFile> myFiles = new ArrayList<MyFile>();

        ArrayList<MyFile> files = getMyFolder().getAllSubFiles();
        ArrayList<MyFolder> folders = getMyFolder().getAllSubFolders();

        for(int i=0; i<folders.size(); i++)
            myFiles.add(folders.get(i));
        for(int i=0; i<files.size(); i++){
            myFiles.add(files.get(i));
        }

        lvFile.setAdapter(new FileAdapter(this, R.layout.item_file_listview, myFiles));
        lvFile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyFile temp = myFiles.get(position);
                if(!temp.isFolder()) return;
                String subName = temp.getName();
                String absolutepath = temp.getPath() + "/" + temp.getName();

                Intent i = new Intent(SearchFileActivity.this, SubFolderActivity.class);
                i.putExtra(DataConstant.ABSOLUTEPATH, absolutepath);
                i.putExtra("NAME", subName);
                startActivity(i);
                finish();
            }
        });
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
    public ListView getLvFile() {
        return null;
    }

    @Override
    public ListView getLvFolder() {
        return null;
    }

    @Override
    public void onSearch(FakeSearchView fakeSearchView, CharSequence constraint) {
        ((FileAdapter) lvFile.getAdapter()).getFilter().filter(constraint);

    }

    @Override
    public void onSearchHint(FakeSearchView fakeSearchView, CharSequence constraint) {
        ((FileAdapter) lvFile.getAdapter()).getFilter().filter(constraint);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        initSearchView(menu);
        return true;
    }

    private void initSearchView(Menu menu) {
        MenuItem item = menu.findItem(R.id.fake_search);
        FakeSearchView fakeSearchView = (FakeSearchView) MenuItemCompat.getActionView(item);
        fakeSearchView.setOnSearchListener(this);
    }

    public void setMyFolderfromPath(){
        String abpath = getIntent().getStringExtra(DataConstant.ABSOLUTEPATH);
        setMyFolder(DataConstant.getFolderfromRoot(abpath));
    }

    @Override
    protected void onResume() {
        if (!DataConstant.checkAutherial(this)) {

        }else {
            MyFolder folder = getMyFolder();
            if(getMyFolder()!= DataConstant.getFolderfromRoot(folder.getPath() + "/" + folder.getName())){
                setMyFolderfromPath();

            }
        }
        super.onResume();
    }
}
