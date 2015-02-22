package edu.rose_hulman.tafkarr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class AssignmentDataAdapter {
    // Becomes the filename of the database
    private static final String DATABASE_NAME = "assignments.db";
    // Only one table in this database
    public static final String TABLE_NAME = "assignments";
    // We increment this every time we change the database schema which will
    // kick off an automatic upgrade
    private static final int DATABASE_VERSION = 2;

    // TODO: Implement a SQLite database
    private SQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;
    // Android naming convention for IDs
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";

    private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static String CREATE_STATEMENT;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLE_NAME + " (");
        sb.append(KEY_ID + " integer primary key autoincrement, ");
        sb.append(KEY_NAME + " text, ");
        sb.append(KEY_SCORE + " double");
        sb.append(")");
        CREATE_STATEMENT = sb.toString();
    }

    public AssignmentDataAdapter(Context context) {
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

    private ContentValues getContentValuesFromScore(Assignment assignment) {
        ContentValues row = new ContentValues();
        row.put(KEY_NAME, assignment.getTitle());
        row.put(KEY_SCORE, assignment.getGrade());
        return row;
    }

    /**
     * Add score to the table.
     *
     * @param assignment
     * @return id of the inserted row or -1 if failed
     */
    public long addAssignment(Assignment assignment) {
        ContentValues row = getContentValuesFromScore(assignment);
        long rowId = mDatabase.insert(TABLE_NAME, null, row);
        assignment.setId(rowId);
        return rowId;
    }

    public Cursor getAssignmentsCursor() {
        String[] projection = new String[] { KEY_ID, KEY_NAME, KEY_SCORE };
        return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
                KEY_SCORE + " DESC");
    }

    public Assignment getAssignment(long id) {
        String[] projection = new String[] { KEY_ID, KEY_NAME, KEY_SCORE };
        String selection = KEY_ID + " = " + id;
        boolean distinctRows = true;
        Cursor c = mDatabase.query(distinctRows, TABLE_NAME, projection,
                selection, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            return getAssignmentFromCursor(c);
        }
        return null;
    }

    private Assignment getAssignmentFromCursor(Cursor c) {
        Assignment assignment = new Assignment(c.getString(c.getColumnIndexOrThrow(KEY_NAME)),c.getDouble(c.getColumnIndexOrThrow(KEY_SCORE)) );
        assignment.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
        return assignment;
    }

    public void updateAssignment(Assignment assignment) {
        ContentValues row = getContentValuesFromScore(assignment);
        String selection = KEY_ID + " = " + assignment.getId();
        mDatabase.update(TABLE_NAME, row, selection, null);
    }

    public boolean removeAssignment(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + " = " + id, null) > 0;
    }

    public boolean removeAssignment(Assignment c) {
        return removeAssignment(c.getId());
    }

    public void logAll() {
        Cursor c = getAssignmentsCursor();
        if (c != null && c.moveToFirst()) {
//            Log.d(ScoresListActivity.SLS, "LOGGING TABLE");
            while (!c.isAfterLast()) {
                Assignment assignment = getAssignmentFromCursor(c);
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
