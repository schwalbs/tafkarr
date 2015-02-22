package edu.rose_hulman.tafkarr;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;

import java.util.ArrayList;

/**
 * Created by gartzkds on 2/21/2015.
 */
public abstract class BaseMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    protected Context mContext;
    protected ArrayList<Integer> mSelectedRows;


    public BaseMultiChoiceModeListener(Context context) {
        mContext = context;
        mSelectedRows = new ArrayList<>();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // what to do when item is checked
        if (checked) {
            this.mSelectedRows.add(Integer.valueOf(position));
        } else {
            this.mSelectedRows.remove(Integer.valueOf(position));
        }
        mode.setSubtitle(this.mContext.getString(R.string.multi_select_sub_title, this.mSelectedRows.size()));
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        MenuInflater pump = mode.getMenuInflater();
        pump.inflate(R.menu.menu_multi_select, menu);
        mode.setTitle(R.string.multi_select_delete_title);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        //nothing to do
        return false;
    }

    @Override
    public abstract boolean onActionItemClicked(ActionMode mode, MenuItem item);

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mSelectedRows = new ArrayList<>();
    }
}
