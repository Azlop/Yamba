package com.marakana.Yamba;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created with IntelliJ IDEA.
 * User: luis
 * Date: 8/3/13
 * Time: 11:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimelineActivity extends Activity {
    DbHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    ListView listTimelime;
    TimelineAdapter adapter;

    static final SimpleCursorAdapter.ViewBinder VIEW_BINDER = new SimpleCursorAdapter.ViewBinder() {

        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
            if(view.getId() != R.id.textCreatedAt){
                return false;
            }

            // Update the created at text to relative time
            long timestamp = cursor.getLong(columnIndex);
            CharSequence relTime = DateUtils.getRelativeTimeSpanString(view.getContext(), timestamp);
            ((TextView) view).setText(relTime);

            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        listTimelime = (ListView) findViewById(R.id.listTimeline);

        // Connect to dabatase
        dbHelper = new DbHelper(this);
        db = dbHelper.getReadableDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get data from DB
        cursor = db.query(DbHelper.TABLE, null, null, null, null, null, DbHelper.C_CREATED_AT+" DESC");
        startManagingCursor(cursor);

        adapter = new TimelineAdapter(this, cursor);
        listTimelime.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        db.close();
    }


}
