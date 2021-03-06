package edu.rose_hulman.tafkarr;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import java.util.ArrayList;

public class CourseDataAdapter {
    // Android naming convention for IDs
    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SCORE = "score";
    // Becomes the filename of the database
    private static final String DATABASE_NAME = "courses.db";
    // Only one table in this database
    private static final String TABLE_NAME = "courses";
    private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
    // We increment this every time we change the database schema which will
    // kick off an automatic upgrade
    private static final int DATABASE_VERSION = 2;
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

    public CourseDataAdapter(Context context) {
        // Create a SQLiteOpenHelper
        mOpenHelper = new CourseDbHelper(context);
    }

    public void open() {
        // Open the database
        mDatabase = mOpenHelper.getWritableDatabase();
    }

    public void close() {
        // Close the database
        mDatabase.close();
    }

    private ContentValues getContentValuesFromCourse(Course course) {
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
    public long addCourse(Course course) {
        ContentValues row = getContentValuesFromCourse(course);
        long rowId = mDatabase.insert(TABLE_NAME, null, row);
        course.setId(rowId);
        return rowId;
    }

    public Cursor getCoursesCursor() {
        String[] projection = new String[]{KEY_ID, KEY_NAME, KEY_SCORE};
        return mDatabase.query(TABLE_NAME, projection, null, null, null, null,
                KEY_SCORE + " DESC");
    }


    public boolean existsCourseWithName(String courseName) {
        String[] projection = new String[]{KEY_NAME};
        String selection = KEY_NAME + "='" + courseName + "'";
        Cursor results = mDatabase.query(TABLE_NAME, projection, selection, null, null, null, null);
        return results.getCount() != 0;
    }

    public Course getCourse(long id) {
        String[] projection = new String[]{KEY_ID, KEY_NAME, KEY_SCORE};
        String selection = KEY_ID + " = " + id;
        boolean distinctRows = true;
        Cursor c = mDatabase.query(distinctRows, TABLE_NAME, projection,
                selection, null, null, null, null, null);
        if (c != null && c.moveToFirst()) {
            return getCourseFromCursor(c);
        }
        return null;
    }

    private Course getCourseFromCursor(Cursor c) {
        Course course = new Course();
        course.setId(c.getInt(c.getColumnIndexOrThrow(KEY_ID)));
        course.setTitle(c.getString(c.getColumnIndexOrThrow(KEY_NAME)));
        course.setCourseGrade(c.getDouble(c.getColumnIndexOrThrow(KEY_SCORE)));
        return course;
    }

    public void updateCourse(Course course) {
        ContentValues row = getContentValuesFromCourse(course);
        String selection = KEY_ID + " = " + course.getId();
        mDatabase.update(TABLE_NAME, row, selection, null);
    }

    public boolean removeCourse(long id) {
        return mDatabase.delete(TABLE_NAME, KEY_ID + " = " + id, null) > 0;
    }

    public boolean removeCourses(ArrayList<Long> ids) {
        String whereClause = KEY_ID + " in (" + TextUtils.join(", ", ids) + ")";
        return mDatabase.delete(TABLE_NAME, whereClause, null) > 0;
    }

    public boolean removeCourse(Course c) {
        return removeCourse(c.getId());
    }


    public void logAll() {
        Cursor c = getCoursesCursor();
        if (c != null && c.moveToFirst()) {
//            Log.d(ScoresListActivity.SLS, "LOGGING TABLE");
            while (!c.isAfterLast()) {
                Course course = getCourseFromCursor(c);
//                Log.d(ScoresListActivity.SLS, score.toString());
                c.moveToNext();
            }
        }
    }

    private static class CourseDbHelper extends SQLiteOpenHelper {

        public CourseDbHelper(Context context) {
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
