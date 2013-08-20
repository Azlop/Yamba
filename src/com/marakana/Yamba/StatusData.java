package com.marakana.Yamba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/5/13
 * Time: 10:59 AM
 *
 * Description:
 */
public class StatusData {

    private static final String TAG = StatusData.class.getSimpleName();

    static final int VERSION = 1;
    static final String DATABASE = "timeline.db";
    static final String TABLE = "timeline";

    public static final String C_ID = "_id";
    public static final String C_CREATED_AT = "created_at";
    public static final String C_TEXT = "txt";
    public static final String C_USER = "user";

    private static final String GET_ALL_ORDER_BY = C_CREATED_AT + " DESC";

    private static final String[] MAX_CREATED_AT_COLUMNS = { "max("
            + StatusData.C_CREATED_AT + ")" };

    private static final String[] DB_TEXT_COLUMNS = { C_TEXT };

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context){
            super(context, DATABASE, null, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String sql = "create table "+TABLE+" ("+C_ID+" int primary key, "
                    +C_CREATED_AT+" int, "+C_USER+" text, "+C_TEXT+" text)";

            sqLiteDatabase.execSQL(sql);

            Log.d(TAG, "onCreated sql" + sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            sqLiteDatabase.execSQL("drop table if exists "+TABLE);
            Log.d(TAG, "onUpdated");
            onCreate(sqLiteDatabase);
        }
    }

    final DBHelper dbHelper;

    public StatusData(Context context){
        this.dbHelper = new DBHelper(context);
        Log.i(TAG, "Initialized data");
    }

    public void close(){
        this.dbHelper.close();
    }

    public void insertOrIgnore(ContentValues values){
        Log.d(TAG, "insertOrIgnore on " + values);
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        try {
            db.insertWithOnConflict(TABLE, null, values,
                    SQLiteDatabase.CONFLICT_IGNORE);
        } finally {
            db.close();
        }
    }

    // return cursor where the columns are _id, created_at, user, text
    public Cursor getStatusUpdates() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        return db.query(TABLE, null, null, null, null, null, GET_ALL_ORDER_BY);
    }

    // return timestamp of the latest status we have it the database
    public long getLatestStatusCreatedAtTime() {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE, MAX_CREATED_AT_COLUMNS, null, null, null,
                    null, null);
            try {
                return cursor.moveToNext() ? cursor.getLong(0) : Long.MIN_VALUE;
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    // return text of the status
    public String getStatusTextById(long id) {
        SQLiteDatabase db = this.dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(TABLE, DB_TEXT_COLUMNS, C_ID + "=" + id, null,
                    null, null, null);
            try {
                return cursor.moveToNext() ? cursor.getString(0) : null;
            } finally {
                cursor.close();
            }
        } finally {
            db.close();
        }
    }

    // deletes all data
    public void delete() {
        // Open Database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Delete the data
        db.delete(TABLE, null, null);

        // Close Database
        db.close();
    }
}
