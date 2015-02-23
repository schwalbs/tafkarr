package edu.rose_hulman.tafkarr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ClassDataAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";
    // Becomes the filename of the database
    private static final String DATABASE_NAME = "courses.db";
    private static final String TABLE_NAME = "courses";
    private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final int DATABASE_VERSION = 4;
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
    private SQLiteOpenHelper mOpenHelper;
    private SQLiteDatabase mDatabase;

    public ClassDataAdapter(Context context) {
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

    private ContentValues getContentValuesFromScore(Course course) {
        ContentValues row = new ContentValues();
        row.put(KEY_NAME, course.getTitle());
        row.put(KEY_SCORE, course.getCourseGrade());
        return row;
    }

    /**
     * Add score to the table.
     *
     * @param course
     * @return id of the inserted row or -1 if failed
     */
    public long addScore(Course course) {
        ContentValues row = getContentValuesFromScore(course);
        long rowId = mDatabase.insert(TABLE_NAME, null, row);
        course.setId(rowId);
        return rowId;
    }

    public Cursor getScoresCursor() {
        String[] projection = new String[]{KEY_ID, KEY_NAME, KEY_SCORE};
        return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
                KEY_SCORE + " DESC");
    }

    public Course getScore(long id) {
        String[] projection = new String[]{KEY_ID, KEY_NAME, KEY_SCORE};
        String selection = KEY_ID + " = " + id;
        boolean distinctRows = true;
        Cursor c = mDatabase.query(distinctRows, TABLE_NAME, projection,
                selection, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            return getScoreFromCursor(c);
        }
        return null;
    }

    private Course getScoreFromCursor(Cursor c) {
        Course course = new Course();
        course.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
        course.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_NAME)));
        course.setCourseGrade(c.getDouble(c.getColumnIndexOrThrow(KEY_SCORE)));
        return course;
    }

    public void updateScore(Course course) {
        ContentValues row = getContentValuesFromScore(course);
        String selection = KEY_ID + " = " + course.getId();
        mDatabase.update(TABLE_NAME, row, selection, null);
    }

    public boolean removeScore(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + " = " + id, null) > 0;
    }

    public boolean removeScore(Course c) {
        return removeScore(c.getId());
    }

    public void logAll() {
        Cursor c = getScoresCursor();
        if (c != null && c.moveToFirst()) {
//            Log.d(ScoresListActivity.SLS, "LOGGING TABLE");
            while (!c.isAfterLast()) {
                Course course = getScoreFromCursor(c);
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
