package com.samsunguet.sev_user.mycloud.adapter;

import android.content.Context;
import android.database.AbstractCursor;
import android.database.Cursor;
import android.text.TextUtils;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by sev_user on 3/21/2016.
 */
public class SearchSuggestionsAdapter extends SimpleCursorAdapter
{
    private static final String[] mFields  = { "_id", "result" };
    private static final String[] mVisible = { "result" };
    private static final int[]    mViewIds = { android.R.id.text1 };

    ArrayList<String> list;

    public SearchSuggestionsAdapter(Context context, ArrayList<String> list)
    {
        super(context, android.R.layout.simple_list_item_1, null, mVisible, mViewIds, 0);
        this.list = list;
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint)
    {
        return new SuggestionsCursor(constraint, list);
    }

    private static class SuggestionsCursor extends AbstractCursor
    {
        private ArrayList<String> mResults;


        public SuggestionsCursor(CharSequence constraint, ArrayList<String> list)
        {
            final int count = 100;
            mResults = list;

            if(!TextUtils.isEmpty(constraint)){
                String constraintString = constraint.toString().toLowerCase(Locale.ROOT);
                Iterator<String> iter = mResults.iterator();
                while(iter.hasNext()){
                    if(!iter.next().toLowerCase(Locale.ROOT).startsWith(constraintString))
                    {
                        iter.remove();
                    }
                }
            }
        }

        @Override
        public int getCount()
        {
            return mResults.size();
        }

        @Override
        public String[] getColumnNames()
        {
            return mFields;
        }

        @Override
        public long getLong(int column)
        {
            if(column == 0){
                return mPos;
            }
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public String getString(int column)
        {
            if(column == 1){
                return mResults.get(mPos);
            }
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public short getShort(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public int getInt(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public float getFloat(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public double getDouble(int column)
        {
            throw new UnsupportedOperationException("unimplemented");
        }

        @Override
        public boolean isNull(int column)
        {
            return false;
        }
    }
}
