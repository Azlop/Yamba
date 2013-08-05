package com.marakana.Yamba;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/5/13
 * Time: 11:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimelineActivity extends Activity {

    DbHelper1 dbHelper1;
    SQLiteDatabase db;
    Cursor cursor;
    ListView listTimeline;
    TimelineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        listTimeline = (ListView) findViewById(R.id.listTimeline);

        dbHelper1 = new DbHelper1(this);
        db = dbHelper1.getReadableDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get the data from the database
        cursor = db.query(DbHelper1.TABLE, null, null, null, null, null,
                DbHelper1.C_CREATED_AT + " DESC");         // <6>

        // Create the adapter
        adapter = new TimelineAdapter(this, cursor);  // <7>
        listTimeline.setAdapter(adapter);
    }
}
