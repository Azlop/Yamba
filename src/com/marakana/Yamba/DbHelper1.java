package com.marakana.Yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/5/13
 * Time: 10:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class DbHelper1 extends SQLiteOpenHelper {

    static final String TAG = "DbHelper";
    static final String DB_NAME = "timeline.db";
    static final int DB_VERSION = 1; // <3>
    static final String TABLE = "timeline";
    static final String C_ID = BaseColumns._ID;
    static final String C_CREATED_AT = "created_at";
    static final String C_SOURCE = "source";
    static final String C_TEXT = "txt";
    static final String C_USER = "user";
    Context context;

    // Constructor
    public DbHelper1(Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table "+TABLE+" ("+C_ID+" int primary key, "
                +C_CREATED_AT+" int, "+C_SOURCE+" text, "+C_USER+" text, "+C_TEXT+" text)";

        sqLiteDatabase.execSQL(sql);

        Log.d(TAG, "onCreated sql"+sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE);
        Log.d(TAG, "onUpdated");
        onCreate(sqLiteDatabase);
    }
}
