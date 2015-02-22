package edu.rose_hulman.tafkarr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CategoryDataAdapter {
    // Becomes the filename of the database
    private static final String DATABASE_NAME = "categorys.db";
    // Only one table in this database
    private static final String TABLE_NAME = "categorys";
    // We increment this every time we change the database schema which will
    // kick off an automatic upgrade
    private static final int DATABASE_VERSION = 2;

    // TODO: Implement a SQLite database
    private SQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;
    // Android naming convention for IDs
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CLASS = "class";
    public static final String KEY_WEIGHT = "weight";

    private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String CREATE_STATEMENT;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(KEY_ID + " integer primary key autoincrement, ");
        sb.append(KEY_NAME + " text, ");
        sb.append(KEY_WEIGHT + " double, ");
        sb.append(KEY_CLASS + " long");
        sb.append(")");
        CREATE_STATEMENT = sb.toString();
    }

    public CategoryDataAdapter(Context context) {
        // Create a SQLiteOpenHelper
        mOpenHelper = new ScoreDbHelper(context);
    }

    public void open() {
        // Open the database
        mDatabase = mOpenHelper.getWritableDatabase();
    }

    public void close() {
        // Close the database
        mDatabase.close();
    }

    private ContentValues getContentValuesFromScore(Category category) {
        ContentValues row = new ContentValues();
        row.put(KEY_NAME, category.getTitle());
        row.put(KEY_WEIGHT, category.getWeight());
        row.put(KEY_CLASS, category.getClassId());
        return row;
    }

    /**
     * Add score to the table.
     *
     * @param category
     * @return id of the inserted row or -1 if failed
     */
    public long addCategory(Category category) {
        ContentValues row = getContentValuesFromScore(category);
        long rowId = mDatabase.insert(TABLE_NAME, null, row);
        category.setId(rowId);
        return rowId;
    }

    public Cursor getCategorysCursor() {
        String[] projection = new String[] { KEY_ID, KEY_NAME, KEY_WEIGHT, KEY_CLASS};
        return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
                KEY_WEIGHT + " DESC");
    }

    public Category getCategory(long id) {
        String[] projection = new String[] { KEY_ID, KEY_NAME, KEY_WEIGHT,KEY_CLASS };
        String selection = KEY_ID + " = " + id;
        boolean distinctRows = true;
        Cursor c = mDatabase.query(distinctRows, TABLE_NAME, projection,
                selection, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            return getCategoryFromCursor(c);
        }
        return null;
    }

    private Category getCategoryFromCursor(Cursor c) {
        Category category = new Category(c.getString(c.getColumnIndexOrThrow(KEY_NAME)),c.getDouble(c.getColumnIndexOrThrow(KEY_WEIGHT)),c.getLong(c.getColumnIndexOrThrow(KEY_CLASS)) );
        category.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
        return category;
    }

    public void updateCategory(Category category) {
        ContentValues row = getContentValuesFromScore(category);
        String selection = KEY_ID + " = " + category.getId();
        mDatabase.update(TABLE_NAME, row, selection, null);
    }

    public boolean removeCategory(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + " = " + id, null) > 0;
    }

    public boolean removeCategory(Category c) {
        return removeCategory(c.getId());
    }

    public void logAll() {
        Cursor c = getCategorysCursor();
        if (c != null && c.moveToFirst()) {
//            Log.d(ScoresListActivity.SLS, "LOGGING TABLE");
            while (!c.isAfterLast()) {
                Category category = getCategoryFromCursor(c);
//                Log.d(ScoresListActivity.SLS, score.toString());
                c.moveToNext();
            }
        }
    }

    private static class ScoreDbHelper extends SQLiteOpenHelper {

        public ScoreDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STATEMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_STATEMENT);
            onCreate(db);
        }

    }

}
