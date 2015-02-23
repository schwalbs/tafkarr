package edu.rose_hulman.tafkarr;

import android.content.Context;
import android.view.ActionMode;
import android.view.MenuItem;

public class CategoryMultiSelectListener extends BaseMultiSelectListener {
    public CategoryMultiSelectListener(Context context) {
        super(context);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }
}
